package com.make.equo.application;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.equinox.app.IApplicationContext;
import org.osgi.framework.Bundle;

import com.make.equo.application.api.IEquoFramework;
import com.make.equo.application.model.EquoApplication;
import com.make.equo.application.util.FrameworkUtil;

public class LifeCycleManager {

//	private static final String equoAppMainClass = "equoAppMainClass";
//	private static final String equoAppBundleName = "equoAppBundleName";

	@ProcessAdditions
	void postContextCreate(IApplicationContext applicationContext, MApplication mainApplication, IEquoFramework equoApp)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
//		String[] appArgs = (String[]) applicationContext.getArguments().get(IApplicationContext.APPLICATION_ARGS);
//		String appBundleName = getArgValue(appArgs, equoAppBundleName).get();
//		String equoAppClassName = getArgValue(appArgs, equoAppMainClass).get();

//		Bundle equoMainAppBundle = Platform.getBundle(appBundleName);
		
		Bundle equoMainAppBundle = org.osgi.framework.FrameworkUtil.getBundle(equoApp.getClass());

		FrameworkUtil.INSTANCE.setMainEquoAppBundle(equoMainAppBundle);

//		Class<?> equoApplicationClazz = equoMainAppBundle.loadClass(equoAppClassName);
//		IEquoFramework equoApp = (IEquoFramework) equoApplicationClazz.newInstance();

		equoApp.buildApp(new EquoApplication(new EquoApplicationModel(mainApplication)));
	}

//	protected Optional<String> getArgValue(String[] appArgs, String argName) {
//		if (argName == null || argName.length() == 0)
//			return Optional.empty();
//
//		for (int i = 0; i < appArgs.length; i++) {
//			if (("-" + argName).equals(appArgs[i]) && i + 1 < appArgs.length)
//				return Optional.of(appArgs[i + 1]);
//		}
//
//		return Optional.empty();
//	}
}
