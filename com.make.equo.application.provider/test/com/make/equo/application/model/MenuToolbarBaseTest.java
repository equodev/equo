package com.make.equo.application.model;

import java.net.URISyntaxException;

import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.ServiceTracker;

import com.make.equo.application.model.EquoApplicationBuilder;

public class MenuToolbarBaseTest {

	private IApplicationBuilder appBuilderReferenced;
	
	@Test(expected = RuntimeException.class)
	public void SimpleMenuCase() throws URISyntaxException {
		IApplicationBuilder appBuilder = getService(EquoApplicationBuilder.class);
		appBuilder.plainApp("/").withMainMenu("menuTest").addMenuItem("menu1");

	}
	
	@Test(expected = RuntimeException.class)
	public void SimpleToolbarCase() throws URISyntaxException {
		IApplicationBuilder appBuilder = getService(EquoApplicationBuilder.class);
		appBuilder.plainApp("/").withToolbar().addToolItem("chat", "tool1");
	}

	static <T> T getService(Class<T> clazz) {
		Bundle bundle = FrameworkUtil.getBundle(MenuToolbarBaseTest.class);
		if (bundle != null) {
			ServiceTracker<T, T> st = new ServiceTracker<T, T>(bundle.getBundleContext(), clazz, null);
			st.open();
			if (st != null) {
				try {
					// give the runtime some time to startup
					return st.waitForService(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	@Reference
	public void setAppBuilder(IApplicationBuilder app) {
		this.appBuilderReferenced = app;
	}
	
	
}

