package com.make.equo.application.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;

public class OpenBrowserCommandHandler {

	@Execute
	public void execute(@Named("com.make.equo.application.websocket.openBrowser") String urlToOpen,
			MApplication mApplication, UISynchronize sync) {
		// TODO log open URL maybe?
		sync.syncExec(() -> {
			MTrimmedWindow windowToOpen = MBasicFactory.INSTANCE.createTrimmedWindow();
			mApplication.getChildren().add(windowToOpen);
			windowToOpen.setVisible(true);
			windowToOpen.setOnTop(true);
			windowToOpen.setToBeRendered(true);
		});
	}
}
