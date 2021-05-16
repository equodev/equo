package com.equo.ws.api;

public interface IEquoRunnableParser<T> {
	/**
	 * Parses the payload data.
	 * 
	 * @param payload the payload data.
	 * @return the parsed data to an object of type T.
	 */
	public T parsePayload(Object payload);

	/**
	 * Gets the instance type of the parser from an equo runnable.
	 * 
	 * @return the instance IEquoRunnable<T>.
	 */
	public IEquoRunnable<T> getEquoRunnable();

}
