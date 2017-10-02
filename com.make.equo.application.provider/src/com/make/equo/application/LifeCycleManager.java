package com.make.equo.application;

import java.util.Optional;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.equinox.app.IApplicationContext;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.make.equo.application.api.IEquoFramework;
import com.make.equo.application.model.EquoApplication;

public class LifeCycleManager {

	private static final String EQUO_APP_MAIN_CLASS = "equoAppMainClass";
	private static final String EQUO_APP_BUNDLE_ID = "equoAppBundleId";

	@ProcessAdditions
	void postContextCreate(IApplicationContext applicationContext, MApplication mainApplication)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		String[] appArgs = (String[]) applicationContext.getArguments().get(IApplicationContext.APPLICATION_ARGS);
		Integer equoAppBundleId = Integer.valueOf(getArgValue(appArgs, EQUO_APP_BUNDLE_ID).get());
		String equoAppClassName = getArgValue(appArgs, EQUO_APP_MAIN_CLASS).get();

		BundleContext context = Activator.getContext();
		
		Bundle equoAppBundle = context.getBundle(equoAppBundleId);
		Class<?> equoApplicationClazz = equoAppBundle.loadClass(equoAppClassName);
		IEquoFramework equoApp = (IEquoFramework) equoApplicationClazz.newInstance();
		
		equoApp.buildApp(new EquoApplication(mainApplication));
	}

	private Optional<String> getArgValue(String[] appArgs, String argName) {
		if (argName == null || argName.length() == 0)
			return Optional.empty();

		for (int i = 0; i < appArgs.length; i++) {
			if (("-" + argName).equals(appArgs[i]) && i + 1 < appArgs.length)
				return Optional.of(appArgs[i + 1]);
		}

		return Optional.empty();
	}
}
