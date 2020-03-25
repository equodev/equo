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

public class BuilderMultiItemsCasesTest extends EquoInjectableTest {

	@Test
	public void toolbarSimpleTest() throws URISyntaxException, NoSuchFieldException, IllegalAccessException {
		String tooltip1 = "text1";
		ToolbarItemBuilder toolItemBuilder1 = appBuilder.plainApp("/").withToolbar().addToolItem("text", tooltip1)
				.onClick(new Runnable() {
					@Override
					public void run() {
						System.out.println("toolitem event 1");
					}
				}).addShortcut("Alt + T");

		String tooltip2 = "text2";
		ToolbarItemBuilder toolItemBuilder2 = toolItemBuilder1.addToolItem("text", tooltip2).onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("toolitem event 2");
			}
		}).addShortcut("Alt + Y");

		// obtaining Item Model via reflection
		MHandledItem toolItem1 = (MHandledItem) getValueFromField(toolItemBuilder1.getClass().getSuperclass(),
				toolItemBuilder1, "item");
		ToolbarBuilder toolbarBuilder1 = (ToolbarBuilder) getValueFromField(ToolbarItemBuilder.class, toolItemBuilder1,
				"toolbarBuilder");
		MToolBar toolbar1 = (MToolBar) getValueFromField(ToolbarBuilder.class, toolbarBuilder1, "toolbar");

		assertThat(toolItemBuilder1).isNotNull();
		assertThat(toolItemBuilder1).extracting("item").isNotNull();
		assertThat(toolItemBuilder1).extracting("text").containsExactly(tooltip1);
		assertThat(toolItemBuilder1).extracting("toolbarBuilder").extracting("toolbar").isNotNull();
		assertThat(toolbar1.getChildren()).contains(new MToolBarElement[] { (MToolBarElement) toolItem1 });

		// obtaining Item Model via reflection
		MHandledItem toolItem2 = (MHandledItem) getValueFromField(toolItemBuilder2.getClass().getSuperclass(),
				toolItemBuilder2, "item");
		ToolbarBuilder toolbarBuilder2 = (ToolbarBuilder) getValueFromField(ToolbarItemBuilder.class, toolItemBuilder2,
				"toolbarBuilder");
		MToolBar toolbar2 = (MToolBar) getValueFromField(ToolbarBuilder.class, toolbarBuilder2, "toolbar");

		assertThat(toolItemBuilder2).isNotNull();
		assertThat(toolItemBuilder2).extracting("item").isNotNull();
		assertThat(toolItemBuilder2).extracting("text").containsExactly(tooltip2);
		assertThat(toolItemBuilder2).extracting("toolbarBuilder").extracting("toolbar").isNotNull();
		assertThat(toolbar2.getChildren()).contains(new MToolBarElement[] { (MToolBarElement) toolItem2 });
	}

	@Test
	public void menuSimpleTest() throws URISyntaxException, NoSuchFieldException, IllegalAccessException {
		String label1 = "item1";
		MenuItemBuilder menuItemBuilder1 = appBuilder.plainApp("/").withMainMenu("Menu1").addMenuItem(label1)
				.onClick(new Runnable() {
					@Override
					public void run() {
						System.out.println("menuitem event 1");
					}
				}).addShortcut("Alt + M");

		String label2 = "item2";
		MenuItemBuilder menuItemBuilder2 = menuItemBuilder1.addMenuItem(label2).onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("menuitem event 2");
			}
		}).addShortcut("Alt + N");

		// obtaining Item Model via reflection
		MHandledItem menuItem1 = (MHandledItem) getValueFromField(menuItemBuilder1.getClass().getSuperclass(),
				menuItemBuilder1, "item");
		MenuBuilder menuBuilder1 = (MenuBuilder) getValueFromField(MenuItemBuilder.class, menuItemBuilder1,
				"menuBuilder");
		MMenu menu1 = (MMenu) getValueFromField(MenuBuilder.class, menuBuilder1, "menu");

		assertThat(menuItemBuilder1).isNotNull();
		assertThat(menuItemBuilder1).extracting("item").isNotNull();
		assertThat(menuItemBuilder1).extracting("item").extractingResultOf("getLabel").contains(label1);
		assertThat(menu1.getChildren()).contains(new MMenuElement[] { (MMenuElement) menuItem1 });

		// obtaining Item Model via reflection
		MHandledItem menuItem2 = (MHandledItem) getValueFromField(menuItemBuilder2.getClass().getSuperclass(),
				menuItemBuilder2, "item");
		MenuBuilder menuBuilder2 = (MenuBuilder) getValueFromField(MenuItemBuilder.class, menuItemBuilder2,
				"menuBuilder");
		MMenu menu2 = (MMenu) getValueFromField(MenuBuilder.class, menuBuilder2, "menu");

		assertThat(menuItemBuilder2).isNotNull();
		assertThat(menuItemBuilder2).extracting("item").isNotNull();
		assertThat(menuItemBuilder2).extracting("item").extractingResultOf("getLabel").contains(label2);
		assertThat(menu2.getChildren()).contains(new MMenuElement[] { (MMenuElement) menuItem2 });
	}

	@Test
	public void toolbarAndMenuTest() throws URISyntaxException, NoSuchFieldException, IllegalAccessException {

		String tooltip1 = "text1";
		ToolbarItemBuilder toolItemBuilder1 = appBuilder.plainApp("/").withToolbar().addToolItem("text", tooltip1)
				.onClick(new Runnable() {
					@Override
					public void run() {
						System.out.println("toolitem event 1");
					}
				}).addShortcut("Alt + T");

		String tooltip2 = "text2";
		ToolbarItemBuilder toolItemBuilder2 = toolItemBuilder1.addToolItem("text", tooltip2).onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("toolitem event 2");
			}
		}).addShortcut("Alt + Y");

		String label1 = "item1";
		MenuItemBuilder menuItemBuilder1 = toolItemBuilder2.withMainMenu("Menu1").addMenuItem(label1)
				.onClick(new Runnable() {
					@Override
					public void run() {
						System.out.println("menuitem event 1");
					}
				}).addShortcut("Alt + M");

		String label2 = "item2";
		MenuItemBuilder menuItemBuilder2 = menuItemBuilder1.addMenuItem(label2).onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("menuitem event 2");
			}
		}).addShortcut("Alt + N");

		// obtaining Item Model via reflection
		MHandledItem toolItem1 = (MHandledItem) getValueFromField(toolItemBuilder1.getClass().getSuperclass(),
				toolItemBuilder1, "item");
		ToolbarBuilder toolbarBuilder1 = (ToolbarBuilder) getValueFromField(ToolbarItemBuilder.class, toolItemBuilder1,
				"toolbarBuilder");
		MToolBar toolbar1 = (MToolBar) getValueFromField(ToolbarBuilder.class, toolbarBuilder1, "toolbar");

		assertThat(toolItemBuilder1).isNotNull();
		assertThat(toolItemBuilder1).extracting("item").isNotNull();
		assertThat(toolItemBuilder1).extracting("text").containsExactly(tooltip1);
		assertThat(toolItemBuilder1).extracting("toolbarBuilder").extracting("toolbar").isNotNull();
		assertThat(toolbar1.getChildren()).contains(new MToolBarElement[] { (MToolBarElement) toolItem1 });

		// obtaining Item Model via reflection
		MHandledItem toolItem2 = (MHandledItem) getValueFromField(toolItemBuilder2.getClass().getSuperclass(),
				toolItemBuilder2, "item");
		ToolbarBuilder toolbarBuilder2 = (ToolbarBuilder) getValueFromField(ToolbarItemBuilder.class, toolItemBuilder2,
				"toolbarBuilder");
		MToolBar toolbar2 = (MToolBar) getValueFromField(ToolbarBuilder.class, toolbarBuilder2, "toolbar");

		assertThat(toolItemBuilder2).isNotNull();
		assertThat(toolItemBuilder2).extracting("item").isNotNull();
		assertThat(toolItemBuilder2).extracting("text").containsExactly(tooltip2);
		assertThat(toolItemBuilder2).extracting("toolbarBuilder").extracting("toolbar").isNotNull();
		assertThat(toolbar2.getChildren()).contains(new MToolBarElement[] { (MToolBarElement) toolItem2 });

		// obtaining Item Model via reflection
		MHandledItem menuItem1 = (MHandledItem) getValueFromField(menuItemBuilder1.getClass().getSuperclass(),
				menuItemBuilder1, "item");
		MenuBuilder menuBuilder1 = (MenuBuilder) getValueFromField(MenuItemBuilder.class, menuItemBuilder1,
				"menuBuilder");
		MMenu menu1 = (MMenu) getValueFromField(MenuBuilder.class, menuBuilder1, "menu");

		assertThat(menuItemBuilder1).isNotNull();
		assertThat(menuItemBuilder1).extracting("item").isNotNull();
		assertThat(menuItemBuilder1).extracting("item").extractingResultOf("getLabel").contains(label1);
		assertThat(menu1.getChildren()).contains(new MMenuElement[] { (MMenuElement) menuItem1 });

		// obtaining Item Model via reflection
		MHandledItem menuItem2 = (MHandledItem) getValueFromField(menuItemBuilder2.getClass().getSuperclass(),
				menuItemBuilder2, "item");
		MenuBuilder menuBuilder2 = (MenuBuilder) getValueFromField(MenuItemBuilder.class, menuItemBuilder2,
				"menuBuilder");
		MMenu menu2 = (MMenu) getValueFromField(MenuBuilder.class, menuBuilder2, "menu");

		assertThat(menuItemBuilder2).isNotNull();
		assertThat(menuItemBuilder2).extracting("item").isNotNull();
		assertThat(menuItemBuilder2).extracting("item").extractingResultOf("getLabel").contains(label2);
		assertThat(menu2.getChildren()).contains(new MMenuElement[] { (MMenuElement) menuItem2 });
	}

	@Test
	public void menuAndToolbarTest() throws URISyntaxException, NoSuchFieldException, IllegalAccessException {

		String label1 = "item1";
		MenuItemBuilder menuItemBuilder1 = appBuilder.plainApp("/").withMainMenu("Menu1").addMenuItem(label1)
				.onClick(new Runnable() {
					@Override
					public void run() {
						System.out.println("menuitem event 1");
					}
				}).addShortcut("Alt + M");

		String label2 = "item2";
		MenuItemBuilder menuItemBuilder2 = menuItemBuilder1.addMenuItem(label2).onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("menuitem event 2");
			}
		}).addShortcut("Alt + N");

		String tooltip1 = "text1";
		ToolbarItemBuilder toolItemBuilder1 = menuItemBuilder1.withToolbar().addToolItem("text", tooltip1)
				.onClick(new Runnable() {
					@Override
					public void run() {
						System.out.println("toolitem event 1");
					}
				}).addShortcut("Alt + T");

		String tooltip2 = "text2";
		ToolbarItemBuilder toolItemBuilder2 = toolItemBuilder1.addToolItem("text", tooltip2).onClick(new Runnable() {
			@Override
			public void run() {
				System.out.println("toolitem event 2");
			}
		}).addShortcut("Alt + Y");

		// obtaining Item Model via reflection
		MHandledItem menuItem1 = (MHandledItem) getValueFromField(menuItemBuilder1.getClass().getSuperclass(),
				menuItemBuilder1, "item");
		MenuBuilder menuBuilder1 = (MenuBuilder) getValueFromField(MenuItemBuilder.class, menuItemBuilder1,
				"menuBuilder");
		MMenu menu1 = (MMenu) getValueFromField(MenuBuilder.class, menuBuilder1, "menu");

		assertThat(menuItemBuilder1).isNotNull();
		assertThat(menuItemBuilder1).extracting("item").isNotNull();
		assertThat(menuItemBuilder1).extracting("item").extractingResultOf("getLabel").contains(label1);
		assertThat(menu1.getChildren()).contains(new MMenuElement[] { (MMenuElement) menuItem1 });

		// obtaining Item Model via reflection
		MHandledItem menuItem2 = (MHandledItem) getValueFromField(menuItemBuilder2.getClass().getSuperclass(),
				menuItemBuilder2, "item");
		MenuBuilder menuBuilder2 = (MenuBuilder) getValueFromField(MenuItemBuilder.class, menuItemBuilder2,
				"menuBuilder");
		MMenu menu2 = (MMenu) getValueFromField(MenuBuilder.class, menuBuilder2, "menu");

		assertThat(menuItemBuilder2).isNotNull();
		assertThat(menuItemBuilder2).extracting("item").isNotNull();
		assertThat(menuItemBuilder2).extracting("item").extractingResultOf("getLabel").contains(label2);
		assertThat(menu2.getChildren()).contains(new MMenuElement[] { (MMenuElement) menuItem2 });

		// obtaining Item Model via reflection
		MHandledItem toolItem1 = (MHandledItem) getValueFromField(toolItemBuilder1.getClass().getSuperclass(),
				toolItemBuilder1, "item");
		ToolbarBuilder toolbarBuilder1 = (ToolbarBuilder) getValueFromField(ToolbarItemBuilder.class, toolItemBuilder1,
				"toolbarBuilder");
		MToolBar toolbar1 = (MToolBar) getValueFromField(ToolbarBuilder.class, toolbarBuilder1, "toolbar");

		assertThat(toolItemBuilder1).isNotNull();
		assertThat(toolItemBuilder1).extracting("item").isNotNull();
		assertThat(toolItemBuilder1).extracting("text").containsExactly(tooltip1);
		assertThat(toolItemBuilder1).extracting("toolbarBuilder").extracting("toolbar").isNotNull();
		assertThat(toolbar1.getChildren()).contains(new MToolBarElement[] { (MToolBarElement) toolItem1 });

		// obtaining Item Model via reflection
		MHandledItem toolItem2 = (MHandledItem) getValueFromField(toolItemBuilder2.getClass().getSuperclass(),
				toolItemBuilder2, "item");
		ToolbarBuilder toolbarBuilder2 = (ToolbarBuilder) getValueFromField(ToolbarItemBuilder.class, toolItemBuilder2,
				"toolbarBuilder");
		MToolBar toolbar2 = (MToolBar) getValueFromField(ToolbarBuilder.class, toolbarBuilder2, "toolbar");

		assertThat(toolItemBuilder2).isNotNull();
		assertThat(toolItemBuilder2).extracting("item").isNotNull();
		assertThat(toolItemBuilder2).extracting("text").containsExactly(tooltip2);
		assertThat(toolItemBuilder2).extracting("toolbarBuilder").extracting("toolbar").isNotNull();
		assertThat(toolbar2.getChildren()).contains(new MToolBarElement[] { (MToolBarElement) toolItem2 });
	}

}
