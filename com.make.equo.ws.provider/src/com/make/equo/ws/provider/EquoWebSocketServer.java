package com.make.equo.ws.provider;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.google.gson.Gson;
import com.make.equo.ws.api.IEquoRunnable;
import com.make.equo.ws.api.IEquoRunnableParser;
import com.make.equo.ws.api.NamedActionMessage;
import com.make.equo.ws.api.actions.IActionHandler;
import com.make.equo.logging.client.api.Logger;
import com.make.equo.logging.client.api.LoggerFactory;

class EquoWebSocketServer extends WebSocketServer {
	protected static final Logger logger = LoggerFactory.getLogger(EquoWebSocketServer.class);

	private Gson gsonParser;
	private Map<String, IEquoRunnableParser<?>> eventHandlers;
	@SuppressWarnings("rawtypes")
	private Map<String, IActionHandler> actions;
	private boolean firstClientConnected = false;
	List<String> messagesToSend = new ArrayList<>();

	private volatile boolean started;

	public EquoWebSocketServer(Map<String, IEquoRunnableParser<?>> eventHandlers,
			@SuppressWarnings("rawtypes") Map<String, IActionHandler> actionHandlers) {
		super(new InetSocketAddress(0));
		this.actions = actionHandlers;
		this.eventHandlers = eventHandlers;
		this.gsonParser = new Gson();
		this.started = false;
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		broadcast("new connection: " + handshake.getResourceDescriptor());
		logger.debug(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the Equo Framework!");
		this.firstClientConnected = true;
		synchronized (messagesToSend) {
			for (String messageToSend : messagesToSend) {
				broadcast(messageToSend);
			}
			messagesToSend.clear();
		}
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		broadcast(conn + " has left the Equo Framework!");
		logger.debug(conn + " has left the Equo Framework!");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void receiveMessage(String message) {
		NamedActionMessage actionMessage = null;
		try {
			actionMessage = gsonParser.fromJson(message, NamedActionMessage.class);
		} catch (Exception e) {
			return;
		}

		String action = actionMessage.getAction().toLowerCase();
		if (eventHandlers.containsKey(action)) {
			IEquoRunnableParser<?> equoRunnableParser = eventHandlers.get(action);
			Object parsedPayload = equoRunnableParser.parsePayload(actionMessage.getParams());
			IEquoRunnable equoRunnable = equoRunnableParser.getEquoRunnable();
			equoRunnable.run(parsedPayload);
		} else if (actions.containsKey(action)) {
			Object parsedPayload = null;
			if (actionMessage.getParams() != null) {
				Gson gson = new Gson();
				String jsonString = gson.toJson(actionMessage.getParams());
				parsedPayload = gson.fromJson(jsonString, actions.get(action).getGenericInterfaceType());
			}
			actions.get(action).call(parsedPayload);
		}
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		// broadcast(message);
		logger.debug(conn + ": " + message);
		receiveMessage(message);
	}

	@Override
	public void onMessage(WebSocket conn, ByteBuffer message) {
		broadcast(message.array());
		logger.debug(conn + ": " + message);
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
		if (conn != null) {
			// some errors like port binding failed may not be assignable to a specific
			// websocket
		}
	}

	@Override
	public void onStart() {
		// TODO log web socket server started
		this.started = true;
		logger.info("Equo Websocket Server started!");
	}

	@Override
	public void broadcast(String messageAsJson) {
		if (firstClientConnected) {
			super.broadcast(messageAsJson);
			receiveMessage(messageAsJson);
		} else {
			synchronized (messagesToSend) {
				messagesToSend.add(messageAsJson);
			}
		}
	}

	public boolean isStarted() {
		return started;
	}

}
