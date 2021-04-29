package com.make.equo.ws.provider;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.make.equo.ws.api.IEquoEventHandler;
import com.make.equo.ws.api.IEquoRunnable;
import com.make.equo.ws.api.IEquoWebSocketService;
import com.make.equo.ws.api.JsonPayloadEquoRunnable;
import com.make.equo.ws.api.JsonRunnableParser;
import com.make.equo.ws.api.ObjectPayloadParser;
import com.make.equo.ws.api.StringPayloadEquoRunnable;
import com.make.equo.ws.api.StringPayloadParser;

/**
 * 
 * Implements the handler actions for send and received websocket events using equoWebSocketService instance.
 *
 */
@Component
public class EquoEventHandler implements IEquoEventHandler {

	private IEquoWebSocketService equoWebSocketService;

	public void send(String userEvent) {
		this.send(userEvent, null);
	}

	public void send(String userEvent, Object payload) {
		equoWebSocketService.send(userEvent, payload);
	}

	public void on(String eventId, JsonPayloadEquoRunnable jsonPayloadEquoRunnable) {
		equoWebSocketService.addEventHandler(eventId, new JsonRunnableParser(jsonPayloadEquoRunnable));
	}

	public void on(String eventId, StringPayloadEquoRunnable stringPayloadEquoRunnable) {
		equoWebSocketService.addEventHandler(eventId, new StringPayloadParser(stringPayloadEquoRunnable));
	}

	public <T> void on(String eventId, IEquoRunnable<T> objectPayloadEquoRunnable) {
		equoWebSocketService.addEventHandler(eventId, new ObjectPayloadParser<T>(objectPayloadEquoRunnable));
	}

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
	void setWebsocketService(IEquoWebSocketService equoWebSocketService) {
		this.equoWebSocketService = equoWebSocketService;
	}

	void unsetWebsocketService(IEquoWebSocketService equoWebSocketService) {
		this.equoWebSocketService = null;
	}
}
