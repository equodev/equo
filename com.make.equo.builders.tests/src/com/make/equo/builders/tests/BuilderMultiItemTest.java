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

public class BuilderMultiItemTest extends AbstractBuilderTest {

	@Test
	public void should_Create_Toolbar_With_Two_ItemEventShortcut()
			throws URISyntaxException, NoSuchFieldException, IllegalAccessException {
		String tooltip1 = "text1";
		ToolbarItemBuilder toolItemBuilder1 = appBuilder.plainApp("/").withToolbar().addToolItem("text", tooltip1)
				.onClick(() -> System.out.println("toolitem event 1")).addShortcut("Alt + T");

		String tooltip2 = "text2";
		ToolbarItemBuilder toolItemBuilder2 = toolItemBuilder1.addToolItem("text", tooltip2)
				.onClick(() -> System.out.println("toolitem event 2")).addShortcut("Alt + Y");

		assertThat(toolItemBuilder1).isNotNull()
			.extracting("item").isNotNull();	
		assertThat(toolItemBuilder1)
			.extracting("toolbarBuilder.toolbar").asInstanceOf(InstanceOfAssertFactories.type(MToolBar.class)).isNotNull()
			.extracting(MToolBar::getChildren).asInstanceOf(InstanceOfAssertFactories.list(MHandledItem.class))
			.satisfies(list -> {assertThat(list).extracting(MHandledItem::getTooltip).contains(tooltip1);});
		
		assertThat(toolItemBuilder2).isNotNull()
			.extracting("item").isNotNull();	
		assertThat(toolItemBuilder2)
			.extracting("toolbarBuilder.toolbar").asInstanceOf(InstanceOfAssertFactories.type(MToolBar.class)).isNotNull()
			.extracting(MToolBar::getChildren).asInstanceOf(InstanceOfAssertFactories.list(MHandledItem.class))
			.satisfies(list -> {assertThat(list).extracting(MHandledItem::getTooltip).contains(tooltip2);});
	}

	@Test
	public void should_Create_Menu_With_Two_ItemEventShortcut()
			throws URISyntaxException, NoSuchFieldException, IllegalAccessException {
		String label1 = "item1";
		MenuItemBuilder menuItemBuilder1 = appBuilder.plainApp("/").withMainMenu("Menu1").addMenuItem(label1)
				.onClick(() -> System.out.println("menuitem event 1")).addShortcut("Alt + M");

		String label2 = "item2";
		MenuItemBuilder menuItemBuilder2 = menuItemBuilder1.addMenuItem(label2)
				.onClick(() -> System.out.println("menuitem event 2")).addShortcut("Alt + N");

		assertThat(menuItemBuilder1)
			.isNotNull()
			.extracting("item").isNotNull();
		assertThat(menuItemBuilder1)
			.extracting("menuBuilder.menu").asInstanceOf(InstanceOfAssertFactories.type(MMenu.class)).isNotNull()
			.extracting(MMenu::getChildren).asInstanceOf(InstanceOfAssertFactories.list(MMenuElement.class))
			.satisfies( list -> {assertThat(list).extracting(MMenuElement::getLabel).contains(label1);});

		assertThat(menuItemBuilder2)
			.isNotNull()
			.extracting("item").isNotNull();
		assertThat(menuItemBuilder2)
			.extracting("menuBuilder.menu").asInstanceOf(InstanceOfAssertFactories.type(MMenu.class)).isNotNull()
			.extracting(MMenu::getChildren).asInstanceOf(InstanceOfAssertFactories.list(MMenuElement.class))
			.satisfies( list -> {assertThat(list).extracting(MMenuElement::getLabel).contains(label2);});
	}

	@Test
	public void should_Create_ToolbarMenu_With_Two_ItemEventShortcut()
			throws URISyntaxException, NoSuchFieldException, IllegalAccessException {

		String tooltip1 = "text1";
		ToolbarItemBuilder toolItemBuilder1 = appBuilder.plainApp("/").withToolbar().addToolItem("text", tooltip1)
				.onClick(() -> System.out.println("toolitem event 1")).addShortcut("Alt + T");

		String tooltip2 = "text2";
		ToolbarItemBuilder toolItemBuilder2 = toolItemBuilder1.addToolItem("text", tooltip2)
				.onClick(() -> System.out.println("toolitem event 2")).addShortcut("Alt + Y");

		String label1 = "item1";
		MenuItemBuilder menuItemBuilder1 = toolItemBuilder2.withMainMenu("Menu1").addMenuItem(label1)
				.onClick(() -> System.out.println("menuitem event 1")).addShortcut("Alt + M");

		String label2 = "item2";
		MenuItemBuilder menuItemBuilder2 = menuItemBuilder1.addMenuItem(label2)
				.onClick(() -> System.out.println("menuitem event 2")).addShortcut("Alt + N");

		assertThat(toolItemBuilder1).isNotNull()
			.extracting("item").isNotNull();	
		assertThat(toolItemBuilder1)
			.extracting("toolbarBuilder.toolbar").asInstanceOf(InstanceOfAssertFactories.type(MToolBar.class)).isNotNull()
			.extracting(MToolBar::getChildren).asInstanceOf(InstanceOfAssertFactories.list(MHandledItem.class))
			.satisfies(list -> {assertThat(list).extracting(MHandledItem::getTooltip).contains(tooltip1);});
		
		assertThat(toolItemBuilder2).isNotNull()
			.extracting("item").isNotNull();	
		assertThat(toolItemBuilder2)
			.extracting("toolbarBuilder.toolbar").asInstanceOf(InstanceOfAssertFactories.type(MToolBar.class)).isNotNull()
			.extracting(MToolBar::getChildren).asInstanceOf(InstanceOfAssertFactories.list(MHandledItem.class))
			.satisfies(list -> {assertThat(list).extracting(MHandledItem::getTooltip).contains(tooltip2);});
			
		assertThat(menuItemBuilder1)
			.isNotNull()
			.extracting("item").isNotNull();
		assertThat(menuItemBuilder1)
			.extracting("menuBuilder.menu").asInstanceOf(InstanceOfAssertFactories.type(MMenu.class)).isNotNull()
			.extracting(MMenu::getChildren).asInstanceOf(InstanceOfAssertFactories.list(MMenuElement.class))
			.satisfies( list -> {assertThat(list).extracting(MMenuElement::getLabel).contains(label1);});
	
		assertThat(menuItemBuilder2)
			.isNotNull()
			.extracting("item").isNotNull();
		assertThat(menuItemBuilder2)
			.extracting("menuBuilder.menu").asInstanceOf(InstanceOfAssertFactories.type(MMenu.class)).isNotNull()
			.extracting(MMenu::getChildren).asInstanceOf(InstanceOfAssertFactories.list(MMenuElement.class))
			.satisfies( list -> {assertThat(list).extracting(MMenuElement::getLabel).contains(label2);});
		}

	@Test
	public void should_Create_MenuToolbar_With_Two_ItemEventShortcut()
			throws URISyntaxException, NoSuchFieldException, IllegalAccessException {

		String label1 = "item1";
		MenuItemBuilder menuItemBuilder1 = appBuilder.plainApp("/").withMainMenu("Menu1").addMenuItem(label1)
				.onClick(() -> System.out.println("menuitem event 1")).addShortcut("Alt + M");

		String label2 = "item2";
		MenuItemBuilder menuItemBuilder2 = menuItemBuilder1.addMenuItem(label2)
				.onClick(() -> System.out.println("menuitem event 2")).addShortcut("Alt + N");

		String tooltip1 = "text1";
		ToolbarItemBuilder toolItemBuilder1 = menuItemBuilder1.withToolbar().addToolItem("text", tooltip1)
				.onClick(() -> System.out.println("toolitem event 1")).addShortcut("Alt + T");

		String tooltip2 = "text2";
		ToolbarItemBuilder toolItemBuilder2 = toolItemBuilder1.addToolItem("text", tooltip2)
				.onClick(() -> System.out.println("toolitem event 2")).addShortcut("Alt + Y");
		
		assertThat(menuItemBuilder1)
			.isNotNull()
			.extracting("item").isNotNull();
		assertThat(menuItemBuilder1)
			.extracting("menuBuilder.menu").asInstanceOf(InstanceOfAssertFactories.type(MMenu.class)).isNotNull()
			.extracting(MMenu::getChildren).asInstanceOf(InstanceOfAssertFactories.list(MMenuElement.class))
			.satisfies( list -> {assertThat(list).extracting(MMenuElement::getLabel).contains(label1);});
	
		assertThat(menuItemBuilder2)
			.isNotNull()
			.extracting("item").isNotNull();
		assertThat(menuItemBuilder2)
			.extracting("menuBuilder.menu").asInstanceOf(InstanceOfAssertFactories.type(MMenu.class)).isNotNull()
			.extracting(MMenu::getChildren).asInstanceOf(InstanceOfAssertFactories.list(MMenuElement.class))
			.satisfies( list -> {assertThat(list).extracting(MMenuElement::getLabel).contains(label2);});

		assertThat(toolItemBuilder1).isNotNull()
			.extracting("item").isNotNull();	
		assertThat(toolItemBuilder1)
			.extracting("toolbarBuilder.toolbar").asInstanceOf(InstanceOfAssertFactories.type(MToolBar.class)).isNotNull()
			.extracting(MToolBar::getChildren).asInstanceOf(InstanceOfAssertFactories.list(MHandledItem.class))
			.satisfies(list -> {assertThat(list).extracting(MHandledItem::getTooltip).contains(tooltip1);});
		
		assertThat(toolItemBuilder2).isNotNull()
			.extracting("item").isNotNull();	
		assertThat(toolItemBuilder2)
			.extracting("toolbarBuilder.toolbar").asInstanceOf(InstanceOfAssertFactories.type(MToolBar.class)).isNotNull()
			.extracting(MToolBar::getChildren).asInstanceOf(InstanceOfAssertFactories.list(MHandledItem.class))
			.satisfies(list -> {assertThat(list).extracting(MHandledItem::getTooltip).contains(tooltip2);});
			
	}

}
