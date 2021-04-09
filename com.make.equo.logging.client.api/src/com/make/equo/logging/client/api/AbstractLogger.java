package com.make.equo.logging.client.api;

/**	
 * Provides a protected initialization of new Logger instances
 *
 */
public abstract class AbstractLogger implements Logger {
	@SuppressWarnings("rawtypes")
	protected abstract void init(Class clazz);
}
