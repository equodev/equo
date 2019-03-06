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
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarElement;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.osgi.service.event.Event;

public class ToolBarModelProcessorAddon {

	private static final String ECLIPSE_MAIN_TOOLBAR_ID = "org.eclipse.ui.main.toolbar";
	private static final String EQUO_MAIN_TOOLBAR = "com.make.equo.main.toolbar";

	private EModelService modelService;

	@Inject
	@org.eclipse.e4.core.di.annotations.Optional
	public void applicationStarted(@EventTopic(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE) Event event,
			MApplication mainApplication, UISynchronize sync, EModelService modelService) {
		this.modelService = modelService;
		hideOldToolBarAndCreateEquoToolBar(mainApplication, sync);
	}

	private void hideOldToolBarAndCreateEquoToolBar(MApplication mainApplication, UISynchronize sync) {
		List<MWindow> children = mainApplication.getChildren();
		for (MWindow mWindow : children) {
			MTrimmedWindow mainTrimmedWindow = (MTrimmedWindow) mWindow;
			List<MTrimBar> trimBars = mainTrimmedWindow.getTrimBars();
			Optional<MTrimBar> mainTrimBar = getMainTrimBar(trimBars);
			if (mainTrimBar.isPresent()) {
				MTrimBar mTrimBar = mainTrimBar.get();
				sync.asyncExec(() -> {
//					hideTrimBarChildren(mTrimBar);
					MToolBar equoToolbar = createEquoToolbar(mTrimBar);
					mTrimBar.getChildren().clear();
					mTrimBar.setToBeRendered(false);
					mTrimBar.getChildren().add(equoToolbar);
					mTrimBar.setToBeRendered(true);
				});
			}
		}
	}

	public MToolBar createEquoToolbar(MTrimBar mTrimBar) {
		List<MTrimElement> mTrimElements = mTrimBar.getChildren();

		MToolBar createdToolbar = modelService.createModelElement(MToolBar.class);
		createdToolbar.setElementId(EQUO_MAIN_TOOLBAR);

		for (MTrimElement mTrimElement : mTrimElements) {
			if (mTrimElement instanceof MToolBar && !EQUO_MAIN_TOOLBAR.equals(((MToolBar) mTrimElement).getElementId())) {
				MToolBar mToolbar = (MToolBar) mTrimElement;
				List<MToolBarElement> toolBarElements = mToolbar.getChildren();

				createdToolbar.getChildren().addAll(toolBarElements);
			}
		}
		return createdToolbar;
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
