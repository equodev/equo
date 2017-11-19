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

import com.google.gson.Gson;
import com.make.equo.application.util.IConstants;

public class EquoWebSocketServer extends WebSocketServer {

	private static final String EXECUTE_ACTION_ID = "execute";
	private ECommandService commandService;
	private EHandlerService handlerService;
	private Gson gsonParser;

	private EquoWebSocketServer(ECommandService commandService, EHandlerService handlerService) {
		super(new InetSocketAddress(9895));
		this.commandService = commandService;
		this.handlerService = handlerService;
		this.gsonParser = new Gson();
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
		System.out.println(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the Equo Framework!");
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		broadcast(conn + " has left the Equo Framework!");
		System.out.println(conn + " has left the Equo Framework!");
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		broadcast(message);
		System.out.println(conn + ": " + message);
		try {
			NamedActionMessage actionMessage = gsonParser.fromJson(message, NamedActionMessage.class);
			if (actionMessage.getAction().equals(EXECUTE_ACTION_ID)) {
				//TODO call user application code with the class passed as param
			} else {
				executeCommand(actionMessage.getAction().toLowerCase(), message);
			}	
		} catch (NotDefinedException | ExecutionException | NotEnabledException | NotHandledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void executeCommand(String action, String message) throws NotDefinedException, ExecutionException, NotEnabledException, NotHandledException {
		String commandParameterId = IConstants.EQUO_WEBSOCKET_PREFIX + "." + action.toLowerCase();
		Command command = commandService.getCommand(commandParameterId + IConstants.COMMAND_SUFFIX);
		Parameterization[] params = new Parameterization[] {
				new Parameterization(command.getParameter(commandParameterId), message) };
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
