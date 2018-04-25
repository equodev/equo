package com.make.equo.analytics.internal.api;

import com.google.gson.JsonObject;

public interface AnalyticsService {

	void registerEvent(String eventKey, double value);

	void registerEvent(String eventKey, double value, JsonObject segmentation);

	void registerEvent(String eventKey, double value, String segmentationAsString);

	void registerLaunchApp();
	
}
