package com.make.equo.analytics.client.api;

import com.google.gson.JsonObject;

public interface AnalyticsApi {
	
	public void registerEvent(String eventKey, int count);
	
	public void registerEvent(String eventKey, int count, JsonObject segmentation);
	
	public void registerEvent(String eventKey, int count, String segmentationAsString);
	
}
