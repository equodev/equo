package com.make.equo.ws.api;

import com.google.gson.Gson;

public class StringPayloadParser implements IEquoRunnableParser<String> {

	private IEquoRunnable<String> stringPayloadEquoRunnable;
	private Gson gson;

	public StringPayloadParser(IEquoRunnable<String> StringPayloadEquoRunnable) {
		stringPayloadEquoRunnable = StringPayloadEquoRunnable;
		this.gson = new Gson();
	}

	@Override
	public String parsePayload(Object payload) {
		String result = gson.toJson(payload);
		return result;
	}

	@Override
	public IEquoRunnable<String> getEquoRunnable() {
		return stringPayloadEquoRunnable;
	}

}
