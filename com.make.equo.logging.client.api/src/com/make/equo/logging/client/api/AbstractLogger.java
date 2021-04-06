package com.make.equo.logging.client.api;

public abstract class AbstractLogger implements Logger {
	@SuppressWarnings("rawtypes")
	protected abstract void init(Class clazz);
}
