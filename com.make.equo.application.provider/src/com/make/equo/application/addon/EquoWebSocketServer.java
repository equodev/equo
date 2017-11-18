package com.make.equo.application.addon;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.java_websocket.WebSocket;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import com.make.equo.application.util.IConstants;

public class EquoWebSocketServer extends WebSocketServer {

	private ECommandService commandService;
	private EHandlerService handlerService;

	private EquoWebSocketServer(ECommandService commandService, EHandlerService handlerService) {
		super(new InetSocketAddress(9895));
		this.commandService = commandService;
		this.handlerService = handlerService;
	}

	public static EquoWebSocketServer startServer(ECommandService commandService, EHandlerService handlerService) {
		EquoWebSocketServer equoWebSocketServer = new EquoWebSocketServer(commandService, handlerService);
		System.out.println("Starting Equo WebSocket server...");
		equoWebSocketServer.start();
		return equoWebSocketServer;
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		broadcast("new connection: " + handshake.getResourceDescriptor());
		System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!");
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		broadcast(conn + " has left the room!");
		System.out.println(conn + " has left the room!");
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		broadcast(message);
		System.out.println(conn + ": " + message);
		
		//TODO parse the JSON object with gson.
		try {
			executeCommand(message);
		} catch (NotDefinedException | ExecutionException | NotEnabledException | NotHandledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void executeCommand(String message) throws NotDefinedException, ExecutionException, NotEnabledException, NotHandledException {
		//TODO get the command based on the received message
		System.out.println("The message from the websocket client is " + message);
		Command command = commandService.getCommand(IConstants.EQUO_WEBSOCKET_OPEN_BROWSER + IConstants.COMMAND_SUFFIX);
		Parameterization[] params = new Parameterization[] {
				new Parameterization(command.getParameter(IConstants.EQUO_WEBSOCKET_OPEN_BROWSER), "https://www.imdb.com") };
		ParameterizedCommand parametrizedCommand = new ParameterizedCommand(command, params);
		handlerService.executeHandler(parametrizedCommand);
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
		//TODO log web socket server started
		System.out.println("Server started!");
	}

}
