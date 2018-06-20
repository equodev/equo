package com.make.equo.ws.api;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.google.gson.GsonBuilder;

public class EquoEventHandler {

	private IEquoWebSocketService equoWebSocketService;
	private GsonBuilder gsonBuilder;

	// TODO improve how the injection is made here, use proper OSGI injection
	public EquoEventHandler() {
		BundleContext ctx = FrameworkUtil.getBundle(EquoEventHandler.class).getBundleContext();
		if (ctx != null) {
			@SuppressWarnings("unchecked")
			ServiceReference<IEquoWebSocketService> serviceReference = (ServiceReference<IEquoWebSocketService>) ctx
					.getServiceReference(IEquoWebSocketService.class.getName());
			if (serviceReference != null) {
				equoWebSocketService = ctx.getService(serviceReference);
			}
		}
		this.gsonBuilder = new GsonBuilder();
	}

	public void send(String userEvent) {
		this.send(userEvent, null);
	}

	public void send(String userEvent, Object payload) {
		equoWebSocketService.send(userEvent, payload, this.gsonBuilder);
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

	public GsonBuilder getGsonBuilder() {
		return this.gsonBuilder;
	}

}
