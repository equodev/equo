/****************************************************************************
**
** Copyright (C) 2021 Equo
**
** This file is part of Equo Framework.
**
** Commercial License Usage
** Licensees holding valid commercial Equo licenses may use this file in
** accordance with the commercial license agreement provided with the
** Software or, alternatively, in accordance with the terms contained in
** a written agreement between you and Equo. For licensing terms
** and conditions see https://www.equoplatform.com/terms.
**
** GNU General Public License Usage
** Alternatively, this file may be used under the terms of the GNU
** General Public License version 3 as published by the Free Software
** Foundation. Please review the following
** information to ensure the GNU General Public License requirements will
** be met: https://www.gnu.org/licenses/gpl-3.0.html.
**
****************************************************************************/

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
