package com.make.equo.ws.api;

import com.make.equo.ws.api.actions.IActionHandler;

public interface IEquoWebSocketService {
	
	public void addActionHandler(String actionId, IActionHandler handler);

	public void addEventHandler(String actionId, IEquoRunnableParser<?> equoRunnableParser);

	public void send(String userEvent, Object payload);

	public int getPort();

}
