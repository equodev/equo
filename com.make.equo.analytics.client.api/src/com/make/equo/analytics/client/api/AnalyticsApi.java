package com.make.equo.analytics.client.api;

import com.google.gson.JsonObject;

/**
 * Equo applications will call methods of this service to register custom events
 * data. For example, in our Netflix application example, let's suppose we want
 * to know which countries were top watchers of the movie The Wolf Of Wall
 * Street. The application may register a call to the Analytics API service
 * whenever a movie is played, somethings like this:
 * 
 * {@code equoAnalytics.registerEvent("movies_summary", jsonObject)} or
 * {@code equoAnalytics.registerEvent("movies_summary", 1, jsonObject)} or
 * {@code equoAnalytics.registerEvent("movies_summary", 1, "{"title": "The Wolf
 * of Wall Street", "Country": "Germany"}")}
 * 
 */
public interface AnalyticsApi {

	/**
	 * Register an event in which the default value of mandatory field
	 * {<code>count</code> is 1.
	 * 
	 * @param eventKey
	 */
	public void registerEvent(String eventKey);

	/**
	 * Register an event with segmentation in which the default value of the
	 * mandatory field {<code>count</code> is 1.
	 * 
	 * @param eventKey
	 * @param segmentation
	 */
	public void registerEvent(String eventKey, JsonObject segmentation);

	/**
	 * Register an event with segmentation in which the default value of the
	 * mandatory field {<code>count</code> is 1.
	 * 
	 * @param eventKey
	 * @param segmentationAsString
	 */
	public void registerEvent(String eventKey, String segmentationAsString);

	public void registerEvent(String eventKey, int count);

	public void registerEvent(String eventKey, int count, JsonObject segmentation);

	public void registerEvent(String eventKey, int count, String segmentationAsString);

}
