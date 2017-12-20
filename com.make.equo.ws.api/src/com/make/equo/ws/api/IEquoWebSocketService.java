package com.make.equo.ws.api;

import java.net.URL;

public interface IEquoWebSocketService {

	public void addEventHandler(String actionId, IEquoRunnableParser<?> equoRunnableParser);

	public URL getEquoWebSocketJavascriptClient();

	public void send(String userEvent, Object payload);

}
