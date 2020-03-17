package com.make.equo.builders.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URISyntaxException;

import javax.management.InvalidApplicationException;

import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.ServiceTracker;

import com.make.equo.application.api.IEquoApplication;
import com.make.equo.application.model.EquoApplicationBuilder;
import com.make.equo.builders.tests.util.EquoApp;

public class BuilderMenuTest {
	
	@Reference
	private EquoApplicationBuilder appBuilderReferenced;
	
	@Reference
	private EquoApp app;
	
	@Test
	public void menuSimpleTest() throws URISyntaxException {
		//Bundle bundle = FrameworkUtil.getBundle(EquoApplicationBuilder.class);
		EquoApplicationBuilder builder = getService(EquoApplicationBuilder.class);
		//assertThat(bundle).isNotNull();
		//assertThat(bundle.getState()).isEqualTo(Bundle.ACTIVE);
		//assertThat(builder).isNull();
		builder.plainApp("/").withToolbar().addToolItem("text", "text");
		assertThat(builder).isNotNull();
	}

	static <T> T getService(Class<T> clazz) {
		Bundle bundle = FrameworkUtil.getBundle(clazz);
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
	
}
