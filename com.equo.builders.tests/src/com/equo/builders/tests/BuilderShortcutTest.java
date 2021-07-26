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
    ToolbarItemBuilder toolItemBuilder =
        appBuilder.withUI("/").withToolbar().addToolItem(tooltip, tooltip)
            .onClick(() -> System.out.println("toolitem event 1")).withShortcut(shortcut);

    assertCheckToolItemTooltip(tooltip, toolItemBuilder);
    assertCheckItemShortcut(shortcut);
  }

  @Test
  public void should_Create_Menu_With_An_ItemEventShortcut() throws Exception {
    String label = "item1";
    String shortcut = "Alt + M";
    MenuItemBuilder menuItemBuilder =
        appBuilder.withUI("/").withMainMenu("Menu1").addMenuItem(label)
            .onClick(() -> System.out.println("menuitem event 1")).withShortcut(shortcut);

    assertCheckMenuItemLabel(label, menuItemBuilder);
    assertCheckItemShortcut(shortcut);
  }

  @Test
  public void should_Create_MenuToolbar_With_An_ItemEventShortcut() throws Exception {
    String label = "item1";
    String shortcutMenu = "Alt + M";
    MenuItemBuilder menuItemBuilder =
        appBuilder.withUI("/").withMainMenu("Menu1").addMenuItem(label)
            .onClick(() -> System.out.println("menuitem event 1")).withShortcut(shortcutMenu);

    String tooltip = "text1";
    String shortcutToolbar = "Alt + T";
    ToolbarItemBuilder toolItemBuilder = menuItemBuilder.withToolbar().addToolItem(tooltip, tooltip)
        .onClick(() -> System.out.println("toolitem event 1")).withShortcut(shortcutToolbar);

    assertCheckMenuItemLabel(label, menuItemBuilder);
    assertCheckItemShortcut(shortcutMenu);

    assertCheckToolItemTooltip(tooltip, toolItemBuilder);
    assertCheckItemShortcut(shortcutToolbar);
  }

  @Test
  public void should_Create_ToolbarMenu_With_An_ItemEventShortcut() throws Exception {
    String tooltip = "text1";
    String shortcutToolbar = "Alt + T";
    ToolbarItemBuilder toolItemBuilder =
        appBuilder.withUI("/").withToolbar().addToolItem(tooltip, tooltip)
            .onClick(() -> System.out.println("toolitem event 1")).withShortcut(shortcutToolbar);

    String label = "item1";
    String shortcutMenu = "Alt + M";
    MenuItemBuilder menuItemBuilder = toolItemBuilder.withMainMenu("Menu1").addMenuItem(label)
        .onClick(() -> System.out.println("menuitem event 1")).withShortcut(shortcutMenu);

    assertCheckToolItemTooltip(tooltip, toolItemBuilder);
    assertCheckItemShortcut(shortcutToolbar);

    assertCheckMenuItemLabel(label, menuItemBuilder);
    assertCheckItemShortcut(shortcutMenu);
  }

  @Test
  public void should_Create_Toolbar_With_An_ItemEventShortcut_declaring_shortcut_before_onclick()
      throws Exception {
    String tooltip = "text1";
    String shortcutToolbar = "Alt + T";
    ToolbarItemBuilder toolItemBuilder =
        appBuilder.withUI("/").withToolbar().addToolItem(tooltip, tooltip)
            .withShortcut(shortcutToolbar).onClick(() -> System.out.println("toolitem event 1"));

    assertCheckToolItemTooltip(tooltip, toolItemBuilder);
    assertCheckItemShortcut(shortcutToolbar);
  }

  @Test
  public void should_Create_Menu_With_An_ItemEventShortcut_declaring_shortcut_before_onclick()
      throws Exception {
    String label = "item1";
    String shortcut = "Alt + M";
    MenuItemBuilder menuItemBuilder =
        appBuilder.withUI("/").withMainMenu("Menu1").addMenuItem(label).withShortcut(shortcut)
            .onClick(() -> System.out.println("menuitem event 1"));

    assertCheckMenuItemLabel(label, menuItemBuilder);
    assertCheckItemShortcut(shortcut);
  }

  @Test
  public void should_Create_Menu_With_two_ItemEventShortcut_declaring_shortcut_before_onclick()
      throws Exception {
    String label1 = "item1";
    String shortcutMenu1 = "Alt + M";
    String label2 = "item2";
    String shortcutMenu2 = "Alt + N";
    MenuItemBuilder menuItemBuilder =
        appBuilder.withUI("/").withMainMenu("Menu").addMenuItem(label1).withShortcut(shortcutMenu1)
            .onClick(() -> System.out.println("menuitem event 1")).addMenuItem(label2)
            .withShortcut(shortcutMenu2).onClick(() -> System.out.println("menuitem event 2"));

    assertCheckMenuItemLabel(label1, menuItemBuilder);
    assertCheckItemShortcut(shortcutMenu1);

    assertCheckMenuItemLabel(label2, menuItemBuilder);
    assertCheckItemShortcut(shortcutMenu2);
  }

}
