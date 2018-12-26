package com.make.equo.aer.api;

import com.google.gson.JsonObject;

public interface IEquoErrorReporter {

	public void reportError(String errorType, String message);
	
	public void reportError(String errorType, String message, int severity);
	
	public void reportError(String errorType, String message, int severity, JsonObject segmentation);
	
}
