package com.make.equo.ws.api;

import com.make.equo.ws.api.lambda.Newable;

/**
 * 
 * Contains the declared run method when the payload is a Object of T type.
 *
 * @param <T> the type of the payload.
 */
@FunctionalInterface
public interface IEquoRunnable<T> extends Newable<T> {
	/**
	 * Executes the defined instructions when it receives the message from websocket.
	 * @param payload the data received from message from websocket.
	 */
	public void run(T payload);
}
