package com.make.equo.ws.api.actions;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.make.equo.ws.api.annotations.EquoHandler;

public interface IActionHandler<T> extends IEquoCallable<T>{

    @SuppressWarnings("rawtypes")
	default public Class getGenericInterfaceType(){
        Class c = getClass();
        ParameterizedType parameterizedType = (ParameterizedType) c.getGenericInterfaces()[0];
		Type[] typeArguments = parameterizedType.getActualTypeArguments();
		Class<?> typeArgument = (Class<?>) typeArguments[0];
		return typeArgument;
	}

	default public String getEventName() {
		Class<?> clazz = getClass();
		EquoHandler equoHandler = clazz.getAnnotation(EquoHandler.class);
		if (equoHandler != null) {
			String eventName = equoHandler.value();
			if (eventName != null && !eventName.isEmpty()) {
				return eventName;
			}
		}
		return clazz.getSimpleName().toLowerCase();
	}

	default public Map<String, IActionHandler<T>> getExtraEvents() {
		IActionHandler<T> original = this;
		Map<String, IActionHandler<T>> result = new HashMap<>();
		Class<?> clazz = getClass();
		for (Method method: clazz.getDeclaredMethods()) {
			EquoHandler equoHandler = method.getAnnotation(EquoHandler.class);
			if (equoHandler != null) {
				String eventName = equoHandler.value();
				if (eventName != null && !eventName.isEmpty()) {
					result.put(eventName, (payload) -> {
						try {
							return method.invoke(original);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							try {
								return method.invoke(original, payload);
							} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
								e1.printStackTrace();
							}
						}
						return null;
					});
				}
			}
		}
		return result;
	}

	public Object call(T payload);
}
