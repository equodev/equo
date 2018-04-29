package com.make.equo.analytics.client.api;

import com.google.gson.JsonObject;

public interface AnalyticsApi {

	/**
	 * Register an event in which the mandatory field {<code>count</code> is 1.
	 * 
	 * @param eventKey
	 */
	public void registerEvent(String eventKey);

	/**
	 * Register an event with segmentation in which the mandatory field
	 * {<code>count</code> is 1.
	 * 
	 * @param eventKey
	 * @param segmentation
	 */
	public void registerEvent(String eventKey, JsonObject segmentation);

	/**
	 * Register an event with segmentation in which the mandatory field
	 * {<code>count</code> is 1.
	 * 
	 * @param eventKey
	 * @param segmentationAsString
	 */
	public void registerEvent(String eventKey, String segmentationAsString);

	public void registerEvent(String eventKey, int count);

	public void registerEvent(String eventKey, int count, JsonObject segmentation);

	public void registerEvent(String eventKey, int count, String segmentationAsString);

}
