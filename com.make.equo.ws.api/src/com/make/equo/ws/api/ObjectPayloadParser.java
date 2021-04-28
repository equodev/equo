package com.make.equo.ws.api;

import com.google.gson.Gson;

public class ObjectPayloadParser<T> implements IEquoRunnableParser<T> {

	private IEquoRunnable<T> objectPayloadEquoRunnable;
	private Gson gson;

	/**
	 * Creates the payload parser for object of type T.
	 * 
	 * @param objectPayloadEquoRunnable the runnable of type T.
	 */
	public ObjectPayloadParser(IEquoRunnable<T> objectPayloadEquoRunnable) {
		this.objectPayloadEquoRunnable = objectPayloadEquoRunnable;
		this.gson = new Gson();
	}

	@Override
	public T parsePayload(Object payload) {
		if (payload == null) {
			return null;
		}
		String jsonString = gson.toJson(payload);
		Class<T> type = getEquoRunnable().type();
		T fromJson = gson.fromJson(jsonString, type);
		return fromJson;
	}

	@Override
	public IEquoRunnable<T> getEquoRunnable() {
		return objectPayloadEquoRunnable;
	}

}
