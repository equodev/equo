package com.make.equo.application;

import java.util.Arrays;

import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.google.gson.JsonObject;
import com.make.equo.aer.api.IEquoErrorReporter;
import com.make.equo.application.util.IAppMessageConstants;

public class LogListener implements ILogListener {
	
	private IEquoErrorReporter equoErrorReporter;
		
	@Override
	public void logging(IStatus status, String plugin) {
		if (equoErrorReporter == null){
			return;
		}
		
		JsonObject json = new JsonObject();
		json.addProperty("Stack Trace", Arrays.asList(status.getException().getStackTrace()).toString());
		json.addProperty("Crash cause", status.getException().getCause().toString());
		json.addProperty("Message", status.getMessage());
		json.addProperty("Plugin", status.getPlugin());
		
		equoErrorReporter.reportError(IAppMessageConstants.LOG_MESSAGE, status.getSeverity(), json);
	}
	
	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.DYNAMIC)
	void setAnalyticsService(IEquoErrorReporter equoErrorReporter) {
		this.equoErrorReporter = equoErrorReporter;
	}

	void unsetAnalyticsService(IEquoErrorReporter equoErrorReporter) {
		this.equoErrorReporter = null;
	}


}
