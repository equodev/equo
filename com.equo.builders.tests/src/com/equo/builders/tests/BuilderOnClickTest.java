package com.equo.builders.tests;

import org.junit.Test;

import com.equo.application.model.MenuItemBuilder;
import com.equo.application.model.ToolbarItemBuilder;

public class BuilderOnClickTest extends AbstractBuilderTest {


	@Test
	public void should_Create_Toolbar_With_An_ItemEvent() throws Exception {
		String tooltip = "text1";
		ToolbarItemBuilder toolItemBuilder = appBuilder.plainApp("/").withToolbar().addToolItem("text", tooltip)
				.onClick(() -> System.out.println("toolitem event 1"));

		assertCheckToolItemTooltip(tooltip, toolItemBuilder);
	}

	@Test
	public void should_Create_Menu_With_An_ItemEvent() throws Exception {

		String label = "item1";
		MenuItemBuilder menuItemBuilder = appBuilder.plainApp("/").withMainMenu("Menu1").addMenuItem(label)
				.onClick(() -> System.out.println("menuitem event 1"));
		
		assertCheckMenuItemLabel(label, menuItemBuilder);
	}

	@Test
	public void should_Create_MenuToolbar_With_An_ItemEvent() throws Exception {
		String label = "item1";
		MenuItemBuilder menuItemBuilder = appBuilder.plainApp("/").withMainMenu("Menu1").addMenuItem(label)
				.onClick(() -> System.out.println("menuitem event 1"));

		String tooltip = "text1";
		ToolbarItemBuilder toolItemBuilder = menuItemBuilder.withToolbar().addToolItem("text", tooltip)
				.onClick(() -> System.out.println("toolitem event 1"));

		assertCheckMenuItemLabel(label, menuItemBuilder);
		
		assertCheckToolItemTooltip(tooltip, toolItemBuilder);
	}

	@Test
	public void should_Create_ToolbarMenu_With_An_ItemEvent() throws Exception {
		String tooltip = "text1";
		ToolbarItemBuilder toolItemBuilder = appBuilder.plainApp("/").withToolbar().addToolItem("text", tooltip)
				.onClick(() -> System.out.println("toolitem event 1"));

		String label = "item1";
		MenuItemBuilder menuItemBuilder = toolItemBuilder.withMainMenu("Menu1").addMenuItem(label)
				.onClick(() -> System.out.println("menuitem event 1"));

		assertCheckToolItemTooltip(tooltip, toolItemBuilder);
		
		assertCheckMenuItemLabel(label, menuItemBuilder);

	}

}
