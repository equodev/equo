package com.make.equo.application;

import java.util.List;
import java.util.Optional;

import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimBar;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimElement;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessRemovals;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.equinox.app.IApplicationContext;

import com.google.common.collect.Lists;
import com.make.equo.aer.internal.api.IEquoCrashReporter;
import com.make.equo.application.api.IEquoApplication;
import com.make.equo.application.model.EquoApplicationBuilder;
import com.make.equo.application.model.EquoApplicationBuilderConfigurator;

public class LifeCycleManager {

	private static final String ECLIPSE_MAIN_TOOLBAR_ID = "org.eclipse.ui.main.toolbar";
	private static final String ECLIPSE_RCP_APP_ID = "org.eclipse.ui.ide.workbench";

	private static final List<String> toolBarsWhiteList = Lists.newArrayList("PerspectiveSpacer",
			"PerspectiveSwitcher");

	@ProcessAdditions
	void postContextCreate(IApplicationContext applicationContext, MApplication mainApplication,
			IEquoApplication equoApp, EquoApplicationBuilder equoApplicationBuilder,
			IEquoCrashReporter equoCrashReporter)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (!isAnEclipseBasedApp()) {
			Platform.addLogListener(new LogListener(equoCrashReporter));
			EquoApplicationModel equoApplicationModel = new EquoApplicationModel();
			equoApplicationModel.setMainApplication(mainApplication);
			EquoApplicationBuilderConfigurator equoApplicationBuilderConfigurator = new EquoApplicationBuilderConfigurator(
					equoApplicationModel, equoApplicationBuilder);
			equoApplicationBuilderConfigurator.configure();
			equoApp.buildApp(equoApplicationBuilder);
		}
	}

	@ProcessRemovals
	void processRemovals(MApplication mainApplication, EModelService modelService) {
		if (isAnEclipseBasedApp()) {
			List<MWindow> children = mainApplication.getChildren();
			for (MWindow mWindow : children) {
				MTrimmedWindow mainTrimmedWindow = (MTrimmedWindow) mWindow;
				List<MTrimBar> trimBars = mainTrimmedWindow.getTrimBars();
				Optional<MTrimBar> mainTrimBar = getMainTrimBar(trimBars);
				if (mainTrimBar.isPresent()) {
					MTrimBar mTrimBar = mainTrimBar.get();
					mTrimBar.setToBeRendered(false);
					mTrimBar.setVisible(false);

					hideTrimBarChildren(mTrimBar);
				}
			}
		}
	}

	private void hideTrimBarChildren(MTrimBar mTrimBar) {
		List<MTrimElement> trimmedElements = mTrimBar.getChildren();

		for (MTrimElement trimmedElement : trimmedElements) {
			if (!toolBarsWhiteList.contains(trimmedElement.getElementId())) {
				trimmedElement.setToBeRendered(false);
				trimmedElement.setVisible(false);
			}
		}
	}

	private Optional<MTrimBar> getMainTrimBar(List<MTrimBar> trimBars) {
		for (MTrimBar trimBar : trimBars) {
			if (ECLIPSE_MAIN_TOOLBAR_ID.equals(trimBar.getElementId())) {
				return Optional.of(trimBar);
			}
		}
		return Optional.empty();
	}

	private boolean isAnEclipseBasedApp() {
		return ECLIPSE_RCP_APP_ID.equals(System.getProperty("eclipse.application"));
	}
}
