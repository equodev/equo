package com.make.equo.ws.api;

public interface IEquoEventHandler {
	/**
	 * Sends a null data to later be transmitted using the userEvent as ID.
	 * @param userEvent the event ID.
	 */
	void send(String userEvent);
	/**
	 * Sends the specified data to later be transmitted using the userEvent as ID.
	 * @param userEvent the event ID.
	 * @param payload the data to send.
	 */
	void send(String userEvent, Object payload);
	/**
	 * Define an custom JsonPayloadEquoRunnable for specific event ID.
	 * @param eventId the id event.
	 * @param jsonPayloadEquoRunnable the runnable.
	 */
	void on(String eventId, JsonPayloadEquoRunnable jsonPayloadEquoRunnable);
	/**
	 * Define an custom StringPayloadEquoRunnable for specific event ID.
	 * @param eventId the id event.
	 * @param stringPayloadEquoRunnable the runnable.
	 */
	void on(String eventId, StringPayloadEquoRunnable stringPayloadEquoRunnable);
	/**
	 * Define an custom objectPayloadEquoRunnable for specific event ID.
	 * @param <T>
	 * @param eventId the event ID.
	 * @param objectPayloadEquoRunnable the runnable.
	 */
	<T> void on(String eventId, IEquoRunnable<T> objectPayloadEquoRunnable);

}
