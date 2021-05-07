package com.equo.ws.api;

public interface IEquoEventHandler {
	/**
	 * Sends a null data to later be transmitted using the userEvent as ID.
	 * 
	 * @param userEvent the event ID.
	 */
	void send(String userEvent);

	/**
	 * Sends the specified data to later be transmitted using the userEvent as ID.
	 * 
	 * @param userEvent the event ID.
	 * @param payload   the data to send.
	 */
	void send(String userEvent, Object payload);

	/**
	 * Defines a custom JsonPayloadEquoRunnable for an specific event ID.
	 * 
	 * @param eventId                 the id event.
	 * @param jsonPayloadEquoRunnable the runnable.
	 */
	void on(String eventId, JsonPayloadEquoRunnable jsonPayloadEquoRunnable);

	/**
	 * Defines a custom StringPayloadEquoRunnable for an specific event ID.
	 * 
	 * @param eventId                   the id event.
	 * @param stringPayloadEquoRunnable the runnable.
	 */
	void on(String eventId, StringPayloadEquoRunnable stringPayloadEquoRunnable);

	/**
	 * Defines a custom objectPayloadEquoRunnable for an specific event ID.
	 * 
	 * @param <T>
	 * @param eventId                   the event ID.
	 * @param objectPayloadEquoRunnable the runnable.
	 */
	<T> void on(String eventId, IEquoRunnable<T> objectPayloadEquoRunnable);

}
