package com.make.equo.ws.api;

import com.google.gson.GsonBuilder;

public interface IEquoWebSocketService {

	public void addEventHandler(String actionId, IEquoRunnableParser<?> equoRunnableParser);

	public void send(String userEvent, Object payload, GsonBuilder gsonBuilder);

	public int getPort();

}
