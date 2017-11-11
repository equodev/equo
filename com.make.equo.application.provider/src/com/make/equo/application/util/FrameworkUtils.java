package com.make.equo.application.util;

import java.net.URL;

import org.osgi.framework.Bundle;

public enum FrameworkUtils {

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

}
