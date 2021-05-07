package com.equo.application.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;

import com.equo.application.util.IConstants;

public class OpenBrowserAsWindowCommandHandler {

	@Execute
	public void execute(@Named(IConstants.EQUO_OPEN_BROWSER_AS_WINDOW) String url,
			@Named(IConstants.EQUO_BROWSER_WINDOW_NAME) String windowName, MApplication mApplication,
			UISynchronize sync) {
		sync.syncExec(() -> {
			MTrimmedWindow windowToOpen = MBasicFactory.INSTANCE.createTrimmedWindow();
			if (windowName != null) {
				windowToOpen.setLabel(windowName);
			} else {
				//remove title bar
			}
			mApplication.getChildren().add(windowToOpen);
			
			MPart part = MBasicFactory.INSTANCE.createPart();
			part.setElementId(IConstants.MAIN_PART_ID + "." + windowName != null ? windowName : "popup");
			part.setContributionURI(IConstants.SINGLE_PART_CONTRIBUTION_URI);
			part.getProperties().put(IConstants.MAIN_URL_KEY, url);
			
			windowToOpen.getChildren().add(part);
			
			windowToOpen.setVisible(true);
			windowToOpen.setOnTop(true);
			windowToOpen.setToBeRendered(true);
		});
	}
}
