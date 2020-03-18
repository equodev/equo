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
import com.make.equo.builders.tests.util.ModelTestingConfigurator;

public class BuilderSimpleCasesTest {
	
	@Reference
	private EquoApplicationBuilder appBuilderReferenced;
	
	private ModelTestingConfigurator modelTestingConfigurator = new ModelTestingConfigurator();
	
	@Test
	public void toolbarSimpleTest() throws URISyntaxException {
		EquoApplicationBuilder builder = getService(EquoApplicationBuilder.class);
		modelTestingConfigurator.configure(builder);
		assertThat(builder.plainApp("/").withToolbar().addToolItem("text", "text")).isNotNull();
	}
	
	@Test
	public void menuSimpleTest() throws URISyntaxException {
		EquoApplicationBuilder builder = getService(EquoApplicationBuilder.class);
		modelTestingConfigurator.configure(builder);
		assertThat(builder.plainApp("/").withMainMenu("Menu").addMenuItem("item1")).isNotNull();
	}
	
	@Test public void menuAndToolbarTest() throws URISyntaxException{
		EquoApplicationBuilder builder = getService(EquoApplicationBuilder.class);
		modelTestingConfigurator.configure(builder);
		assertThat(builder.plainApp("/").withMainMenu("Menu").addMenuItem("item1").withToolbar().addToolItem("text", "text")).isNotNull();
	}
	
	@Test public void toolbarAndmenuTest() throws URISyntaxException{
		EquoApplicationBuilder builder = getService(EquoApplicationBuilder.class);
		modelTestingConfigurator.configure(builder);
		assertThat(builder.plainApp("/").withToolbar().addToolItem("text", "text").withMainMenu("Menu").addMenuItem("item1")).isNotNull();
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
