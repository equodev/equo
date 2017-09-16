package com.make.equo.application.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

public class CreateAppHandler {
	
	@Execute
	public void execute(final MApplication application, final EModelService ms, final EPartService partService,
			@Named("windowId") final String windowId) {

		// find the window to show
		MUIElement e = ms.find(windowId, application);
		e.setVisible(true);
		e.setOnTop(true);
		e.setToBeRendered(true);
		ms.bringToTop(e);
		
		// hide the others
		for (MWindow window : application.getChildren()) {
			if (!windowId.equals(window.getElementId())) {
				window.setVisible(false);
				window.setOnTop(false);
				window.setToBeRendered(false);
			}
		}
		
	}
}
