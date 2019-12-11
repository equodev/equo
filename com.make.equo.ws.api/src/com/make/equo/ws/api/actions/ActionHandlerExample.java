package com.make.equo.ws.api.actions;

import org.osgi.service.component.annotations.Component;

import com.google.gson.JsonObject;

@Component(
    immediate = true,
    service = EquoActionHandler.class
)
public class ActionHandlerExample extends EquoActionHandler {

	private static final long serialVersionUID = 1L;

	@Override
	public Object call(JsonObject payload) {
		System.out.println("Hola, soy el handler activado");
		return null;
	}

}
