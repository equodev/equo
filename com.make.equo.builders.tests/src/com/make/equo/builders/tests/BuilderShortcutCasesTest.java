package com.make.equo.builders.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URISyntaxException;

import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.ServiceTracker;

import com.make.equo.application.model.EquoApplicationBuilder;
import com.make.equo.builders.tests.util.ModelTestingConfigurator;

public class BuilderShortcutCasesTest {
			
	@Reference
	private EquoApplicationBuilder appBuilderReferenced;

	private ModelTestingConfigurator modelTestingConfigurator = new ModelTestingConfigurator();

	@Test
	public void toolbarSimpleTest() throws URISyntaxException {
		EquoApplicationBuilder builder = getService(EquoApplicationBuilder.class);
		modelTestingConfigurator.configure(builder);
		assertThat(builder.plainApp("/").withToolbar().addToolItem("text", "text").onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("toolitem event 1");
			}
		}).addShortcut("Alt + T")).isNotNull();
	}

	@Test
	public void menuSimpleTest() throws URISyntaxException {
		EquoApplicationBuilder builder = getService(EquoApplicationBuilder.class);
		modelTestingConfigurator.configure(builder);
		assertThat(builder.plainApp("/").withMainMenu("Menu1").addMenuItem("item1").onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("menuitem event 1");
			}
		}).addShortcut("Alt + M")).isNotNull();
	}

	@Test
	public void menuAndToolbarTest() throws URISyntaxException {
		EquoApplicationBuilder builder = getService(EquoApplicationBuilder.class);
		modelTestingConfigurator.configure(builder);
		assertThat(builder.plainApp("/").withMainMenu("Menu1").addMenuItem("item1").onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("menuitem event 1");
			}
		}).addShortcut("Alt + M").withToolbar().addToolItem("text", "text").onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("toolitem event 1");
			}
		}).addShortcut("Alt + T")).isNotNull();
	}
	
	@Test
	public void toolbarAndMenuTest() throws URISyntaxException {
		EquoApplicationBuilder builder = getService(EquoApplicationBuilder.class);
		modelTestingConfigurator.configure(builder);
		assertThat(builder.plainApp("/").withToolbar().addToolItem("text", "text").onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("toolitem event 1");
			}
		}).addShortcut("Alt + T").withMainMenu("Menu1").addMenuItem("item1").onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("menuitem event 1");
			}
		}).addShortcut("Alt + M")).isNotNull();
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
