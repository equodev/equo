package com.make.equo.logging.client.api;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

public class LoggerFactory {
	private static boolean loaded = false;

	static <T extends Object> T getService(final Class<T> clazz){
		final BundleContext bundleContext = FrameworkUtil.getBundle(clazz).getBundleContext();
		if (!loaded) {
			String bundleName = System.getProperty("logger.bundle", "com.make.equo.logging.client.core.provider");
			for (Bundle bundle : bundleContext.getBundles()) {
				if (bundle.getSymbolicName().startsWith(bundleName)) {
					try {
						bundle.start();
					} catch (BundleException e) {
						e.printStackTrace();
					}
					break;
				}
			}
			loaded = true;
		}

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
