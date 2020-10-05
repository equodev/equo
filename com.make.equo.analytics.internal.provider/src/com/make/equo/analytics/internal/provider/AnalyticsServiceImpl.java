package com.make.equo.analytics.internal.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Point.Builder;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.make.equo.analytics.internal.api.AnalyticsService;
import com.make.equo.analytics.internal.provider.util.IAnalyticsConstants;
import com.make.equo.analytics.internal.provider.util.IAnalyticsEventsNames;
import com.make.equo.application.api.IEquoApplication;

import ch.hsr.geohash.GeoHash;

@Component
public class AnalyticsServiceImpl implements AnalyticsService {

	private static final String VALUE_FIELD = "value";
	private static final String INFO_JSON_URL = "https://ipapi.co/json"; 
	private static final int GEOHASH_PRECISION = 6; 
	
	private InfluxDB influxDB;
	private Gson gson;
	private long appStartTime;

	private String appName;

	private String appVersion;

	private IEquoApplication equoApplication;

	private static boolean connected = false;

	private static boolean enabled = false;

	@Activate
	public void start() {
		this.appName = getEquoAppName();
		this.appVersion = getEquoAppVersion();
		connect();
	}

	private boolean connect() {
		String equoInfluxdbUrl = getInfluxdbProperty("equo_influxdb_url");
		String equoUsername = getInfluxdbProperty("equo_username");
		String equoPassword = getInfluxdbProperty("equo_password");

		if (equoInfluxdbUrl != null && equoPassword != null && equoUsername != null) {

			equoUsername = decodeInfluxdbProperty(equoUsername);
			equoPassword = decodeInfluxdbProperty(equoPassword);
			this.gson = new Gson();
			try {
				this.influxDB = InfluxDBFactory.connect(equoInfluxdbUrl, equoUsername, equoPassword);
			} catch (Exception e) {
				this.influxDB = null;
			}

			if (influxDB != null) {
				influxDB.setDatabase(IAnalyticsConstants.INFLUXDB_DATABASE_NAME);
				influxDB.enableBatch(BatchOptions.DEFAULTS);
				connected = true;
			}
		} else {
			System.out.println("Connection to InfluxDB failed: InfluxDB parameters must be defined.");
			connected = false;
		}
		return connected;
	}

	private boolean isAddressReachable() {
		try (Socket socket = new Socket()) {
			URI uri = new URI(INFO_JSON_URL);
			String host = uri.getHost();
			socket.connect(new InetSocketAddress(host, 80));
			return true;
		} catch (IOException | URISyntaxException e) {
			return false;
		}
	}

	private String getInfluxdbProperty(String propertyName) {
		String influxdbProperty = System.getProperty(propertyName);
		if (influxdbProperty == null) {
			System.out.println("The " + propertyName + " Influxdb property of the Equo Platform must be defined.");
		}
		return influxdbProperty;
	}

	private String getEquoAppVersion() {
		return FrameworkUtil.getBundle(equoApplication.getClass()).getVersion().toString();
	}

	private String getEquoAppName() {
		String appName = System.getProperty("appName");
		if (appName != null) {
			return appName;
		} else {
			return FrameworkUtil.getBundle(equoApplication.getClass()).getSymbolicName();
		}
	}

	private String decodeInfluxdbProperty(String encodedParameter) {
		byte[] decodedBytes = Base64.getDecoder().decode(encodedParameter);
		String decodedParameter = new String(decodedBytes);
		return decodedParameter;
	}

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.DYNAMIC)
	void setEquoApplication(IEquoApplication equoApplication) {
		this.equoApplication = equoApplication;
	}

	void unsetEquoApplication(IEquoApplication equoApplication) {
		this.equoApplication = null;
	}

	@Override
	public void registerEvent(String eventKey, double value) {
		writeInflux(eventKey, value);
	}

	@Override
	public void registerEvent(String eventKey, double value, JsonObject segmentation) {
		writeInflux(eventKey, value, segmentation);
	}

	@Override
	public void registerEvent(String eventKey, double value, String segmentationAsString) {
		JsonParser parser = new JsonParser();
		JsonObject segmentation = parser.parse(segmentationAsString).getAsJsonObject();
		writeInflux(eventKey, value, segmentation);
	}

	private void writeInflux(String eventKey, double value) {
		writeInflux(eventKey, value, new JsonObject());
	}
	
	private void writeInflux(String eventKey, double value, JsonObject segmentation) {
		if (isEnabled()) {
			String segmentationAsString = gson.toJson(addSystemInfo(segmentation));
			Point build = buildBasicLog(eventKey, value).tag(getSegmentation(segmentationAsString)).build();
			influxDB.write(build);
		} else {
			logMessage();
		}
	}

	private Builder buildBasicLog(String eventKey, double value) {
		return addFields(eventKey, value).tag(IAnalyticsConstants.APP_NAME_TAG, appName)
				.tag(IAnalyticsConstants.APP_VERSION_TAG, appVersion);
	}


	private Builder addFields(String eventKey, double value) {
		return Point.measurement(eventKey).time(System.currentTimeMillis(), TimeUnit.MILLISECONDS).addField(VALUE_FIELD,
				value);
	}

	private Map<String, String> getSegmentation(String segmentation) {
		Type type = new TypeToken<Map<String, String>>() {
		}.getType();
		Map<String, String> segmentationMap = gson.fromJson(segmentation, type);
		return segmentationMap;
	}

	@Deactivate
	public void stop() {
		registerSessionTime();
		if (influxDB != null) {
			influxDB.close();
		}
	}

	private void registerSessionTime() {
		long endTime = System.currentTimeMillis();
		long sessionTime = endTime - appStartTime;
		System.out.println("Total execution time: " + sessionTime);
		registerEvent(IAnalyticsEventsNames.SESSION_TIME, sessionTime);
	}

	@Override
	public void registerLaunchApp() {
		appStartTime = System.currentTimeMillis();
		registerEvent(IAnalyticsEventsNames.LAUNCH_EVENT, 1);
	}

	private JsonObject addSystemInfo(JsonObject json) {
		json.addProperty("javaVendor", System.getProperty("java.vendor"));
		json.addProperty("javaVersion", System.getProperty("java.version"));
		json.addProperty("country", System.getProperty("user.country"));
		json.addProperty("gtkVersion", System.getProperty("org.eclipse.swt.internal.gtk.version"));
		json.addProperty("osName", System.getProperty("os.name"));
		json.addProperty("osVersion", System.getProperty("os.version"));

		appendGeohash(json);
		return json;
	}

	private void appendGeohash(JsonObject json) {
		Optional<JsonObject> optionalIpapiJSON = getUserInfoJson();
		if(optionalIpapiJSON.isPresent()) {
			JsonObject ipapiJSON = optionalIpapiJSON.get();
			String geoHash = GeoHash.geoHashStringWithCharacterPrecision(ipapiJSON.get("latitude").getAsDouble(), ipapiJSON.get("longitude").getAsDouble(), GEOHASH_PRECISION);
			json.addProperty("geoHash", geoHash);
		}
	}
	
	@Override
	public void enableAnalytics() {
		enabled = true;
	}

	@Override
	public boolean isEnabled() {
		if (enabled && isAddressReachable()) {
			if (connected)
				return true;
			else
				return connect();
		}
		return false;
	}

	private void logMessage() {
		if (!enabled) {
			System.out.println("Analytics are not enabled by the Client App");
		} else {
			System.out.println("Analytics are not working because InfluxDB is not connected");
		}
	}
	
	private Optional<JsonObject> getUserInfoJson() {
		try {
			JsonObject json = new JsonObject();
			URL url = new URL(INFO_JSON_URL);
			URLConnection request = url.openConnection();
			request.connect();
			JsonParser jp = new JsonParser();
			JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
			json = root.getAsJsonObject();
			return Optional.of(json);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return Optional.empty();
		} catch (IOException e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

}
