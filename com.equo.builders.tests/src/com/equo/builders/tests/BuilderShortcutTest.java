package com.equo.builders.tests;

import org.junit.Test;

import com.equo.application.model.MenuItemBuilder;
import com.equo.application.model.ToolbarItemBuilder;


public class BuilderShortcutTest extends AbstractBuilderTest {

	@Test
	public void should_Create_Toolbar_With_An_ItemEventShortcut() throws Exception {
		String tooltip = "text1";
		String shortcut = "Alt + T";
		ToolbarItemBuilder toolItemBuilder = appBuilder.withUI("/").withToolbar().addToolItem(tooltip, tooltip)
				.onClick(() -> System.out.println("toolitem event 1")).addShortcut(shortcut);

		assertCheckToolItemTooltip(tooltip, toolItemBuilder);
		assertCheckItemShortcut(shortcut);

	}

	

	@Test
	public void should_Create_Menu_With_An_ItemEventShortcut() throws Exception {
		String label = "item1";
		String shortcut = "Alt + M";
		MenuItemBuilder menuItemBuilder = appBuilder.withUI("/").withMainMenu("Menu1").addMenuItem(label)
				.onClick(() -> System.out.println("menuitem event 1")).addShortcut(shortcut);
		
		assertCheckMenuItemLabel(label, menuItemBuilder);
		assertCheckItemShortcut(shortcut);
		
	}

	@Test
	public void should_Create_MenuToolbar_With_An_ItemEventShortcut() throws Exception {
		String label = "item1";
		String shortcutMenu = "Alt + M";
		MenuItemBuilder menuItemBuilder = appBuilder.withUI("/").withMainMenu("Menu1").addMenuItem(label)
				.onClick(() -> System.out.println("menuitem event 1")).addShortcut(shortcutMenu);

		String tooltip = "text1";
		String shortcutToolbar = "Alt + T";
		ToolbarItemBuilder toolItemBuilder = menuItemBuilder.withToolbar().addToolItem(tooltip, tooltip)
				.onClick(() -> System.out.println("toolitem event 1")).addShortcut(shortcutToolbar);

		assertCheckMenuItemLabel(label, menuItemBuilder);
		assertCheckItemShortcut(shortcutMenu);
		
		assertCheckToolItemTooltip(tooltip, toolItemBuilder);
		assertCheckItemShortcut(shortcutToolbar);
	}

	@Test
	public void should_Create_ToolbarMenu_With_An_ItemEventShortcut() throws Exception {
		String tooltip = "text1";
		String shortcutToolbar = "Alt + T";
		ToolbarItemBuilder toolItemBuilder = appBuilder.withUI("/").withToolbar().addToolItem(tooltip, tooltip)
				.onClick(() -> System.out.println("toolitem event 1")).addShortcut(shortcutToolbar);

		String label = "item1";
		String shortcutMenu = "Alt + M";
		MenuItemBuilder menuItemBuilder = toolItemBuilder.withMainMenu("Menu1").addMenuItem(label)
				.onClick(() -> System.out.println("menuitem event 1")).addShortcut(shortcutMenu);

		assertCheckToolItemTooltip(tooltip, toolItemBuilder);
		assertCheckItemShortcut(shortcutToolbar);
		
		assertCheckMenuItemLabel(label, menuItemBuilder);
		assertCheckItemShortcut(shortcutToolbar);
	}
	
	

}
