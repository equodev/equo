package com.make.equo.ws.api.lambda;

public interface Newable<T> extends MethodFinder {
	@SuppressWarnings("unchecked")
	default Class<T> type() {
		return (Class<T>) parameter(0).getType();
	}

	default T newInstance() {
		try {
			return type().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}