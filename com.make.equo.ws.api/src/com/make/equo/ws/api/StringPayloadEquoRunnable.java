package com.make.equo.ws.api;

public interface StringPayloadEquoRunnable extends IEquoRunnable<String> {

	@Override
	public void run(String payload);

}
