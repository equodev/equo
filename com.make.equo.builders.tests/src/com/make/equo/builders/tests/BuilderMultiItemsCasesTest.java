package com.make.equo.builders.tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URISyntaxException;

import org.junit.Test;

public class BuilderMultiItemsCasesTest extends EquoInjectableTest {
	
	@Test
	public void toolbarSimpleTest() throws URISyntaxException {
		assertThat(appBuilder.plainApp("/").withToolbar().addToolItem("text", "text").onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("toolitem event 1");
			}
		}).addShortcut("Alt + T").addToolItem("text", "text2").onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("toolitem event 2");
			}
		}).addShortcut("Alt + Y")).isNotNull();
	}

	@Test
	public void menuSimpleTest() throws URISyntaxException {
		assertThat(appBuilder.plainApp("/").withMainMenu("Menu1").addMenuItem("item1").onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("menuitem event 1");
			}
		}).addShortcut("Alt + M").addMenuItem("item2").onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("menuitem event 2");
			}
		}).addShortcut("Alt + N")).isNotNull();
	}

	@Test
	public void menuAndToolbarTest() throws URISyntaxException {
		assertThat(appBuilder.plainApp("/").withMainMenu("Menu1").addMenuItem("item1").onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("menuitem event 1");
			}
		}).addShortcut("Alt + M").addMenuItem("item2").onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("menuitem event 2");
			}
		}).addShortcut("Alt + N").withToolbar().addToolItem("text", "text").onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("toolitem event 1");
			}
		}).addShortcut("Alt + T").addToolItem("text", "text2").onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("toolitem event 2");
			}
		}).addShortcut("Alt + Y")).isNotNull();
	}
	
	@Test
	public void toolbarAndMenuTest() throws URISyntaxException {
		assertThat(appBuilder.plainApp("/").withToolbar().addToolItem("text", "text").onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("toolitem event 1");
			}
		}).addShortcut("Alt + T").addToolItem("text", "text2").onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("toolitem event 2");
			}
		}).addShortcut("Alt + Y").withMainMenu("Menu1").addMenuItem("item1").onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("menuitem event 1");
			}
		}).addShortcut("Alt + M").addMenuItem("item2").onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("menuitem event 2");
			}
		}).addShortcut("Alt + N")).isNotNull();
	}


}
