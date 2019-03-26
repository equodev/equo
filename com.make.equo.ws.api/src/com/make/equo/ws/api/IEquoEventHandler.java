package com.make.equo.ws.api;

public interface IEquoEventHandler {

	void send(String userEvent);

	void send(String userEvent, Object payload);

	void on(String eventId, JsonPayloadEquoRunnable jsonPayloadEquoRunnable);

	void on(String eventId, StringPayloadEquoRunnable stringPayloadEquoRunnable);

	<T> void on(String eventId, IEquoRunnable<T> objectPayloadEquoRunnable);

}
