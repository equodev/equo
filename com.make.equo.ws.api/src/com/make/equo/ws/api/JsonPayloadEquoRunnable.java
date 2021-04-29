package com.make.equo.ws.api;

import com.google.gson.JsonObject;

/**
 * Calls the declared run method when the payload is a JsonObject.
 */
public interface JsonPayloadEquoRunnable extends IEquoRunnable<JsonObject> {

	@Override
	public void run(JsonObject payload);

}
