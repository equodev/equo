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

package com.equo.application.model;

import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuSeparator;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuFactoryImpl;

/**
 * Equo menu item separator builder for Java.
 */
public class MenuItemSeparatorBuilder {

  private MenuBuilder menuBuilder;

  MenuItemSeparatorBuilder(MenuBuilder menuBuilder) {
    this.menuBuilder = menuBuilder;
  }

  /**
   * Adds a separator between menus.
   * @return this.
   */
  public MenuItemSeparatorBuilder addMenuItemSeparator() {
    MMenuSeparator menuSeparator = MenuFactoryImpl.eINSTANCE.createMenuSeparator();
    MMenu parentMenu = menuBuilder.getMenu();
    parentMenu.getChildren().add(menuSeparator);
    return this;
  }

  /**
   * Adds a new menu that will contain other menus.
   * @param  menuLabel the menu title.
   * @return           the MenuBuilder instance.
   */
  public MenuBuilder addMenu(String menuLabel) {
    return new MenuBuilder(this.menuBuilder).addMenu(menuLabel);
  }

  /**
   * Adds a new menu item that will not contain other menus.
   * @param  menuItemLabel the menu title.
   * @return               the MenuItemBuilder instance.
   */
  public MenuItemBuilder addMenuItem(String menuItemLabel) {
    return new MenuBuilder(this.menuBuilder).addMenuItem(menuItemLabel);
  }

  /**
   * Adds new menu item with full screen mode.
   * @param  menuItemLabel the menu title.
   * @return               the MenuItemBuilder instance.
   */
  public MenuItemBuilder addFullScreenModeMenuItem(String menuItemLabel) {
    return new MenuBuilder(this.menuBuilder).addFullScreenModeMenuItem(menuItemLabel);
  }
}
