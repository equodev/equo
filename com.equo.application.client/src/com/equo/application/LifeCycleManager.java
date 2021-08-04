/****************************************************************************
**
** Copyright (C) 2021 Equo
**
** This file is part of Equo Framework.
**
** Commercial License Usage
** Licensees holding valid commercial Equo licenses may use this file in
** accordance with the commercial license agreement provided with the
** Software or, alternatively, in accordance with the terms contained in
** a written agreement between you and Equo. For licensing terms
** and conditions see https://www.equoplatform.com/terms.
**
** GNU General Public License Usage
** Alternatively, this file may be used under the terms of the GNU
** General Public License version 3 as published by the Free Software
** Foundation. Please review the following
** information to ensure the GNU General Public License requirements will
** be met: https://www.gnu.org/licenses/gpl-3.0.html.
**
****************************************************************************/

package com.equo.application;

import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.equinox.app.IApplicationContext;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.equo.aer.internal.api.IEquoCrashReporter;
import com.equo.application.api.IEquoApplication;
import com.equo.application.handlers.AppStartupCompleteEventHandler;
import com.equo.application.model.EquoApplicationBuilder;
import com.equo.application.model.EquoApplicationBuilderConfigurator;
import com.equo.application.model.EquoApplicationModel;
import com.equo.contribution.api.IEquoContributionManager;

/**
 * Controls the application life cycle.
 */
public class LifeCycleManager {

  @ProcessAdditions
  void postContextCreate(IApplicationContext applicationContext, MApplication mainApplication,
      IEquoApplication equoApp, EquoApplicationBuilder equoApplicationBuilder,
      IDependencyInjector dependencyInjector, EModelService modelService, IEventBroker eventBroker,
      IEquoContributionManager equoContributionManager)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    registerService(mainApplication, MApplication.class);
    IEquoCrashReporter equoCrashReporter = dependencyInjector.getEquoCrashReporter();
    if (equoCrashReporter != null) {
      Platform.addLogListener(new LogListener(equoCrashReporter));
    }
    //    mainApplication.getContext().get(ModelElementInjector.class);
    EquoApplicationModel equoApplicationModel = new EquoApplicationModel();
    equoApplicationModel.setMainApplication(mainApplication);
    EquoApplicationBuilderConfigurator equoApplicationBuilderConfigurator =
        new EquoApplicationBuilderConfigurator(equoApplicationModel, equoApplicationBuilder,
            equoApp);
    equoApplicationBuilderConfigurator.configure();
    AppStartupCompleteEventHandler appStartupHandler = AppStartupCompleteEventHandler.getInstance();
    appStartupHandler.setEquoContributionManager(equoContributionManager);
    eventBroker.subscribe(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE, appStartupHandler);
    equoApp.buildApp(equoApplicationBuilder);
    equoContributionManager.reportPendingContributions();
  }

  void registerService(Object service, Class<?> serviceClass) {
    Bundle bundle = FrameworkUtil.getBundle(serviceClass);
    BundleContext bundleContext = bundle.getBundleContext();
    bundleContext.registerService(serviceClass.getName(), service, null);
  }
}
