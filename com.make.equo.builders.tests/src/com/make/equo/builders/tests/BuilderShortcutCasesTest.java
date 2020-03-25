package com.make.equo.builders.tests;

import static org.assertj.core.api.Assertions.assertThat;


import java.net.URISyntaxException;

import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarElement;
import org.junit.Test;


import com.make.equo.application.model.MenuBuilder;
import com.make.equo.application.model.MenuItemBuilder;
import com.make.equo.application.model.ToolbarBuilder;
import com.make.equo.application.model.ToolbarItemBuilder;


public class BuilderShortcutCasesTest extends EquoInjectableTest {

	@Test
	public void toolbarSimpleTest() throws URISyntaxException, NoSuchFieldException, IllegalAccessException {
		String tooltip = "text1";
		ToolbarItemBuilder toolItemBuilder = appBuilder.plainApp("/").withToolbar().addToolItem(tooltip, tooltip)
				.onClick(new Runnable() {
					@Override
					public void run() {
						System.out.println("toolitem event 1");
					}
				}).addShortcut("Alt + T");

		// obtaining Item Model via reflection
		MHandledItem toolItem = (MHandledItem) getValueFromField(toolItemBuilder.getClass().getSuperclass(),
				toolItemBuilder, "item");
		ToolbarBuilder toolbarBuilder = (ToolbarBuilder) getValueFromField(ToolbarItemBuilder.class, toolItemBuilder,
				"toolbarBuilder");
		MToolBar toolbar = (MToolBar) getValueFromField(ToolbarBuilder.class, toolbarBuilder, "toolbar");

		assertThat(toolItemBuilder).isNotNull();
		assertThat(toolItemBuilder).extracting("item").isNotNull();
		assertThat(toolItemBuilder).extracting("text").containsExactly(tooltip);
		assertThat(toolItemBuilder).extracting("toolbarBuilder").extracting("toolbar").isNotNull();
		assertThat(toolbar.getChildren()).contains(new MToolBarElement[] { (MToolBarElement) toolItem });

	}

	@Test
	public void menuSimpleTest() throws URISyntaxException, NoSuchFieldException, IllegalAccessException {
		String label = "item1";
		MenuItemBuilder menuItemBuilder = appBuilder.plainApp("/").withMainMenu("Menu1").addMenuItem(label)
				.onClick(new Runnable() {
					@Override
					public void run() {
						System.out.println("menuitem event 1");
					}
				}).addShortcut("Alt + M");

		// obtaining Item Model via reflection
		MHandledItem menuItem = (MHandledItem) getValueFromField(menuItemBuilder.getClass().getSuperclass(),
				menuItemBuilder, "item");
		MenuBuilder menuBuilder = (MenuBuilder) getValueFromField(MenuItemBuilder.class, menuItemBuilder,
				"menuBuilder");
		MMenu menu = (MMenu) getValueFromField(MenuBuilder.class, menuBuilder, "menu");

		assertThat(menuItemBuilder).isNotNull();
		assertThat(menuItemBuilder).extracting("item").isNotNull();
		assertThat(menuItemBuilder).extracting("item").extractingResultOf("getLabel").containsExactly(label);
		assertThat(menu.getChildren()).contains(new MMenuElement[] { (MMenuElement) menuItem });
	}

	@Test
	public void menuAndToolbarTest() throws URISyntaxException, NoSuchFieldException, IllegalAccessException {
		String label = "item1";
		MenuItemBuilder menuItemBuilder = appBuilder.plainApp("/").withMainMenu("Menu1").addMenuItem(label)
				.onClick(new Runnable() {
					@Override
					public void run() {
						System.out.println("menuitem event 1");
					}
				}).addShortcut("Alt + M");

		String tooltip = "text1";
		ToolbarItemBuilder toolItemBuilder = menuItemBuilder.withToolbar().addToolItem(tooltip, tooltip)
				.onClick(new Runnable() {
					@Override
					public void run() {
						System.out.println("toolitem event 1");
					}
				}).addShortcut("Alt + T");

		// obtaining Item Model via reflection
		MHandledItem menuItem = (MHandledItem) getValueFromField(menuItemBuilder.getClass().getSuperclass(),
				menuItemBuilder, "item");
		MenuBuilder menuBuilder = (MenuBuilder) getValueFromField(MenuItemBuilder.class, menuItemBuilder,
				"menuBuilder");
		MMenu menu = (MMenu) getValueFromField(MenuBuilder.class, menuBuilder, "menu");

		assertThat(menuItemBuilder).isNotNull();
		assertThat(menuItemBuilder).extracting("item").isNotNull();
		assertThat(menuItemBuilder).extracting("item").extractingResultOf("getLabel").containsExactly(label);
		assertThat(menu.getChildren()).contains(new MMenuElement[] { (MMenuElement) menuItem });

		// obtaining Item Model via reflection
		MHandledItem toolItem = (MHandledItem) getValueFromField(toolItemBuilder.getClass().getSuperclass(),
				toolItemBuilder, "item");
		ToolbarBuilder toolbarBuilder = (ToolbarBuilder) getValueFromField(ToolbarItemBuilder.class, toolItemBuilder,
				"toolbarBuilder");
		MToolBar toolbar = (MToolBar) getValueFromField(ToolbarBuilder.class, toolbarBuilder, "toolbar");

		assertThat(toolItemBuilder).isNotNull();
		assertThat(toolItemBuilder).extracting("item").isNotNull();
		assertThat(toolItemBuilder).extracting("text").containsExactly(tooltip);
		assertThat(toolItemBuilder).extracting("toolbarBuilder").extracting("toolbar").isNotNull();
		assertThat(toolbar.getChildren()).contains(new MToolBarElement[] { (MToolBarElement) toolItem });
	}

	@Test
	public void toolbarAndMenuTest() throws URISyntaxException, NoSuchFieldException, IllegalAccessException {
		String tooltip = "text1";
		ToolbarItemBuilder toolItemBuilder = appBuilder.plainApp("/").withToolbar().addToolItem(tooltip, tooltip)
				.onClick(new Runnable() {
					@Override
					public void run() {
						System.out.println("toolitem event 1");
					}
				}).addShortcut("Alt + T");

		String label = "item1";
		MenuItemBuilder menuItemBuilder = toolItemBuilder.withMainMenu("Menu1").addMenuItem(label)
				.onClick(new Runnable() {
					@Override
					public void run() {
						System.out.println("menuitem event 1");
					}
				}).addShortcut("Alt + M");

		
		// obtaining Item Model via reflection
		MHandledItem toolItem = (MHandledItem) getValueFromField(toolItemBuilder.getClass().getSuperclass(),
				toolItemBuilder, "item");
		ToolbarBuilder toolbarBuilder = (ToolbarBuilder) getValueFromField(ToolbarItemBuilder.class, toolItemBuilder,
				"toolbarBuilder");
		MToolBar toolbar = (MToolBar) getValueFromField(ToolbarBuilder.class, toolbarBuilder, "toolbar");

		assertThat(toolItemBuilder).isNotNull();
		assertThat(toolItemBuilder).extracting("item").isNotNull();
		assertThat(toolItemBuilder).extracting("text").containsExactly(tooltip);
		assertThat(toolItemBuilder).extracting("toolbarBuilder").extracting("toolbar").isNotNull();
		assertThat(toolbar.getChildren()).contains(new MToolBarElement[] { (MToolBarElement) toolItem });

		// obtaining Item Model via reflection
		MHandledItem menuItem = (MHandledItem) getValueFromField(menuItemBuilder.getClass().getSuperclass(),
				menuItemBuilder, "item");
		MenuBuilder menuBuilder = (MenuBuilder) getValueFromField(MenuItemBuilder.class, menuItemBuilder,
				"menuBuilder");
		MMenu menu = (MMenu) getValueFromField(MenuBuilder.class, menuBuilder, "menu");

		assertThat(menuItemBuilder).isNotNull();
		assertThat(menuItemBuilder).extracting("item").isNotNull();
		assertThat(menuItemBuilder).extracting("item").extractingResultOf("getLabel").containsExactly(label);
		assertThat(menu.getChildren()).contains(new MMenuElement[] { (MMenuElement) menuItem });

	}

}
