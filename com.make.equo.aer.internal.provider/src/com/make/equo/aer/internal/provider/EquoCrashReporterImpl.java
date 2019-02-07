package com.make.equo.aer.internal.provider;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.google.gson.JsonObject;
import com.make.equo.aer.internal.api.IEquoCrashReporter;
import com.make.equo.analytics.internal.api.AnalyticsService;

@Component
public class EquoCrashReporterImpl implements IEquoCrashReporter {

	private static final String CRASH = "crash";
	private static final String ECLIPSE = "eclipse";
	
	private static AnalyticsService analyticsService;
	
	@Override
	public void logCrash(JsonObject segmentation) {
		analyticsService.registerEvent(CRASH, 1, segmentation);
	}

	@Override
	public void logEclipse(JsonObject segmentation) {
		analyticsService.registerEvent(ECLIPSE, 1, segmentation);
	}
	
	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.DYNAMIC)
	void setAnalyticsService(AnalyticsService service) {
		analyticsService = service;
	}

	void unsetAnalyticsService(AnalyticsService service) {
		analyticsService = null;
	}

}
