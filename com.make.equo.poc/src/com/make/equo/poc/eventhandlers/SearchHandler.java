package com.make.equo.poc.eventhandlers;

import com.make.equo.ws.api.actions.IActionHandler;

import org.osgi.service.component.annotations.Component;

import com.make.equo.poc.payloads.SearchPayload;

@SuppressWarnings("serial")
@Component
public class SearchHandler implements IActionHandler<SearchPayload>{

	@Override
	public Object call(SearchPayload payload) {
		System.out.println("EventHandler de Search recibio \"" + payload + "\" como busqueda");
		return null;
	}

}
