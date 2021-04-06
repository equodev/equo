package com.make.equo.logging.client.api;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

public class LoggerFactory {
	static <T extends Object> T getService(final Class<T> clazz) {
	    final BundleContext bundleContext = FrameworkUtil.getBundle(clazz).getBundleContext();
	    // OSGI uses the order of registration if multiple services are found
	    final ServiceReference<T> ref = bundleContext.getServiceReference(clazz);
	    return bundleContext.getServiceObjects(ref).getService();
	  }
	
	@SuppressWarnings("rawtypes")
	public static Logger getLogger(final Class clazz) {
		Logger logger = getService(Logger.class);
		if (logger instanceof AbstractLogger) {
			((AbstractLogger) logger).init(clazz);
		}
		return logger;
	}
}
