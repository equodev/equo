package com.make.equo.application.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;

import com.google.gson.Gson;
import com.make.equo.application.util.IConstants;

public class OpenBrowserCommandHandler {

	@Execute
	public void execute(@Named(IConstants.EQUO_WEBSOCKET_OPEN_BROWSER) String browserParams,
			MApplication mApplication, UISynchronize sync) {
		System.out.println("browser params are " + browserParams);
		Gson gsonParser = new Gson();
		BrowserActionMessage broserParamsObject = gsonParser.fromJson(browserParams, BrowserActionMessage.class);
		System.out.println("browser params object is " + broserParamsObject.getParams());
		sync.syncExec(() -> {
			MTrimmedWindow windowToOpen = MBasicFactory.INSTANCE.createTrimmedWindow();
			mApplication.getChildren().add(windowToOpen);
			windowToOpen.setVisible(true);
			windowToOpen.setOnTop(true);
			windowToOpen.setToBeRendered(true);
		});
	}
}
