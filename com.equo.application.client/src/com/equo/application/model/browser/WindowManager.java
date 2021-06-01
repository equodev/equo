package com.equo.application.model.browser;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.equo.ws.api.IEquoEventHandler;
import com.google.gson.Gson;

@Component(service = WindowManager.class)
public class WindowManager {

	@Reference
	private IEquoEventHandler eventHandler;

	public void openBrowser(BrowserParams browserParams) {
		eventHandler.send("openBrowser", new Gson().toJsonTree(browserParams).getAsJsonObject());
	}

	public void updateBrowser(BrowserParams browserParams) {
		eventHandler.send("updateBrowser", new Gson().toJsonTree(browserParams).getAsJsonObject());
	}
}
