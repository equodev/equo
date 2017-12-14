package com.make.equo.ws.provider;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import com.google.common.io.Resources;
import com.make.equo.ws.api.IEquoRunnable;
import com.make.equo.ws.api.IEquoWebSocketService;

@Component
public class EquoWebSocketServiceImpl implements IEquoWebSocketService {

	private static final String EQUO_JS_API = "equo.js";

	private Map<String, IEquoRunnable> eventHandlers = new HashMap<>();
	private EquoWebSocketServer equoWebSocketServer;

	@Activate
	public void start() {
		System.out.println("Initializing Equo websocket server...");
		equoWebSocketServer = new EquoWebSocketServer(eventHandlers);
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
	public void send(String payload) {
		equoWebSocketServer.broadcast(payload);
	}

	@Override
	public void addEventHandler(String eventId, IEquoRunnable equoRunnable) {
		eventHandlers.put(eventId.toLowerCase(), equoRunnable);
	}

	@Override
	public URL getEquoWebSocketJavascriptClient() {
		return Resources.getResource(EQUO_JS_API);
	}

}
