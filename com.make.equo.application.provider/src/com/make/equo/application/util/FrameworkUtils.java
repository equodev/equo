package com.make.equo.application.util;

import java.net.URL;

import org.osgi.framework.Bundle;

public enum FrameworkUtils {

	INSTANCE;

	private Bundle mainEquoAppBundle;

	public void setMainAppBundle(Bundle mainEquoAppBundle) {
		this.mainEquoAppBundle = mainEquoAppBundle;
	}

	public URL getEntry(String scriptPath) {
		URL scriptUrl = mainEquoAppBundle.getEntry(scriptPath);
		return scriptUrl;
	}
	
	public String getMainBundleLocation() {
		return mainEquoAppBundle.getLocation();
	}

}
