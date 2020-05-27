package com.make.equo.poc.eventhandlers;

import org.osgi.service.component.annotations.Component;

import com.make.equo.poc.payloads.Payload;
import com.make.equo.ws.api.actions.IActionHandler;

@SuppressWarnings("serial")
@Component
public class DebugHandler implements IActionHandler<Payload> {


	@Override
	public Object call(Payload payload) {
		System.out.println("EventHandler de Debug recibio \"" + payload + "\" como busqueda");
		return null;
	}

}
