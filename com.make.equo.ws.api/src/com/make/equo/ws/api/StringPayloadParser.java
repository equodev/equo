package com.make.equo.ws.api;

public class StringPayloadParser implements IEquoRunnableParser<String> {

	private IEquoRunnable<String> stringPayloadEquoRunnable;

	public StringPayloadParser(IEquoRunnable<String> StringPayloadEquoRunnable) {
		stringPayloadEquoRunnable = StringPayloadEquoRunnable;
	}

	@Override
	public String parsePayload(Object payload) {
		return payload.toString();
	}

	@Override
	public IEquoRunnable<String> getEquoRunnable() {
		return stringPayloadEquoRunnable;
	}

}
