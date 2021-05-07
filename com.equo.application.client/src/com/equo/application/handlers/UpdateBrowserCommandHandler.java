package com.equo.application.handlers;

import java.util.Optional;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

import com.google.gson.Gson;
import com.equo.application.parts.SinglePagePart;
import com.equo.application.util.IConstants;
import com.equo.logging.client.api.Logger;
import com.equo.logging.client.api.LoggerFactory;

public class UpdateBrowserCommandHandler implements BrowserCommandHandler {
	protected static final Logger logger = LoggerFactory.getLogger(UpdateBrowserCommandHandler.class);

	@Execute
	public void execute(@Named(IConstants.EQUO_WEBSOCKET_UPDATE_BROWSER) String browserParams,
			MApplication mApplication, EModelService modelService, UISynchronize sync) {
		Gson gsonParser = new Gson();
		BrowserParams params = gsonParser.fromJson(browserParams, BrowserParams.class);
		Optional<MPart> existingBrowser = existingBrowserFor(mApplication, params, modelService);
		if (existingBrowser.isPresent()) {
			MPart mPart = existingBrowser.get();
			SinglePagePart singlePagePart = (SinglePagePart) mPart.getObject();
			sync.syncExec(() -> {
				singlePagePart.loadUrl(params.getUrl());
			});
		} else {
			// TODO notify the web clients that there is no browser openned for that browser
			// name/id. We can use the onMessage hook.
			logger.debug("No browser exists for the given browser id/name");
		}
	}

}