package com.make.equo.application;

import java.util.Arrays;

import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.core.services.statusreporter.StatusReporter;

import com.google.gson.JsonObject;
import com.make.equo.aer.api.IEquoErrorReporter;

public class LogListener implements ILogListener {
	
	private IEquoErrorReporter equoErrorReporter;
	
	public LogListener(IEquoErrorReporter equoErrorReporter) {
		this.equoErrorReporter = equoErrorReporter;
	}
	
	@Override
	public void logging(IStatus status, String plugin) {
		if (equoErrorReporter == null){
			return;
		}
		
		JsonObject json = new JsonObject();
		json.addProperty("Stack Trace", Arrays.asList(status.getException().getStackTrace()).toString());
		json.addProperty("Crash cause", status.getException().getCause().toString());
		json.addProperty("Plugin", status.getPlugin());
		
		if (status.matches(StatusReporter.ERROR)) {
			equoErrorReporter.logError(status.getMessage(), json);
		} else if (status.matches(StatusReporter.WARNING)) {
			equoErrorReporter.logWarning(status.getMessage(), json);
		} else if (status.matches(StatusReporter.INFO)) {
			equoErrorReporter.logInfo(status.getMessage(), json);
		}
	}
}
