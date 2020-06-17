package com.make.equo.poc.eventhandlers;

import com.make.equo.ws.api.actions.IActionHandler;

import org.osgi.service.component.annotations.Component;

import com.make.equo.poc.payloads.Payload;

@SuppressWarnings("serial")
@Component
public class SearchHandler implements IActionHandler<Payload>{

	@Override
	public Object call(Payload payload) {
		System.out.println("EventHandler de Search recibio \"" + payload + "\" como busqueda");
		return null;
	}

}
