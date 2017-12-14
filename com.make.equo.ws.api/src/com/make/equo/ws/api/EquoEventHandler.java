package com.make.equo.ws.api;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

public class EquoEventHandler {

	private IEquoWebSocketService equoWebSocketService;

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
	}

	public void on(String eventId, IEquoRunnable equoRunnable) {
		equoWebSocketService.addEventHandler(eventId, equoRunnable);
	}

	public void send(String userEvent) {
		equoWebSocketService.send(userEvent);
	}

}
