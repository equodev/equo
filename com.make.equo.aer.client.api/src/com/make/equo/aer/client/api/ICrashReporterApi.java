package com.make.equo.aer.client.api;

import com.google.gson.JsonObject;

public interface ICrashReporterApi {
	
	   public void logCrash(JsonObject segmentation,String type, String message);

	   public void logEclipse(JsonObject segmentation, String type, String message);
	   
	   public void logCrash(String type, String message);
	   
	   public void logEclipse( String type, String message);
	   
	   

}
