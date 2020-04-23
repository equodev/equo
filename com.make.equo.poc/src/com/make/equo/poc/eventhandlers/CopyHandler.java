package com.make.equo.poc.eventhandlers;

import org.osgi.service.component.annotations.Component;

import com.make.equo.poc.payloads.SearchPayload;
import com.make.equo.ws.api.actions.IActionHandler;

@SuppressWarnings("serial")
@Component
public class CopyHandler implements IActionHandler<SearchPayload> {


	@Override
	public Object call(SearchPayload payload) {
		System.out.println("EventHandler de Copy recibio \"" + payload + "\" como busqueda");
		return null;
	}

	
}
