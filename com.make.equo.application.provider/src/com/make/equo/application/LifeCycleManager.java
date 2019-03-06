package com.make.equo.application;

import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.equinox.app.IApplicationContext;

import com.make.equo.aer.internal.api.IEquoCrashReporter;
import com.make.equo.application.api.IEquoApplication;
import com.make.equo.application.model.EquoApplicationBuilder;
import com.make.equo.application.model.EquoApplicationBuilderConfigurator;
import com.make.equo.ui.helper.provider.model.ModelElementInjector;

public class LifeCycleManager {

	@ProcessAdditions
	void postContextCreate(IApplicationContext applicationContext, MApplication mainApplication,
			IEquoApplication equoApp, EquoApplicationBuilder equoApplicationBuilder,
			IEquoCrashReporter equoCrashReporter, EModelService modelService)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Platform.addLogListener(new LogListener(equoCrashReporter));
		mainApplication.getContext().get(ModelElementInjector.class);
		EquoApplicationModel equoApplicationModel = new EquoApplicationModel();
		equoApplicationModel.setMainApplication(mainApplication);
		EquoApplicationBuilderConfigurator equoApplicationBuilderConfigurator = new EquoApplicationBuilderConfigurator(
				equoApplicationModel, equoApplicationBuilder);
		equoApplicationBuilderConfigurator.configure();
		equoApp.buildApp(equoApplicationBuilder);

	}
}
