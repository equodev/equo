package com.make.equo.logging.client.api;

public abstract class AbstractLogger implements ILoggingApi {
	@SuppressWarnings("rawtypes")
	protected abstract void init(Class clazz);
}
