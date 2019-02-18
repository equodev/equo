package com.make.equo.renderers.contributions;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimBar;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimElement;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.osgi.service.event.Event;

import com.google.common.collect.Lists;

public class ToolBarModelProcessorAddon {

	private static final List<String> toolBarsWhiteList = Lists.newArrayList("PerspectiveSpacer",
			"PerspectiveSwitcher");

	private static final String ECLIPSE_MAIN_TOOLBAR_ID = "org.eclipse.ui.main.toolbar";

	@Inject
	@org.eclipse.e4.core.di.annotations.Optional
	public void applicationStarted(@EventTopic(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE) Event event,
			MApplication mainApplication, UISynchronize sync) {
		hideMainTrimBar(mainApplication, sync);
	}

	private void hideMainTrimBar(MApplication mainApplication, UISynchronize sync) {
		List<MWindow> children = mainApplication.getChildren();
		for (MWindow mWindow : children) {
			MTrimmedWindow mainTrimmedWindow = (MTrimmedWindow) mWindow;
			List<MTrimBar> trimBars = mainTrimmedWindow.getTrimBars();
			Optional<MTrimBar> mainTrimBar = getMainTrimBar(trimBars);
			if (mainTrimBar.isPresent()) {
				MTrimBar mTrimBar = mainTrimBar.get();
				sync.asyncExec(() -> {
					hideTrimBarChildren(mTrimBar);
					mTrimBar.getChildren().clear();
				});
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

}
