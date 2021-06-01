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

package com.equo.application.model;

import java.util.List;

import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuFactoryImpl;

import com.equo.application.util.IConstants;

/**
 * Equo menu builder for Java.
 */
public class MenuBuilder {

  private OptionalViewBuilder optionalFieldBuilder;
  private MMenu parentMenu;
  private MMenu menu;

  MenuBuilder(OptionalViewBuilder optionalFieldBuilder) {
    this.parentMenu = optionalFieldBuilder.getMainMenu();
    if (this.parentMenu == null) {
      this.parentMenu = optionalFieldBuilder.getEquoApplicationBuilder().getmWindow().getMainMenu();
    }
    this.optionalFieldBuilder = optionalFieldBuilder;
  }

  MenuBuilder(MenuBuilder menuBuilder) {
    this.parentMenu = menuBuilder.menu;
    this.menu = menuBuilder.menu;
    this.optionalFieldBuilder = menuBuilder.optionalFieldBuilder;
  }

  /**
   * Adds a new menu that will contain other menus.
   * @param  label the menu title.
   * @return       the MenuBuilder instance.
   */
  public MenuBuilder addMenu(String label) {
    for (MMenuElement children : parentMenu.getChildren()) {
      // If already exists a menu with this label, return that one
      if (children instanceof MMenu && children.getLabel().equals(label)) {
        this.menu = (MMenu) children;
        return new MenuBuilder(this);
      }
    }
    MMenu actualMenu = menu;
    menu = createMenu(parentMenu, label);
    MenuBuilder newMenuBuilder = new MenuBuilder(this);
    menu = actualMenu;
    return newMenuBuilder;
  }

  /**
   * Removes the current menu construction from the UI model. If it does not
   * exist, it will remove a current model in the user interface already
   * established.
   * @return the MenuBuilder instance.
   */
  public MenuBuilder remove() {
    if (menu == null) {
      List<MMenuElement> children = parentMenu.getChildren();
      for (MMenuElement menu : children) {
        removeRecursively(menu);
      }
      children.clear();
    } else {
      removeRecursively(menu);
      parentMenu.getChildren().remove(menu);
    }
    return new MenuBuilder(this);
  }

  private void removeShortcutFromItem(MMenuElement element) {
    if (element instanceof MHandledMenuItem) {
      MHandledMenuItem item = (MHandledMenuItem) element;
      Object shortcut = item.getTransientData().get(IConstants.ITEM_SHORTCUT);
      if (shortcut != null) {
        optionalFieldBuilder.removeShortcut((String) shortcut);
      }
    }
  }

  private void removeRecursively(MMenuElement element) {
    if (element instanceof MMenu) {
      MMenu menu = (MMenu) element;
      List<MMenuElement> childrens = menu.getChildren();
      for (MMenuElement children : childrens) {
        removeShortcutFromItem(children);
        removeRecursively(children);
        children.setVisible(false);
      }
      childrens.clear();
    }
    removeShortcutFromItem(element);
    element.setVisible(false);
  }

  /**
   * Removes a menu children by title.
   * @param  label the menu title.
   * @return       the MenuBuilder instance.
   */
  public MenuBuilder removeChildren(String label) {
    List<MMenuElement> childrens = menu.getChildren();
    MMenuElement itemToDelete = null;
    for (MMenuElement children : childrens) {
      if (children.getLabel().equals(label)) {
        removeRecursively(children);
        itemToDelete = children;
        break;
      }
    }
    if (itemToDelete != null) {
      childrens.remove(itemToDelete);
    }
    return new MenuBuilder(this);
  }

  private MMenu createMenu(MMenu parentMenu, String menuLabel) {
    MMenu newMenu = MenuFactoryImpl.eINSTANCE.createMenu();
    newMenu.setElementId(
        parentMenu.getElementId() + "." + menuLabel.replaceAll("\\s+", "").toLowerCase());
    newMenu.setLabel(menuLabel);
    parentMenu.getChildren().add(newMenu);
    return newMenu;
  }

  /**
   * Adds new menu item.
   * @param  label the menu title.
   * @return       the MenuItemBuilder instance.
   */
  public MenuItemBuilder addMenuItem(String label) {
    return new MenuItemBuilder(this).addMenuItem(label);
  }

  /**
   * Adds a Exit menu item only if needed (Not needed in OSx) and executes the
   * runnable before exiting the application.
   * @param  label    the menu title.
   * @param  runnable code to be executed before application exits.
   * @return          the MenuItemBuilder instance.
   */
  public MenuItemBuilder onBeforeExit(String label, Runnable runnable) {
    return new MenuItemBuilder(this).onBeforeExit(label, runnable);
  }

  /**
   * Executes the run method of the runnable before exiting the application.
   * @param  runnable code to be executed before application exits.
   * @return          the MenuItemBuilder instance.
   */
  public MenuItemBuilder onBeforeExit(Runnable runnable) {
    return new MenuItemBuilder(this).onBeforeExit(runnable);
  }

  /**
   * Adds a Preferences menu item only if needed (Not needed in OSx) and executes
   * the runnable when the item is accessed.
   * @param  label    the menu title.
   * @return          the MenuItemBuilder instance.
   */
  public MenuItemBuilder onPreferences(String label, Runnable runnable) {
    return new MenuItemBuilder(this).onPreferences(label, runnable);
  }

  /**
   * Executes the run method of the runnable when the item is accessed.
   * @param  runnable code to be executed before application exits.
   * @return          the MenuItemBuilder instance.
   */
  public MenuItemBuilder onPreferences(Runnable runnable) {
    return new MenuItemBuilder(this).onPreferences(runnable);
  }

  /**
   * Adds About menu item only if needed (Not needed in OSx) and executes the
   * runnable before exiting the application.
   * @param  label the menu title.
   * @return       the MenuItemBuilder instance.
   */
  public MenuItemBuilder onAbout(String label, Runnable runnable) {
    return new MenuItemBuilder(this).onAbout(label, runnable);
  }

  /**
   * Executes the run method of the runnable when the item is accessed.
   * @return the MenuItemBuilder instance.
   */
  public MenuItemBuilder onAbout(Runnable runnable) {
    return new MenuItemBuilder(this).onAbout(runnable);
  }

  /**
   * Adds new menu item with full screen mode.
   * @param  menuItemLabel the menu title.
   * @return               the MenuItemBuilder instance.
   */
  public MenuItemBuilder addFullScreenModeMenuItem(String menuItemLabel) {
    return new MenuItemBuilder(this).addFullScreenModeMenuItem(menuItemLabel);
  }

  OptionalViewBuilder getOptionalFieldBuilder() {
    return optionalFieldBuilder;
  }

  MMenu getParentMenu() {
    return parentMenu;
  }

  MMenu getMenu() {
    return menu;
  }
}
