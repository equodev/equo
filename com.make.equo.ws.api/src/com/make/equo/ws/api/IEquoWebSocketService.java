package com.make.equo.ws.api;

public interface IEquoWebSocketService {
	/**
	 * Adds event handler. Asings a instance of IEquoRunnableParser<?> to determinated action.
	 * @param actionId the action ID.
	 * @param equoRunnableParser the parser instance.
	 */
	public void addEventHandler(String actionId, IEquoRunnableParser<?> equoRunnableParser);
	/**
	 * Sends the specified data to later be transmitted using the userEvent as ID.
	 * @param userEvent the event ID.
	 * @param payload the data to send.
	 */
	public void send(String userEvent, Object payload);
	/**
	 * Gets the port number that this server listens on.
	 * @return the port number.
	 */
	public int getPort();

}
