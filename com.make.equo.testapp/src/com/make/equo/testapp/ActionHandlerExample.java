package com.make.equo.testapp;

import org.osgi.service.component.annotations.Component;

import com.make.equo.ws.api.actions.IActionHandler;

@Component
public class ActionHandlerExample implements IActionHandler<MyObject>{

	@Override
	public Object call(MyObject payload) {
		System.out.println("Action called. "+ payload.toString());
		return null;
	}
	

}
