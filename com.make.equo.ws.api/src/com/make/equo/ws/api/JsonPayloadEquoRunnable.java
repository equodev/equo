package com.make.equo.ws.api;

import com.google.gson.JsonObject;

public interface JsonPayloadEquoRunnable extends IEquoRunnable<JsonObject> {

	@Override
	public void run(JsonObject payload);

}
