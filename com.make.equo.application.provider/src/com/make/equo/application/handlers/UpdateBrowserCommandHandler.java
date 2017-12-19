package com.make.equo.application.handlers;

import java.util.Optional;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

import com.google.gson.Gson;
import com.make.equo.application.parts.SinglePagePart;
import com.make.equo.application.util.IConstants;

public class UpdateBrowserCommandHandler implements BrowserCommandHandler {

	@Execute
	public void execute(@Named(IConstants.EQUO_WEBSOCKET_UPDATE_BROWSER) String browserParams,
			MApplication mApplication, EModelService modelService) {
		Gson gsonParser = new Gson();
		BrowserParams params = gsonParser.fromJson(browserParams, BrowserParams.class);
		Optional<MPart> existingBrowser = existingBrowserFor(mApplication, params, modelService);
		if (existingBrowser.isPresent()) {
			MPart mPart = existingBrowser.get();
			SinglePagePart singlePagePart = (SinglePagePart) mPart.getObject();
			singlePagePart.loadUrl(params.getUrl());
		} else {
			// TODO notify the web clients that there is no browser openned for that browser
			// name/id. We can use the onMessage hook.
			System.out.println("No browser exists for the given browser id/name");
		}
	}

}
