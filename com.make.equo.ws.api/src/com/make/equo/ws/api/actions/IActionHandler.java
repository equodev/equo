package com.make.equo.ws.api.actions;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public interface IActionHandler<T> extends IEquoCallable<T>{
	
    default public Class getGenericInterfaceType(){
        Class c = getClass();
        ParameterizedType parameterizedType = (ParameterizedType) c.getGenericInterfaces()[0];
        Type[] typeArguments = parameterizedType.getActualTypeArguments();
        Class<?> typeArgument = (Class<?>) typeArguments[0];
        return typeArgument;
    }
		
	public Object call(T payload);
}
