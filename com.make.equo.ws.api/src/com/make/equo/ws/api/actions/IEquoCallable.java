package com.make.equo.ws.api.actions;

import com.make.equo.ws.api.lambda.Newable;

@FunctionalInterface
public interface IEquoCallable<T> extends Newable<T> {

	public Object call(T payload);

}
