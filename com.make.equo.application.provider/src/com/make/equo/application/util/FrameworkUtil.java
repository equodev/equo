package com.make.equo.application.util;

import org.osgi.framework.Bundle;

public enum FrameworkUtil {

	INSTANCE;

	private Bundle mainEquoAppBundle;

	public Bundle getMainEquoAppBundle() {
		return mainEquoAppBundle;
	}

	public void setMainEquoAppBundle(Bundle mainEquoAppBundle) {
		this.mainEquoAppBundle = mainEquoAppBundle;
	}

}
