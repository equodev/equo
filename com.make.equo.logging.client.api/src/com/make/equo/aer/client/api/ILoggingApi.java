package com.make.equo.aer.client.api;

import com.google.gson.JsonObject;

public interface ILoggingApi {
	
	public void logError(String message, JsonObject tags);

	public void logInfo(String message, JsonObject tags);

	public void logWarning(String message, JsonObject tags);

}
