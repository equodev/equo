package com.make.equo.ws.api;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component(service = EquoEventHandler.class)
public class EquoEventHandler {

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
	void setViewBuilder(IEquoWebSocketService equoWebSocketService) {
		this.equoWebSocketService = equoWebSocketService;
	}

	void unsetViewBuilder(IEquoWebSocketService equoWebSocketService) {
		this.equoWebSocketService = null;
	}
}
