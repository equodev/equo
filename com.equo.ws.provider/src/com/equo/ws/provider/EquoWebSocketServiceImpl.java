package com.equo.ws.provider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

import com.google.gson.GsonBuilder;
import com.equo.logging.client.api.Logger;
import com.equo.logging.client.api.LoggerFactory;
import com.equo.ws.api.IEquoRunnableParser;
import com.equo.ws.api.IEquoWebSocketService;
import com.equo.ws.api.NamedActionMessage;
import com.equo.ws.api.actions.IActionHandler;

@Component
public class EquoWebSocketServiceImpl implements IEquoWebSocketService {
	protected static final Logger logger = LoggerFactory.getLogger(EquoWebSocketServiceImpl.class);

	@SuppressWarnings("rawtypes")
	private Map<String, IActionHandler> actionHandlers = new HashMap<>();
	private Map<String, IEquoRunnableParser<?>> eventHandlers = new HashMap<>();
	private EquoWebSocketServer equoWebSocketServer;
	
	@Activate
	public void start() {
		logger.info("Initializing Equo websocket server...");
		equoWebSocketServer = new EquoWebSocketServer(eventHandlers, actionHandlers);
		equoWebSocketServer.start();
	}

	@Deactivate
	public void stop() {
		logger.info("Stopping Equo websocket server... ");
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC, policyOption = ReferencePolicyOption.GREEDY)
	public void setActionHandler(IActionHandler actionHandler) {
		this.actionHandlers.put(actionHandler.getEventName(), actionHandler);
		Map<String, IActionHandler> events = actionHandler.getExtraEvents();
		for (Map.Entry<String, IActionHandler> extraEvent : events.entrySet()) {
			this.actionHandlers.put(extraEvent.getKey(), extraEvent.getValue());
		}
	}

	public void unsetActionHandler(@SuppressWarnings("rawtypes") IActionHandler actionHandler) {
		this.actionHandlers.clear();
	}
	

}
