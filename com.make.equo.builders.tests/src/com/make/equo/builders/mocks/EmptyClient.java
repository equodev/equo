package com.make.equo.builders.mocks;

import java.net.URI;
import java.nio.ByteBuffer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

public class EmptyClient extends WebSocketClient {

	private String message;
	
	public static EmptyClient create(URI uri) {
		return new EmptyClient(uri);
	}
	
	public static EmptyClient create(URI serverUri, Draft draft) {
		return new EmptyClient(serverUri, draft);
	}
	
	public EmptyClient(URI serverUri, Draft draft) {
		super(serverUri, draft);
	}

	private EmptyClient(URI serverURI) {
		super(serverURI);
	}
	
	
	public String getMessage() {
		return message;
	}

	public EmptyClient singleMessage(String message) {
		this.message = message;
		return this;
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		send(message);
		System.out.println("new connection opened");
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("closed with exit code " + code + " additional info: " + reason);
	}

	@Override
	public void onMessage(String message) {
		System.out.println("received message: " + message);
	}

	@Override
	public void onMessage(ByteBuffer message) {
		System.out.println("received ByteBuffer");
	}

	@Override
	public void onError(Exception ex) {
		System.err.println("an error occurred:" + ex);
	}

}