package com.make.equo.aer.provider;


import java.util.Arrays;

import javax.inject.Inject;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.internal.workbench.swt.WorkbenchStatusReporter;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.google.gson.JsonObject;
import com.make.equo.analytics.internal.api.AnalyticsService;

@Component
public class EquoStatusReporter extends WorkbenchStatusReporter{
	
	private final static String APP_CRASH = "crash";
	private final static String LOG_MESSAGE = "log";

	@Inject
	Logger logger;
	
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
	public void report(IStatus status, int style, Object... information) {
		int action = style & (IGNORE | LOG | SHOW | BLOCK);
		if (action == 0) {
			if (status.matches(ERROR)) {
				action = SHOW;
			} else {
				action = LOG;
			}
		}
		if (style != IGNORE) {
			// log even if showing a dialog
			if ((action & (SHOW | BLOCK)) != 0) {
				registerEvent(status, APP_CRASH);
			} else {
				log(status);
				registerEvent(status, LOG_MESSAGE);
			}
		}
	}

	private void log(IStatus status) {
		if (status.matches(ERROR)) {
			logger.error(status.getException(), status.getMessage());
		} else if (status.matches(WARNING)) {
			logger.warn(status.getException(), status.getMessage());
		} else if (status.matches(INFO)) {
			logger.info(status.getException(), status.getMessage());
		}
	}
	
	private void registerEvent(IStatus status, String eventType) {		
		JsonObject json = new JsonObject();
		json.addProperty("Message", status.getMessage());
		json.addProperty("Severity", this.getSeverity(status.getSeverity()));
		json.addProperty("Plugin", status.getPlugin());
		
		if (eventType.equals(APP_CRASH)) {
			json.addProperty("Stack Trace", Arrays.asList(status.getException().getStackTrace()).toString());
			json.addProperty("Crash cause", status.getException().getCause().toString());
		}
		
		analyticsService.registerEvent(eventType, 1, json);
	}
	
	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.DYNAMIC)
	void setAnalyticsService(AnalyticsService analyticsService) {
		this.analyticsService = analyticsService;
	}

	void unsetAnalyticsService(AnalyticsService analyticsService) {
		this.analyticsService = null;
	}

}
