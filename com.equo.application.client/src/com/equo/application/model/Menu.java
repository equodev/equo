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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.workbench.renderers.swt.MenuManagerRenderer;
import org.eclipse.swt.widgets.Display;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Represents a complete model of an application menu.
 */
public class Menu implements IEquoMenu {
  private List<EquoMenu> menus = new ArrayList<>();

  Menu() {
  }

  @Override
  public EquoMenu withMainMenu(String title) {
    EquoMenu menu = getExistingMenu(title);
    if (menu == null) {
      menu = new EquoMenu(this, title);
      menus.add(menu);
    }
    return menu;
  }

  /**
   * Appends the menu item using path location.
   * @param  parentMenuPath the path to add menu.
   * @param  indexToAddItem the index to add item.
   * @param  newItemTitle   the menu title.
   * @return                the EquoMenuItem instance.
   */
  public EquoMenuItem appendMenuItem(String parentMenuPath, int indexToAddItem,
      String newItemTitle) {
    AbstractEquoMenu item = get(parentMenuPath);
    if (!(item instanceof EquoMenu)) {
      return null;
    }
    EquoMenu parent = (EquoMenu) item;
    return parent.addMenuItem(indexToAddItem, newItemTitle);
  }

  /**
   * Appends the menu item at the end.
   * @param  parentMenuPath the path to add menu.
   * @param  newItemTitle   the menu title.
   * @return                the EquoMenuItem instance.
   */
  public EquoMenuItem appendMenuItemAtTheEnd(String parentMenuPath, String newItemTitle) {
    return appendMenuItem(parentMenuPath, -1, newItemTitle);
  }

  /**
   * Appends the menu using path location.
   * @param  parentMenuPath the path to add menu.
   * @param  indexToAddMenu the index to add item.
   * @param  newMenuTitle   the menu title.
   * @return                the EquoMenu instance.
   */
  public EquoMenu appendMenu(String parentMenuPath, int indexToAddMenu, String newMenuTitle) {
    AbstractEquoMenu item = get(parentMenuPath);
    if (!(item instanceof EquoMenu)) {
      return null;
    }
    EquoMenu parent = (EquoMenu) item;
    return parent.addMenu(indexToAddMenu, newMenuTitle);
  }

  /**
   * Appends the menu at the end.
   * @param  parentMenuPath the path to add menu.
   * @param  newMenuTitle   the menu title.
   * @return                the EquoMenu instance.
   */
  public EquoMenu appendMenuAtTheEnd(String parentMenuPath, String newMenuTitle) {
    return appendMenu(parentMenuPath, -1, newMenuTitle);
  }

  /**
   * Removes the menu element by path. If the element exists, it will be removed.
   * @param pathElementToRemove the path to remove the element, separated by "/".
   *                            If the element it's under File -> New -> Project,
   *                            then the argument must be "File/New/Project"
   */
  public void removeMenuElementByPath(String pathElementToRemove) {
    AbstractEquoMenu item = get(pathElementToRemove);
    if (item == null) {
      return;
    }
    IEquoMenu parent = item.getParent();
    if (parent instanceof EquoMenu) {
      ((EquoMenu) parent).removeChildren(item);
    } else {
      menus.remove(item);
    }
  }

  private EquoMenu getExistingMenu(String title) {
    return menus.stream().filter(m -> m.getTitle().equals(title)).findFirst().orElse(null);
  }

  protected void addMenu(EquoMenu menu) {
    EquoMenu existingMenu = getExistingMenu(menu.getTitle());
    if (existingMenu == null) {
      menus.add(menu);
    }
  }

  void implement(MenuBuilder menuBuilder) {
    for (EquoMenu menu : menus) {
      menu.implement(menuBuilder);
    }
  }

  /**
   * Gets the menu element under the given path.
   * @param  path The path to get the element, separated by "/". If the element
   *              it's under File -> New -> Project, then the argument must be
   *              "File/New/Project"
   * @return      a menu instance if the path is correct. Void for any other case.
   */
  public AbstractEquoMenu get(String path) {
    String[] steps = path.split("/");
    AbstractEquoMenu currentItem = null;
    for (String item : steps) {
      if (currentItem == null) {
        currentItem = getExistingMenu(item);
      } else {
        if (currentItem instanceof EquoMenu) {
          currentItem = ((EquoMenu) currentItem).getItem(item);
        } else {
          return null;
        }
      }
      if (currentItem == null) { // Path wrong
        return null;
      }
    }
    return currentItem;
  }

  /**
   * Serializes the menu and its children recursively in a JsonObject.
   * @return the menu and its children recursively in a JsonObject.
   */
  public JsonObject serialize() {
    JsonArray jArr = new JsonArray();
    for (EquoMenu menu : menus) {
      jArr.add(menu.serialize());
    }

    JsonObject jOb = new JsonObject();
    jOb.add("menus", jArr);
    return jOb;
  }

  /**
   * Sets the current model as the application menu.
   */
  @Override
  public void setApplicationMenu() {
    final EquoApplicationBuilder currentBuilder = EquoApplicationBuilder.getCurrentBuilder();
    final OptionalViewBuilder optionalViewBuilder = currentBuilder.getOptionalViewBuilder();
    Display.getDefault().asyncExec(() -> {
      optionalViewBuilder.inicMainMenu();
      MenuBuilder menuBuilder = new MenuBuilder(optionalViewBuilder);
      menuBuilder.remove();
      implement(menuBuilder);
      MMenu mainMenu = optionalViewBuilder.getMainMenu();
      if (mainMenu == null) {
        mainMenu = currentBuilder.getmWindow().getMainMenu();
      }
      MenuManagerRenderer renderer = (MenuManagerRenderer) mainMenu.getRenderer();
      if (renderer != null) {
        renderer.getManager(mainMenu).update(true);
      }
    });
  }

  /**
   * Creates a new Menu instance.
   * @return a new Menu instance.
   */
  public static Menu create() {
    return new Menu();
  }

  /**
   * Gets the menu model that is currently shown.
   * @return the menu model that is currently shown.
   */
  public static Menu getActiveMenu() {
    Menu model = new Menu();
    EquoApplicationBuilder builder = EquoApplicationBuilder.getCurrentBuilder();
    if (builder != null) {
      OptionalViewBuilder optionalViewBuilder = builder.getOptionalViewBuilder();
      if (optionalViewBuilder != null) {
        MMenu menu = optionalViewBuilder.getMainMenu();
        if (menu != null) {
          for (MMenuElement children : menu.getChildren()) {
            AbstractEquoMenu subMenu = AbstractEquoMenu.getElement(model, children);
            if (subMenu instanceof EquoMenu) {
              model.addMenu((EquoMenu) subMenu);
            }
          }
        }
      }
    }
    return model;
  }
}
