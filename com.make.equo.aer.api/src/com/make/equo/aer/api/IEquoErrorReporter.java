package com.make.equo.aer.api;

import com.google.gson.JsonObject;

public interface IEquoErrorReporter {

	public void logError(String message);
	
	public void logInfo(String message);
	
	public void logWarning(String message);

	public void logError(String message, JsonObject segmentation);
	
	public void logInfo(String message, JsonObject segmentation);
	
	public void logWarning(String message, JsonObject segmentation);
	
	public void logCrash(String message);
	
	public void logCrash(String message, JsonObject segmentation);
	
}
