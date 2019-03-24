package com.make.equo.server.api;

import java.util.Optional;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.make.equo.server.api.IEquoServer;

/**
 * Useful to get and use the Equo Server from classes which are not OSGI
 * components.
 *
 */
public class EquoServerProvider {

	private IEquoServer equoServer;

	public EquoServerProvider() {
		BundleContext ctx = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		if (ctx != null) {
			@SuppressWarnings("unchecked")
			ServiceReference<IEquoServer> serviceReference = (ServiceReference<IEquoServer>) ctx
					.getServiceReference(IEquoServer.class.getName());
			if (serviceReference != null) {
				equoServer = ctx.getService(serviceReference);
			}
		}
	}

	public Optional<IEquoServer> getEquoServer() {
		return Optional.ofNullable(equoServer);
	}
}
