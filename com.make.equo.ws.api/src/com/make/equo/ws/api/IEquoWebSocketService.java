package com.make.equo.ws.api;

import java.net.URL;

import com.google.gson.GsonBuilder;

public interface IEquoWebSocketService {

	public void addEventHandler(String actionId, IEquoRunnableParser<?> equoRunnableParser);

	public URL getEquoWebsocketsJavascriptClient();

	public String getEquoWebsocketsJsResourceName();

	public void send(String userEvent, Object payload, GsonBuilder gsonBuilder);

}
