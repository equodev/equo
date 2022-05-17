/****************************************************************************
**
** Copyright (C) 2021 Equo
**
** This file is part of the Equo SDK.
**
** Commercial License Usage
** Licensees holding valid commercial Equo licenses may use this file in
** accordance with the commercial license agreement provided with the
** Software or, alternatively, in accordance with the terms contained in
** a written agreement between you and Equo. For licensing terms
** and conditions see https://www.equo.dev/terms.
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

public class BuilderMultiItemTest extends AbstractBuilderTest {

	@Test
	public void should_Create_Toolbar_With_Two_ItemEventShortcut() throws Exception {
		String tooltip1 = "text1";
		String shortcut1 = "Alt + T";
		ToolbarItemBuilder toolItemBuilder1 = appBuilder.withUI("/").withToolbar().addToolItem("text", tooltip1)
				.onClick(() -> System.out.println("toolitem event 1")).withShortcut(shortcut1);

		String tooltip2 = "text2";
		String shortcut2 = "Alt + Y";
		ToolbarItemBuilder toolItemBuilder2 = toolItemBuilder1.addToolItem("text", tooltip2)
				.onClick(() -> System.out.println("toolitem event 2")).withShortcut(shortcut2);

		assertCheckToolItemTooltip(tooltip1, toolItemBuilder1);
		assertCheckItemShortcut(shortcut1);

		assertCheckToolItemTooltip(tooltip2, toolItemBuilder2);
		assertCheckItemShortcut(shortcut2);
	}

	@Test
	public void should_Create_Menu_With_Two_ItemEventShortcut() throws Exception {
		String label1 = "item1";
		String shortcut1 = "Alt + M";
		MenuItemBuilder menuItemBuilder1 = appBuilder.withUI("/").withMainMenu("Menu1").addMenuItem(label1)
				.onClick(() -> System.out.println("menuitem event 1")).withShortcut(shortcut1);

		String label2 = "item2";
		String shortcut2 = "Alt + N";
		MenuItemBuilder menuItemBuilder2 = menuItemBuilder1.addMenuItem(label2)
				.onClick(() -> System.out.println("menuitem event 2")).withShortcut(shortcut2);

		assertCheckMenuItemLabel(label1, menuItemBuilder1);
		assertCheckItemShortcut(shortcut1);

		assertCheckMenuItemLabel(label2, menuItemBuilder2);
		assertCheckItemShortcut(shortcut2);
	}

	@Test
	public void should_Create_ToolbarMenu_With_Two_ItemEventShortcut() throws Exception {

		String tooltip1 = "text1";
		String shortcutToolbar1 = "Alt + T";
		ToolbarItemBuilder toolItemBuilder1 = appBuilder.withUI("/").withToolbar().addToolItem("text", tooltip1)
				.onClick(() -> System.out.println("toolitem event 1")).withShortcut(shortcutToolbar1);

		String tooltip2 = "text2";
		String shortcutToolbar2 = "Alt + Y";
		ToolbarItemBuilder toolItemBuilder2 = toolItemBuilder1.addToolItem("text", tooltip2)
				.onClick(() -> System.out.println("toolitem event 2")).withShortcut(shortcutToolbar2);

		String label1 = "item1";
		String shortcutMenu1 = "Alt + M";
		MenuItemBuilder menuItemBuilder1 = toolItemBuilder2.withMainMenu("Menu1").addMenuItem(label1)
				.onClick(() -> System.out.println("menuitem event 1")).withShortcut(shortcutMenu1);

		String label2 = "item2";
		String shortcutMenu2 = "Alt + N";
		MenuItemBuilder menuItemBuilder2 = menuItemBuilder1.addMenuItem(label2)
				.onClick(() -> System.out.println("menuitem event 2")).withShortcut(shortcutMenu2);

		assertCheckToolItemTooltip(tooltip1, toolItemBuilder1);
		assertCheckItemShortcut(shortcutToolbar1);

		assertCheckToolItemTooltip(tooltip2, toolItemBuilder2);
		assertCheckItemShortcut(shortcutToolbar2);

		assertCheckMenuItemLabel(label1, menuItemBuilder1);
		assertCheckItemShortcut(shortcutMenu1);

		assertCheckMenuItemLabel(label2, menuItemBuilder2);
		assertCheckItemShortcut(shortcutMenu2);
		}

	@Test
	public void should_Create_MenuToolbar_With_Two_ItemEventShortcut() throws Exception {

		String label1 = "item1";
		String shortcutMenu1 = "Alt + M";
		MenuItemBuilder menuItemBuilder1 = appBuilder.withUI("/").withMainMenu("Menu1").addMenuItem(label1)
				.onClick(() -> System.out.println("menuitem event 1")).withShortcut(shortcutMenu1);

		String label2 = "item2";
		String shortcutMenu2 = "Alt + N";
		MenuItemBuilder menuItemBuilder2 = menuItemBuilder1.addMenuItem(label2)
				.onClick(() -> System.out.println("menuitem event 2")).withShortcut(shortcutMenu2);

		String tooltip1 = "text1";
		String shortcutToolbar1 = "Alt + T";
		ToolbarItemBuilder toolItemBuilder1 = menuItemBuilder1.withToolbar().addToolItem("text", tooltip1)
				.onClick(() -> System.out.println("toolitem event 1")).withShortcut(shortcutToolbar1);

		String tooltip2 = "text2";
		String shortcutToolbar2 = "Alt + Y";
		ToolbarItemBuilder toolItemBuilder2 = toolItemBuilder1.addToolItem("text", tooltip2)
				.onClick(() -> System.out.println("toolitem event 2")).withShortcut(shortcutToolbar2);

		assertCheckMenuItemLabel(label1, menuItemBuilder1);
		assertCheckItemShortcut(shortcutMenu1);

		assertCheckMenuItemLabel(label2, menuItemBuilder2);
		assertCheckItemShortcut(shortcutMenu2);

		assertCheckToolItemTooltip(tooltip1, toolItemBuilder1);
		assertCheckItemShortcut(shortcutToolbar1);

		assertCheckToolItemTooltip(tooltip2, toolItemBuilder2);
		assertCheckItemShortcut(shortcutToolbar2);
	}

}
