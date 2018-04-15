package com.make.equo.analytics.client.api;

import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

import com.google.gson.JsonObject;
import com.make.equo.analytics.client.api.internal.AnalyticsService;

/**
 * Equo applications will call methods of this service to register custom events data.
 * For example, in our Netflix application example, let's suppose we want to know which
 * countries were top watchers of the movie The Wolf Of Wall Street.
 * The application may register a call to the Analytics API service whenever a movie is played,
 * somethings like this:
 * 
{@code equoAnalytics.registerEvent("movies_summary", 1, jsonObject)}
or
{@code equoAnalytics.registerEvent("movies_summary", 1, "{"title": "The Wolf of Wall Street", "Country": "Germany"}")}

*/

public class AnalyticsApi {
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	private AnalyticsService analyticsService;
	
	public void registerEvent(String eventKey, int count) {
		analyticsService.registerEvent(eventKey, count);
	}
	
	public void registerEvent(String eventKey, int count, JsonObject segmentation) {
		analyticsService.registerEvent(eventKey, count, segmentation);
	}
	
	public void registerEvent(String eventKey, int count, String segmentationAsString) {
		analyticsService.registerEvent(eventKey, count, segmentationAsString);
	}
	
}
