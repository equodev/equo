package com.make.equo.builders.tests;

import static org.assertj.core.api.Assertions.assertThat;


import java.net.URISyntaxException;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.junit.Test;


import com.make.equo.application.model.MenuItemBuilder;
import com.make.equo.application.model.ToolbarItemBuilder;


public class BuilderShortcutTest extends AbstractBuilderTest {

	@Test
	public void should_Create_Toolbar_With_An_ItemEventShortcut() throws URISyntaxException, NoSuchFieldException, IllegalAccessException {
		String tooltip = "text1";
		ToolbarItemBuilder toolItemBuilder = appBuilder.plainApp("/").withToolbar().addToolItem(tooltip, tooltip)
				.onClick(() -> System.out.println("toolitem event 1")).addShortcut("Alt + T");

		assertThat(toolItemBuilder).isNotNull()
			.extracting("item").isNotNull();	
		assertThat(toolItemBuilder)
			.extracting("toolbarBuilder.toolbar").asInstanceOf(InstanceOfAssertFactories.type(MToolBar.class)).isNotNull()
			.extracting(MToolBar::getChildren).asInstanceOf(InstanceOfAssertFactories.list(MHandledItem.class))
			.satisfies(list -> {assertThat(list).extracting(MHandledItem::getTooltip).containsExactly(tooltip);});

	}

	@Test
	public void should_Create_Menu_With_An_ItemEventShortcut() throws URISyntaxException, NoSuchFieldException, IllegalAccessException {
		String label = "item1";
		MenuItemBuilder menuItemBuilder = appBuilder.plainApp("/").withMainMenu("Menu1").addMenuItem(label)
				.onClick(() -> System.out.println("menuitem event 1")).addShortcut("Alt + M");

		assertThat(menuItemBuilder)
			.isNotNull()
			.extracting("item").isNotNull();
		assertThat(menuItemBuilder)
			.extracting("menuBuilder.menu").asInstanceOf(InstanceOfAssertFactories.type(MMenu.class)).isNotNull()
			.extracting(MMenu::getChildren).asInstanceOf(InstanceOfAssertFactories.list(MMenuElement.class))
			.satisfies( list -> {assertThat(list).extracting(MMenuElement::getLabel).containsExactly(label);});
		}

	@Test
	public void should_Create_MenuToolbar_With_An_ItemEventShortcut() throws URISyntaxException, NoSuchFieldException, IllegalAccessException {
		String label = "item1";
		MenuItemBuilder menuItemBuilder = appBuilder.plainApp("/").withMainMenu("Menu1").addMenuItem(label)
				.onClick(() -> System.out.println("menuitem event 1")).addShortcut("Alt + M");

		String tooltip = "text1";
		ToolbarItemBuilder toolItemBuilder = menuItemBuilder.withToolbar().addToolItem(tooltip, tooltip)
				.onClick(() -> System.out.println("toolitem event 1")).addShortcut("Alt + T");

		assertThat(menuItemBuilder)
			.isNotNull()
			.extracting("item").isNotNull();
		assertThat(menuItemBuilder)
			.extracting("menuBuilder.menu").asInstanceOf(InstanceOfAssertFactories.type(MMenu.class)).isNotNull()
			.extracting(MMenu::getChildren).asInstanceOf(InstanceOfAssertFactories.list(MMenuElement.class))
			.satisfies( list -> {assertThat(list).extracting(MMenuElement::getLabel).containsExactly(label);});

		assertThat(toolItemBuilder).isNotNull()
			.extracting("item").isNotNull();	
		assertThat(toolItemBuilder)
			.extracting("toolbarBuilder.toolbar").asInstanceOf(InstanceOfAssertFactories.type(MToolBar.class)).isNotNull()
			.extracting(MToolBar::getChildren).asInstanceOf(InstanceOfAssertFactories.list(MHandledItem.class))
			.satisfies(list -> {assertThat(list).extracting(MHandledItem::getTooltip).containsExactly(tooltip);});

	}

	@Test
	public void should_Create_ToolbarMenu_With_An_ItemEventShortcut() throws URISyntaxException, NoSuchFieldException, IllegalAccessException {
		String tooltip = "text1";
		ToolbarItemBuilder toolItemBuilder = appBuilder.plainApp("/").withToolbar().addToolItem(tooltip, tooltip)
				.onClick(() -> System.out.println("toolitem event 1")).addShortcut("Alt + T");

		String label = "item1";
		MenuItemBuilder menuItemBuilder = toolItemBuilder.withMainMenu("Menu1").addMenuItem(label)
				.onClick(() -> System.out.println("menuitem event 1")).addShortcut("Alt + M");

		assertThat(toolItemBuilder).isNotNull()
			.extracting("item").isNotNull();	
		assertThat(toolItemBuilder)
			.extracting("toolbarBuilder.toolbar").asInstanceOf(InstanceOfAssertFactories.type(MToolBar.class)).isNotNull()
			.extracting(MToolBar::getChildren).asInstanceOf(InstanceOfAssertFactories.list(MHandledItem.class))
			.satisfies(list -> {assertThat(list).extracting(MHandledItem::getTooltip).containsExactly(tooltip);});
		
		assertThat(menuItemBuilder)
			.isNotNull()
			.extracting("item").isNotNull();
		assertThat(menuItemBuilder)
			.extracting("menuBuilder.menu").asInstanceOf(InstanceOfAssertFactories.type(MMenu.class)).isNotNull()
			.extracting(MMenu::getChildren).asInstanceOf(InstanceOfAssertFactories.list(MMenuElement.class))
			.satisfies( list -> {assertThat(list).extracting(MMenuElement::getLabel).containsExactly(label);});

	}

}
