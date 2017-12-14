package com.make.equo.ws.provider;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Map;

import org.java_websocket.WebSocket;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.google.gson.Gson;
import com.make.equo.ws.api.IEquoRunnable;
import com.make.equo.ws.api.NamedActionMessage;

class EquoWebSocketServer extends WebSocketServer {

	private Gson gsonParser;
	private Map<String, IEquoRunnable> eventHandlers;

	public EquoWebSocketServer(Map<String, IEquoRunnable> eventHandlers) {
		super(new InetSocketAddress(9895));
		this.eventHandlers = eventHandlers;
		this.gsonParser = new Gson();
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		broadcast("new connection: " + handshake.getResourceDescriptor());
		System.out
				.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the Equo Framework!");
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		broadcast(conn + " has left the Equo Framework!");
		System.out.println(conn + " has left the Equo Framework!");
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		// broadcast(message);
		System.out.println(conn + ": " + message);
		NamedActionMessage actionMessage = gsonParser.fromJson(message, NamedActionMessage.class);
		String action = actionMessage.getAction().toLowerCase();
		if (eventHandlers.containsKey(action)) {
			IEquoRunnable runnable = eventHandlers.get(action);
			runnable.run(message);
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

}
