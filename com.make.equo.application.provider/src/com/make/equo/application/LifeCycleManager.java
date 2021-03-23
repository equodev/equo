package com.make.equo.application;

import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.equinox.app.IApplicationContext;

import com.make.equo.aer.internal.api.IEquoCrashReporter;
import com.make.equo.application.api.IEquoApplication;
import com.make.equo.application.handlers.AppStartupCompleteEventHandler;
import com.make.equo.application.model.EquoApplicationBuilder;
import com.make.equo.application.model.EquoApplicationBuilderConfigurator;
import com.make.equo.application.model.EquoApplicationModel;
import com.make.equo.contribution.api.IEquoContributionManager;

public class LifeCycleManager {

	@ProcessAdditions
	void postContextCreate(IApplicationContext applicationContext, MApplication mainApplication,
			IEquoApplication equoApp, EquoApplicationBuilder equoApplicationBuilder,
			IDependencyInjector dependencyInjector, EModelService modelService, IEventBroker eventBroker,
			IEquoContributionManager equoContributionManager)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		IEquoCrashReporter equoCrashReporter = dependencyInjector.getEquoCrashReporter();
		if (equoCrashReporter != null) {
			Platform.addLogListener(new LogListener(equoCrashReporter));
		}
//		mainApplication.getContext().get(ModelElementInjector.class);
		EquoApplicationModel equoApplicationModel = new EquoApplicationModel();
		equoApplicationModel.setMainApplication(mainApplication);
		EquoApplicationBuilderConfigurator equoApplicationBuilderConfigurator = new EquoApplicationBuilderConfigurator(
				equoApplicationModel, equoApplicationBuilder, equoApp);
		equoApplicationBuilderConfigurator.configure();
		AppStartupCompleteEventHandler appStartupHandler = AppStartupCompleteEventHandler.getInstance();
		appStartupHandler.setEquoContributionManager(equoContributionManager);
		eventBroker.subscribe(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE, appStartupHandler);
		equoApp.buildApp(equoApplicationBuilder);

	}
}
