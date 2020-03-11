package com.make.equo.builders.tests;

import static org.assertj.core.api.Assertions.assertThat;

import javax.management.InvalidApplicationException;

import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.ServiceTracker;
import com.make.equo.application.model.IApplicationBuilder;

public class BuilderMenuTest {
	
	@Reference
	private IApplicationBuilder appBuilderReferenced;
	
	@Test
	public void menuSimpleTest() {
		Bundle bundle = FrameworkUtil.getBundle(IApplicationBuilder.class);
		IApplicationBuilder builder = getService(IApplicationBuilder.class);
		assertThat(bundle).isNotNull();
		assertThat(bundle.getState()).isEqualTo(Bundle.ACTIVE);
		assertThat(appBuilderReferenced).isNull();
		//assertThat(true).isEqualTo(true);
	}

	static <T> T getService(Class<T> clazz) {
		Bundle bundle = FrameworkUtil.getBundle(BuilderMenuTest.class);
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
