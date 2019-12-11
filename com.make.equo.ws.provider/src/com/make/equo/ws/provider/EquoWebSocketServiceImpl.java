package com.make.equo.ws.provider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import com.google.gson.GsonBuilder;
import com.make.equo.ws.api.IEquoRunnableParser;
import com.make.equo.ws.api.IEquoWebSocketService;
import com.make.equo.ws.api.NamedActionMessage;
import com.make.equo.ws.api.actions.IActionHandler;

@Component
public class EquoWebSocketServiceImpl implements IEquoWebSocketService {

	private Map<String,IActionHandler> actionHandlers = new HashMap<>();
	private Map<String, IEquoRunnableParser<?>> eventHandlers = new HashMap<>();
	private EquoWebSocketServer equoWebSocketServer;

	@Activate
	public void start() {
		System.out.println("Initializing Equo websocket server...");
		equoWebSocketServer = new EquoWebSocketServer(eventHandlers,actionHandlers);
		equoWebSocketServer.start();
	}

	@Deactivate
	public void stop() {
		System.out.println("Stopping Equo websocket server... ");
		eventHandlers.clear();
		try {
			equoWebSocketServer.stop();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void addEventHandler(String eventId, IEquoRunnableParser<?> equoRunnableParser) {
		eventHandlers.put(eventId.toLowerCase(), equoRunnableParser);
	}

	@Override
	public void send(String userEvent, Object payload) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		NamedActionMessage namedActionMessage = new NamedActionMessage(userEvent, payload);
		String messageAsJson = gsonBuilder.create().toJson(namedActionMessage);
		equoWebSocketServer.broadcast(messageAsJson);
	}

	@Override
	public int getPort() {
		// TODO implement a timeout.
		while (!equoWebSocketServer.isStarted())
			;
		return equoWebSocketServer.getPort();
	}

	public EquoWebSocketServer getEquoWebSocketServer() {
		return equoWebSocketServer;
	}

	@Override
	public void addActionHandler(String actionId, IActionHandler handler) {
		actionHandlers.put(actionId, handler);
	}
	
	
	
}
