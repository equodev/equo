package com.make.equo.ws.api;

import java.net.URL;

public interface IEquoWebSocketService {

	public void send(String payload);

	public void addEventHandler(String actionId, IEquoRunnable runnable);

	public URL getEquoWebSocketJavascriptClient();

}
