package com.make.equo.builders.tests;

import org.junit.Test;

import com.make.equo.application.model.MenuItemBuilder;
import com.make.equo.application.model.ToolbarItemBuilder;

public class BuilderSimpleTest extends AbstractBuilderTest {

	@Test
	public void should_create_toolbar_with_an_item() throws Exception {
		String tooltip = "text1";
		ToolbarItemBuilder toolItemBuilder = appBuilder.plainApp("/").withToolbar().addToolItem("text", tooltip);
		assertCheckToolItemTooltip(tooltip, toolItemBuilder);
			
	}

	@Test
	public void should_Create_Menu_With_An_Item() throws Exception {
		String label = "item1";
		MenuItemBuilder menuItemBuilder = appBuilder.plainApp("/").withMainMenu("Menu").addMenuItem(label);

		assertCheckMenuItemLabel(label, menuItemBuilder);
		
	}

	@Test
	public void should_Create_MenuToolbar_With_An_Item() throws Exception {
		String label = "item1";
		MenuItemBuilder menuItemBuilder = appBuilder.plainApp("/").withMainMenu("Menu").addMenuItem(label);
		
		String tooltip = "text1";
		ToolbarItemBuilder toolItemBuilder = menuItemBuilder.withToolbar().addToolItem("text", tooltip);

		assertCheckMenuItemLabel(label, menuItemBuilder);
		
		assertCheckToolItemTooltip(tooltip, toolItemBuilder);
	}

	@Test
	public void should_Create_ToolbarMenu_With_An_Item() throws Exception {
		String tooltip = "text1";
		ToolbarItemBuilder toolItemBuilder = appBuilder.plainApp("/").withToolbar().addToolItem("text", tooltip);

		String label = "item1";
		MenuItemBuilder menuItemBuilder = toolItemBuilder.withMainMenu("Menu").addMenuItem(label);
		
		assertCheckToolItemTooltip(tooltip, toolItemBuilder);
		
		assertCheckMenuItemLabel(label, menuItemBuilder);
	}


	
}
