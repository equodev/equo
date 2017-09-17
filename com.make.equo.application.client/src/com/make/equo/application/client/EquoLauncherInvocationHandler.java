package com.make.equo.application.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class EquoLauncherInvocationHandler implements InvocationHandler{

	private Object object;

	public EquoLauncherInvocationHandler(Object object) {
		this.object = object;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return method.invoke(object, args);
	}

}
