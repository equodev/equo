package com.make.equo.aer.provider;

import java.util.Arrays;

import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.make.equo.aer.api.IEquoErrorReporter;
import com.make.equo.analytics.internal.api.AnalyticsService;

public class EquoErrorReporterImpl implements IEquoErrorReporter{

	private static AnalyticsService analyticsService;

	private String getSeverity(int severity) {
		switch (severity)
		{
			case 0:
				return "OK";
			case 1:
				return "INFO";
			case 2:
				return "WARNING";
			case 4:
				return "ERROR";
			case 8:
				return "CANCEL";
			default:
				return "";
		}
	}
	
	
	@Override
	public void reportError(String errorType, String message) {
		this.reportError(errorType, 4);
	}

	@Override
	public void reportError(String errorType, String message, int severity) {
		this.reportError(errorType, severity, null);
	}

	@Override
	public void reportError(String errorType, String message, int severity, JsonObject segmentation) {
		JsonPrimitive severityAsString = new JsonPrimitive(this.getSeverity(severity));
		JsonObject json = new JsonObject();
		json.add("Severity", severityAsString);
		analyticsService.registerEvent(errorType, 1, segmentation);
	}
	
	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.DYNAMIC)
	void setAnalyticsService(AnalyticsService analyticsService) {
		this.analyticsService = analyticsService;
	}

	void unsetAnalyticsService(AnalyticsService analyticsService) {
		this.analyticsService = null;
	}

	
}
