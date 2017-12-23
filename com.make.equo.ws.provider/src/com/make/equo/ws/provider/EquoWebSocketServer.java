package com.make.equo.ws.provider;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.java_websocket.WebSocket;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.google.gson.Gson;
import com.make.equo.ws.api.IEquoRunnable;
import com.make.equo.ws.api.IEquoRunnableParser;
import com.make.equo.ws.api.NamedActionMessage;

class EquoWebSocketServer extends WebSocketServer {

	private Gson gsonParser;
	private Map<String, IEquoRunnableParser<?>> eventHandlers;
	private boolean firstClientConnected = false;
	List<String> messagesToSend = new ArrayList<>();

	public EquoWebSocketServer(Map<String, IEquoRunnableParser<?>> eventHandlers) {
		super(new InetSocketAddress(9895));
		this.eventHandlers = eventHandlers;
		this.gsonParser = new Gson();
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		broadcast("new connection: " + handshake.getResourceDescriptor());
		System.out
				.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the Equo Framework!");
		this.firstClientConnected = true;
		for (String messageToSend : messagesToSend) {
			broadcast(messageToSend);
		}
		messagesToSend.clear();
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		broadcast(conn + " has left the Equo Framework!");
		System.out.println(conn + " has left the Equo Framework!");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void onMessage(WebSocket conn, String message) {
		// broadcast(message);
		System.out.println(conn + ": " + message);
		NamedActionMessage actionMessage = gsonParser.fromJson(message, NamedActionMessage.class);
		String action = actionMessage.getAction().toLowerCase();
		if (eventHandlers.containsKey(action)) {
			IEquoRunnableParser<?> equoRunnableParser = eventHandlers.get(action);
			Object parsedPayload = equoRunnableParser.parsePayload(actionMessage.getParams());
			IEquoRunnable equoRunnable = equoRunnableParser.getEquoRunnable();
			equoRunnable.run(parsedPayload);
		}
	}

	@Override
	public void onMessage(WebSocket conn, ByteBuffer message) {
		broadcast(message.array());
		System.out.println(conn + ": " + message);
	}

	@Override
	public void onFragment(WebSocket conn, Framedata fragment) {
		System.out.println("received fragment: " + fragment);
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
		System.out.println("Equo Server started!");
	}

	@Override
	public void broadcast(String messageAsJson) {
		if (firstClientConnected) {
			super.broadcast(messageAsJson);
		} else {
			messagesToSend.add(messageAsJson);
		}
	}

}
