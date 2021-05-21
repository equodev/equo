package com.equo.application.model.browser;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
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

	/**
	 * 
	 * @return a WindowManager instance or null if there was an error getting the component
	 */
	public static WindowManager getInstance() {
		BundleContext ctx = FrameworkUtil.getBundle(WindowManager.class).getBundleContext();
		if (ctx != null) {
			@SuppressWarnings("unchecked")
			ServiceReference<WindowManager> serviceReference = (ServiceReference<WindowManager>) ctx
					.getServiceReference(WindowManager.class.getName());
			if (serviceReference != null) {
				return ctx.getServiceObjects(serviceReference).getService();
			}
		}
		return null;
	}
}
