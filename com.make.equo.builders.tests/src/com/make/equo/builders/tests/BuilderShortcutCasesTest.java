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

public class BuilderShortcutCasesTest extends EquoInjectableTest{
			
	@Test
	public void toolbarSimpleTest() throws URISyntaxException {
		assertThat(appBuilder.plainApp("/").withToolbar().addToolItem("text", "text").onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("toolitem event 1");
			}
		}).addShortcut("Alt + T")).isNotNull();
	}

	@Test
	public void menuSimpleTest() throws URISyntaxException {
		assertThat(appBuilder.plainApp("/").withMainMenu("Menu1").addMenuItem("item1").onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("menuitem event 1");
			}
		}).addShortcut("Alt + M")).isNotNull();
	}

	@Test
	public void menuAndToolbarTest() throws URISyntaxException {
		assertThat(appBuilder.plainApp("/").withMainMenu("Menu1").addMenuItem("item1").onClick(new Runnable() {
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
		assertThat(appBuilder.plainApp("/").withToolbar().addToolItem("text", "text").onClick(new Runnable() {
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



}
