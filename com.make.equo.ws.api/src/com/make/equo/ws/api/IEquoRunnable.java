package com.make.equo.ws.api;

import com.make.equo.ws.api.lambda.Newable;

public interface IEquoRunnable<T> extends Newable<T> {

	public void run(T payload);
}
