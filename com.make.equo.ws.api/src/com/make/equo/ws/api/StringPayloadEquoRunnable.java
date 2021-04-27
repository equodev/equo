package com.make.equo.ws.api;

/**
 * 
 * Contains the declared run method when the payload is a String object.
 *
 */
public interface StringPayloadEquoRunnable extends IEquoRunnable<String> {

	@Override
	public void run(String payload);

}
