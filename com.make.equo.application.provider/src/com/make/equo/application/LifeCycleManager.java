package com.make.equo.application;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.equinox.app.IApplicationContext;
import org.osgi.framework.Bundle;

import com.make.equo.application.api.IEquoApplication;
import com.make.equo.application.model.EquoApplicationBuilder;
import com.make.equo.application.model.EquoApplicationBuilderConfigurator;
import com.make.equo.application.util.FrameworkUtil;

public class LifeCycleManager {

	@ProcessAdditions
	void postContextCreate(IApplicationContext applicationContext, MApplication mainApplication,
			IEquoApplication equoApp, EquoApplicationBuilder equoApplicationBuilder,
			EquoApplicationModel equoApplicationModel)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		// String[] appArgs = (String[])
		// applicationContext.getArguments().get(IApplicationContext.APPLICATION_ARGS);
		// String appBundleName = getArgValue(appArgs, equoAppBundleName).get();
		// String equoAppClassName = getArgValue(appArgs, equoAppMainClass).get();

		// Bundle equoMainAppBundle = Platform.getBundle(appBundleName);

		Bundle equoMainAppBundle = org.osgi.framework.FrameworkUtil.getBundle(equoApp.getClass());

		FrameworkUtil.INSTANCE.setMainEquoAppBundle(equoMainAppBundle);

		// Class<?> equoApplicationClazz =
		// equoMainAppBundle.loadClass(equoAppClassName);
		// IEquoFramework equoApp = (IEquoFramework) equoApplicationClazz.newInstance();
		equoApplicationModel.setMainApplication(mainApplication);
		EquoApplicationBuilderConfigurator equoApplicationBuilderConfigurator = new EquoApplicationBuilderConfigurator(
				equoApplicationModel, equoApplicationBuilder);
		equoApplicationBuilderConfigurator.configure();
		equoApp.buildApp(equoApplicationBuilder);
	}
}
