package com.equo.ws.api.actions;

import com.equo.ws.api.lambda.Newable;

@FunctionalInterface
public interface IEquoCallable<T> extends Newable<T> {

	public Object call(T payload);

}
