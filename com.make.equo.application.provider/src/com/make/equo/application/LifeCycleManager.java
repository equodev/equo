package com.make.equo.application;

import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.equinox.app.IApplicationContext;

import com.make.equo.aer.api.IEquoErrorReporter;
import com.make.equo.application.api.IEquoApplication;
import com.make.equo.application.model.EquoApplicationBuilder;
import com.make.equo.application.model.EquoApplicationBuilderConfigurator;

public class LifeCycleManager {

	@ProcessAdditions
	void postContextCreate(IApplicationContext applicationContext, MApplication mainApplication,
			IEquoApplication equoApp, EquoApplicationBuilder equoApplicationBuilder, IEquoErrorReporter equoErrorReporter)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		String eclipseLogging = System.getProperty("eclipseLogging");
		if (eclipseLogging != null && eclipseLogging.equals("true")) {
			Platform.addLogListener(new LogListener(equoErrorReporter));
		}
		EquoApplicationModel equoApplicationModel = new EquoApplicationModel();
		equoApplicationModel.setMainApplication(mainApplication);
		EquoApplicationBuilderConfigurator equoApplicationBuilderConfigurator = new EquoApplicationBuilderConfigurator(
				equoApplicationModel, equoApplicationBuilder);
		equoApplicationBuilderConfigurator.configure();
		equoApp.buildApp(equoApplicationBuilder);
	}
	
	
}
