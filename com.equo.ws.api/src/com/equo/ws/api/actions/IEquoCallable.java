package com.equo.ws.api.actions;

import com.equo.ws.api.lambda.Newable;

/**
 * Interface for callables with a ws event payload as parameters.
 */
@FunctionalInterface
public interface IEquoCallable<T> extends Newable<T> {

  public Object call(T payload);

}
