package com.make.equo.aer.internal.api;

import com.google.gson.JsonObject;

public interface IEquoCrashReporter {
	
	public void logCrash(JsonObject segmentation);
	
	public void logEclipse(JsonObject segmentation);
	
}
