package com.make.equo.analytics.client.provider;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Point.Builder;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.make.equo.analytics.client.api.internal.AnalyticsService;

@Component
public class AnalyticsServiceImpl implements AnalyticsService {

	private InfluxDB influxDB;
	private Gson gson;

	@Activate
	public void start() {
		this.influxDB = InfluxDBFactory.connect("http://172.17.0.2:8086", "root", "root");
		this.gson = new Gson();
		influxDB.enableBatch(BatchOptions.DEFAULTS);
	}

	@Override
	public void registerEvent(String eventKey, float value) {
		influxDB.write(addFields(eventKey, value).build());
	}

	@Override
	public void registerEvent(String eventKey, float value, JsonObject segmentation) {
		String segmentationAsString = gson.toJson(segmentation);
		registerEvent(eventKey, value, segmentationAsString);
	}

	@Override
	public void registerEvent(String eventKey, float value, String segmentationAsString) {
		Point build = addFields(eventKey, value)
						.tag(getSegmentation(segmentationAsString))
						.build();
		influxDB.write(build);
	}

	private Builder addFields(String eventKey, float value) {
		return Point.measurement(eventKey)
						.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
						.addField("value", value);
	}

	private Map<String, String> getSegmentation(String segmentation) {
		Type type = new TypeToken<Map<String, String>>(){}.getType();
		Map<String, String> segmentationMap = gson.fromJson(segmentation, type);
		return segmentationMap;
	}

}
