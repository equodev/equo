package com.make.equo.analytics.client.provider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.make.equo.analytics.client.api.IAnalyticsApi;
import com.make.equo.analytics.internal.api.AnalyticsService;
import com.make.equo.ws.api.IEquoEventHandler;
import com.make.equo.ws.api.JsonPayloadEquoRunnable;

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

@Component
public class AnalyticsApiImpl implements IAnalyticsApi {
	protected static Logger logger = LoggerFactory.getLogger(AnalyticsApiImpl.class);

	private static final String CUSTOM_EVENT_KEY = "customEvent";
	static final int DEFAULT_COUNT = 1;
	private AnalyticsService analyticsService;
	private IEquoEventHandler equoEventHandler;

	@Activate
	public void start() {
		logger.info("Initializing Analytics Client provider...");
		equoEventHandler.on(CUSTOM_EVENT_KEY, new CustomEventPayloadRunnable());
	}

	public void registerEvent(String eventKey, int count) {
		analyticsService.registerEvent(eventKey, count);
	}

	public void registerEvent(String eventKey, int count, JsonObject segmentation) {
		analyticsService.registerEvent(eventKey, count, segmentation);
	}

	public void registerEvent(String eventKey, int count, String segmentationAsString) {
		analyticsService.registerEvent(eventKey, count, segmentationAsString);
	}

	@Reference(cardinality = ReferenceCardinality.MANDATORY)
	void setAnalyticsService(AnalyticsService analyticsService) {
		this.analyticsService = analyticsService;
	}

	@Reference(cardinality = ReferenceCardinality.MANDATORY)
	void setEquoEventHandler(IEquoEventHandler equoEventHandler) {
		this.equoEventHandler = equoEventHandler;
	}

	@Override
	public void registerEvent(String eventKey) {
		this.registerEvent(eventKey, DEFAULT_COUNT);
	}

	@Override
	public void registerEvent(String eventKey, JsonObject segmentation) {
		this.registerEvent(eventKey, DEFAULT_COUNT, segmentation);
	}

	@Override
	public void registerEvent(String eventKey, String segmentationAsString) {
		this.registerEvent(eventKey, DEFAULT_COUNT, segmentationAsString);
	}

	private class CustomEventPayloadRunnable implements JsonPayloadEquoRunnable {

		private static final long serialVersionUID = 1L;
		private static final String SEGMENTATION_KEY = "segmentation";
		private static final String EVENT_KEY = "key";

		@Override
		public void run(JsonObject payload) {
			logger.debug("custom event json payload is " + payload);
			JsonElement keyJsonElement = payload.get(EVENT_KEY);
			if (keyJsonElement == null) {
				throw new RuntimeException(
						"A \"key\" member which identified the event name must be defined in the custom Analytics event object.");
			}
			String eventKey = keyJsonElement.getAsString();
			JsonObject segmentationJsonObject = payload.getAsJsonObject(SEGMENTATION_KEY);
			if (segmentationJsonObject == null) {
				registerEvent(eventKey);
			} else {
				registerEvent(eventKey, segmentationJsonObject);
			}
		}

	}
}
