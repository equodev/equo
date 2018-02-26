package com.make.equo.application.util;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public enum FrameworkUtil {

	INSTANCE;

	private Bundle mainEquoAppBundle;

	public void inject(Object object) {
		BundleContext bundleContext = org.osgi.framework.FrameworkUtil.getBundle(FrameworkUtil.class)
				.getBundleContext();
		IEclipseContext serviceContext = EclipseContextFactory.getServiceContext(bundleContext);
		ContextInjectionFactory.inject(object, serviceContext);
	}

	public Bundle getMainEquoAppBundle() {
		return mainEquoAppBundle;
	}

	public void setMainEquoAppBundle(Bundle mainEquoAppBundle) {
		this.mainEquoAppBundle = mainEquoAppBundle;
	}

}
