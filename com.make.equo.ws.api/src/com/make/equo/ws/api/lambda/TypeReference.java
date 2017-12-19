package com.make.equo.ws.api.lambda;

import java.util.function.Consumer;

public interface TypeReference<T> extends Newable<T> {
	T typeIs(T t);

	default Consumer<T> consumer() {
		return this::typeIs;
	}
}