package com.make.equo.application.util;

import java.net.URL;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public enum FrameworkUtil {

	INSTANCE;

	private Bundle mainEquoAppBundle;
	private String appBundlePath;

	public void setMainAppBundle(Bundle mainEquoAppBundle) {
		this.mainEquoAppBundle = mainEquoAppBundle;
	}

	public URL getEntry(String scriptPath) {
		URL scriptUrl = mainEquoAppBundle.getEntry(scriptPath);
		return scriptUrl;
	}

	public String getFrameworkName() {
		return IConstants.FRAMEWORK_NAME;
	}

	public void setAppBundlePath(String appBundlePath) {
		this.appBundlePath = appBundlePath;
	}

	public String getAppBundlePath() {
		return appBundlePath;
	}

	public void inject(Object object) {
		BundleContext bundleContext = org.osgi.framework.FrameworkUtil.getBundle(FrameworkUtil.class)
				.getBundleContext();
		IEclipseContext serviceContext = EclipseContextFactory.getServiceContext(bundleContext);
		ContextInjectionFactory.inject(object, serviceContext);
	}

}
