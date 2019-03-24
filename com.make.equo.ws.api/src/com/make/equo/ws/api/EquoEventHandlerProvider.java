package com.make.equo.ws.api;

import java.util.Optional;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

public class EquoEventHandlerProvider {

	private IEquoEventHandler equoEventHandler;

	public EquoEventHandlerProvider() {
		BundleContext ctx = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		if (ctx != null) {
			@SuppressWarnings("unchecked")
			ServiceReference<IEquoEventHandler> serviceReference = (ServiceReference<IEquoEventHandler>) ctx
					.getServiceReference(IEquoEventHandler.class.getName());
			if (serviceReference != null) {
				equoEventHandler = ctx.getService(serviceReference);
			}
		}
	}

	public Optional<IEquoEventHandler> getEquoEventHandler() {
		return Optional.ofNullable(equoEventHandler);
	}

}
