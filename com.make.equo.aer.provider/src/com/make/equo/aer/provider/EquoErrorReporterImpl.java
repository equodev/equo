package com.make.equo.aer.provider;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.google.gson.JsonObject;
import com.make.equo.aer.api.IEquoErrorReporter;
import com.make.equo.analytics.internal.api.AnalyticsService;

@Component
public class EquoErrorReporterImpl implements IEquoErrorReporter{

	private static final String INFO = "info";
	private static final String WARNING = "warning";
	private static final String ERROR = "error";
	private static final String CRASH = "crash";
	
	private static AnalyticsService analyticsService;
		
	private JsonObject getJson(String message, JsonObject segmentation) {
		if (segmentation != null) {
			segmentation.addProperty("Message", message);
		} else {
			segmentation = new JsonObject();
			segmentation.addProperty("Message", message);
		}
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
	public void logCrash(String message) {
		this.logCrash(message, null);
	}

	@Override
	public void logError(String message, JsonObject segmentation) {
		segmentation = getJson(message, segmentation);
		analyticsService.registerEvent(ERROR, 1, segmentation);
	}

	@Override
	public void logInfo(String message, JsonObject segmentation) {
		segmentation = getJson(message, segmentation);
		analyticsService.registerEvent(INFO, 1, segmentation);		
	}


	@Override
	public void logWarning(String message, JsonObject segmentation) {
		segmentation = getJson(message, segmentation);
		analyticsService.registerEvent(WARNING, 1, segmentation);		
	}

	@Override
	public void logCrash(String message, JsonObject segmentation) {
		segmentation = getJson(message, segmentation);
		analyticsService.registerEvent(CRASH, 1, segmentation);		
	}

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.DYNAMIC)
	void setAnalyticsService(AnalyticsService service) {
		analyticsService = service;
	}

	void unsetAnalyticsService(AnalyticsService service) {
		analyticsService = null;
	}

}
