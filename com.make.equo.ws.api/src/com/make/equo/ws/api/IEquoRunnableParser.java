package com.make.equo.ws.api;

public interface IEquoRunnableParser<T> {

	public T parsePayload(Object payload);

	public IEquoRunnable<T> getEquoRunnable();

}
