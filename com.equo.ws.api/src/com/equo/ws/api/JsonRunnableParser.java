package com.equo.ws.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonRunnableParser implements IEquoRunnableParser<JsonObject> {

	private JsonParser jsonParser;
	private IEquoRunnable<JsonObject> jsonPayloadEquoRunnable;
	private Gson gson;

	/**
	 * Creates the payload parser for object of type Json.
	 * 
	 * @param objectPayloadEquoRunnable the runnable of type Json.
	 */
	public JsonRunnableParser(IEquoRunnable<JsonObject> jsonPayloadEquoRunnable) {
		this.jsonPayloadEquoRunnable = jsonPayloadEquoRunnable;
		this.jsonParser = new JsonParser();
		this.gson = new Gson();
	}

	@Override
	public JsonObject parsePayload(Object payload) {
		String jsonString = gson.toJson(payload);
		return jsonParser.parse(jsonString).getAsJsonObject();
	}

	@Override
	public IEquoRunnable<JsonObject> getEquoRunnable() {
		return jsonPayloadEquoRunnable;
	}

}
