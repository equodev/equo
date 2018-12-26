package com.make.equo.application;


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
import com.make.equo.aer.api.IEquoErrorReporter;
import com.make.equo.analytics.internal.api.AnalyticsService;
import com.make.equo.application.util.IAppMessageConstants;

@Component
public class EquoStatusReporter extends WorkbenchStatusReporter{
	
	private static IEquoErrorReporter equoErrorReporter;
	
	@Inject
	Logger logger;
	
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
			if ((action & (SHOW | BLOCK)) != 0) {
				registerEvent(status, IAppMessageConstants.APP_CRASH);
			} else {
				log(status);
				registerEvent(status, IAppMessageConstants.LOG_MESSAGE);
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
		json.addProperty("Plugin", status.getPlugin());
		
		if (eventType.equals(IAppMessageConstants.APP_CRASH)) {
			json.addProperty("Stack Trace", Arrays.asList(status.getException().getStackTrace()).toString());
			json.addProperty("Crash cause", status.getException().getCause().toString());
		}
		
		equoErrorReporter.reportError(eventType, status.getSeverity(), json);
	}
	
	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.DYNAMIC)
	void setAnalyticsService(IEquoErrorReporter equoErrorReporter) {
		this.equoErrorReporter = equoErrorReporter;
	}

	void unsetAnalyticsService(IEquoErrorReporter equoErrorReporter) {
		this.equoErrorReporter = null;
	}

}
