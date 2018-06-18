package com.make.equo.application.client.api;

import java.io.File;

import org.eclipse.core.runtime.adaptor.EclipseStarter;
import org.eclipse.e4.ui.workbench.IWorkbench;

import com.make.equo.application.api.IEquoApplication;
import com.make.equo.application.client.EquoBundleManager;

public class Equo {

	public static void start(Class<?> equoApplicationClazz) {
		if (!IEquoApplication.class.isAssignableFrom(equoApplicationClazz)) {
			throw new IllegalArgumentException("The " + equoApplicationClazz.getName() + " is not an Equo Application");
		}

		EquoBundleManager equoBundleManager = EquoBundleManager.INSTANCE;
		File appBundleFile = equoBundleManager.convertAppToBundle(equoApplicationClazz);
		equoBundleManager.initializeBundleProperties(appBundleFile);

		System.setProperty("com.apple.mrj.application.apple.menu.about.name", equoApplicationClazz.getName());

		String[] args = { "-appName", "com.make.equo.application", "-application",
				"org.eclipse.e4.ui.workbench.swt.E4Application", "-" + IWorkbench.XMI_URI_ARG,
				"com.make.equo.application.provider/Application.e4xmi", "-clearPersistedState",
				"-" + IWorkbench.LIFE_CYCLE_URI_ARG,
				"bundleclass://com.make.equo.application.provider/com.make.equo.application.LifeCycleManager",
				"-XstartOnFirstThread",
				"-equoAppBundleName", equoBundleManager.getMainAppBundleName(),
				"-equoAppMainClass", equoApplicationClazz.getName() };
		try {
			EclipseStarter.run(args, null);
		} catch (Exception e) {
			System.out.println("Failed to start Equo Application...");
			e.printStackTrace();
		}
	}
}
