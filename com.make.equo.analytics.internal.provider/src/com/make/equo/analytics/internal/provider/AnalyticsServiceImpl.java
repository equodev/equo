package com.make.equo.analytics.internal.provider;

import java.lang.reflect.Type;
import java.util.Base64;
import java.util.Map;
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
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.make.equo.analytics.internal.api.AnalyticsService;
import com.make.equo.analytics.internal.provider.util.IAnalyticsConstants;
import com.make.equo.analytics.internal.provider.util.IAnalyticsEventsNames;
import com.make.equo.application.api.IEquoApplication;

@Component
public class AnalyticsServiceImpl implements AnalyticsService {

	private static final String VALUE_FIELD = "value";

	private InfluxDB influxDB;
	private Gson gson;
	private long appStartTime;

	private String appName;

	private String appVersion;

	private IEquoApplication equoApplication;

	private boolean connected = false;
	
	private boolean enabled = false;

	@Activate
	public void start() {
		this.appName = getEquoAppName();
		this.appVersion = getEquoAppVersion();
		
		String equoInfluxdbUrl = getInfluxdbProperty("equo_influxdb_url");
		String equoUsername = getInfluxdbProperty("equo_username");
		String equoPassword = getInfluxdbProperty("equo_password");

		if (equoInfluxdbUrl != null && equoPassword != null && equoUsername != null) {

			equoUsername = decodeInfluxdbProperty(equoUsername);
			equoPassword = decodeInfluxdbProperty(equoPassword);
			this.influxDB = InfluxDBFactory.connect(equoInfluxdbUrl, equoUsername, equoPassword);
			influxDB.setDatabase(IAnalyticsConstants.INFLUXDB_DATABASE_NAME);
			this.gson = new Gson();
			influxDB.enableBatch(BatchOptions.DEFAULTS);
			connected = true;
		} 

	}

	
	private String getInfluxdbProperty(String propertyName) {
		String influxdbProperty = System.getProperty(propertyName);
		if (influxdbProperty == null)
			System.out.println("Initialization of Analytics Internal provider fails: The " + propertyName
					+ " Influxdb property " + " of the Equo Platform must be defined.");
		return influxdbProperty;
	}

	private String getEquoAppVersion() {
		return FrameworkUtil.getBundle(equoApplication.getClass()).getVersion().toString();
	}

	private String getEquoAppName() {
		String appName = System.getProperty("appName");
		if (appName != null) {
			return appName.toLowerCase();
		} else {
			return FrameworkUtil.getBundle(equoApplication.getClass()).getSymbolicName().toLowerCase();
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
		if (isEnabled()) {
			influxDB.write(buildBasicLog(eventKey, value).build());
		} else {
			logMessage();
		}
	}

	private Builder buildBasicLog(String eventKey, double value) {
		return addFields(eventKey, value).tag(IAnalyticsConstants.APP_NAME_TAG, appName)
				.tag(IAnalyticsConstants.APP_VERSION_TAG, appVersion);
	}

	@Override
	public void registerEvent(String eventKey, double value, JsonObject segmentation) {
		if (isEnabled()) {
			String segmentationAsString = gson.toJson(segmentation);
			registerEvent(eventKey, value, segmentationAsString);
		} else {
			logMessage();
		}
	}

	@Override
	public void registerEvent(String eventKey, double value, String segmentationAsString) {
		if (isEnabled()) {
			Point build = buildBasicLog(eventKey, value).tag(getSegmentation(segmentationAsString)).build();
			influxDB.write(build);
		} else {
			logMessage();
		}
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
		if (connected) {
			registerSessionTime();
			if (influxDB != null) {
				influxDB.close();
			}
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


	@Override
	public void enableAnalytics() {
		enabled = true;	
	}

	@Override
	public boolean isEnabled() {
		return enabled && connected;
	}
	
	private void logMessage() {
		if(!enabled)
			System.out.println("Analytics are not enabled by the Client App");
		else System.out.println("Analytics are not working because InfluxDB is not connected");
		
	}

}
