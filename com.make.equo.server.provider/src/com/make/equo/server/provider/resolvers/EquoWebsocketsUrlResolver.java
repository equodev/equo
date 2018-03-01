package com.make.equo.server.provider.resolvers;

import java.net.URL;

import com.make.equo.ws.api.IEquoWebSocketService;

public class EquoWebsocketsUrlResolver implements ILocalUrlResolver {

	private String equoWebsocketsJsPath;
	private IEquoWebSocketService equoWebsocketServer;

	public EquoWebsocketsUrlResolver(String equoWebsocketsJsPath, IEquoWebSocketService equoWebsocketServer) {
		this.equoWebsocketsJsPath = equoWebsocketsJsPath;
		this.equoWebsocketServer = equoWebsocketServer;
	}

	@Override
	public String getProtocol() {
		return equoWebsocketsJsPath;
	}

	@Override
	public URL resolve(String pathToResolve) {
		String equoWebsocketsJsResourceName = equoWebsocketServer.getEquoWebsocketsJsResourceName();
		URL resource = equoWebsocketServer.getClass().getClassLoader().getResource(equoWebsocketsJsResourceName);
		return resource;
	}

}
