package com.make.equo.ws.api;

import com.google.gson.Gson;

public class ObjectPayloadParser<T> implements IEquoRunnableParser<T> {

	private IEquoRunnable<T> objectPayloadEquoRunnable;
	private Gson gson;

	public ObjectPayloadParser(IEquoRunnable<T> objectPayloadEquoRunnable) {
		this.objectPayloadEquoRunnable = objectPayloadEquoRunnable;
		this.gson = new Gson();
	}

	@Override
	public T parsePayload(Object payload) {
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
