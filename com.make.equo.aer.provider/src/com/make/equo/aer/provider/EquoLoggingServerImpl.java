package com.make.equo.aer.provider;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.google.gson.JsonObject;
import com.make.equo.aer.api.IEquoLoggingService;
import com.make.equo.analytics.internal.api.AnalyticsService;

@Component
public class EquoLoggingServerImpl implements IEquoLoggingService {

	private static final String USER_LOG_MEASUREMENT = "userlogs";
	
	private static final String INFO = "Info";
	private static final String WARNING = "Warning";
	private static final String ERROR = "Error";
	
	private static AnalyticsService analyticsService;
		
	private JsonObject getJson(String message, JsonObject segmentation, String severity) {
		if (segmentation != null) {
			segmentation.addProperty("message", message);
		} else {
			segmentation = new JsonObject();
			segmentation.addProperty("message", message);
		}
		segmentation.addProperty("severity", severity);
		return segmentation;
	}
	
	@Override
	public void logError(String message) {
		this.logError(message, null);
	}

	@Override
	public void logInfo(String message) {
		this.logInfo(message, null);
	}

	@Override
	public void logWarning(String message) {
		this.logWarning(message, null);
	}

	@Override
	public void logError(String message, JsonObject segmentation) {
		segmentation = getJson(message, segmentation, ERROR);
		analyticsService.registerEvent(USER_LOG_MEASUREMENT, 1, segmentation);
	}

	@Override
	public void logInfo(String message, JsonObject segmentation) {
		segmentation = getJson(message, segmentation, INFO);
		analyticsService.registerEvent(USER_LOG_MEASUREMENT, 1, segmentation);		
	}


	@Override
	public void logWarning(String message, JsonObject segmentation) {
		segmentation = getJson(message, segmentation, WARNING);
		analyticsService.registerEvent(USER_LOG_MEASUREMENT, 1, segmentation);		
	}

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.DYNAMIC)
	void setAnalyticsService(AnalyticsService service) {
		analyticsService = service;
	}

	void unsetAnalyticsService(AnalyticsService service) {
		analyticsService = null;
	}

}
