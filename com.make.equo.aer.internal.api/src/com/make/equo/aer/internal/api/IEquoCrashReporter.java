package com.make.equo.aer.internal.api;

import com.google.gson.JsonObject;

public interface IEquoCrashReporter {

	public void logCrash(String message);
	
	public void logCrash(String message, JsonObject segmentation);
	
}
