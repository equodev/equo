package com.make.equo.analytics.client.api.internal;

import com.google.gson.JsonObject;

public interface AnalyticsService {

	void registerEvent(String eventKey, float value);

	void registerEvent(String eventKey, float value, JsonObject segmentation);

	void registerEvent(String eventKey, float value, String segmentationAsString);
	
}
