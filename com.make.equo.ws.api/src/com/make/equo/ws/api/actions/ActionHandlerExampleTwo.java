package com.make.equo.ws.api.actions;

import org.osgi.service.component.annotations.Component;

import com.google.gson.JsonObject;

@Component
public class ActionHandlerExampleTwo implements IActionHandler{
	

	@Override
	public Object call(JsonObject payload) {
		System.out.println("Hola, soy el handler 2 activado");
		return null;
	}
}
