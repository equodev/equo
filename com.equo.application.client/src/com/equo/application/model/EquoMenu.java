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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Equo menu. Represents a menu model.
 */
public class EquoMenu extends AbstractEquoMenu {
  private List<AbstractEquoMenu> children;
  public static final String CLASSNAME = "EquoMenu";
  private String iconPath = null;

  EquoMenu(IEquoMenu parent, String title) {
    super(parent, title);
    children = new ArrayList<>();
  }

  EquoMenu addItem(AbstractEquoMenu item) {
    children.add(item);
    return this;
  }

  private AbstractEquoMenu getExistingChildren(String title) {
    return children.stream().filter(ch -> ch.getTitle().equals(title)).findFirst().orElse(null);
  }

  EquoMenuItem addMenuItem(int index, String title) {
    AbstractEquoMenu item = getExistingChildren(title);
    if (item != null) {
      if (item instanceof EquoMenuItem) {
        return (EquoMenuItem) item;
      }
      return null;
    }
    EquoMenuItem newItem = new EquoMenuItem(this, title);
    if (index >= 0) {
      children.add(index, newItem);
    } else {
      children.add(newItem);
    }
    return newItem;
  }

  @Override
  public EquoMenuItem addMenuItem(String title) {
    return addMenuItem(-1, title);
  }

  public EquoMenu addIcon(String iconPath) {
    this.iconPath = iconPath;
    return this;
  }
  
  EquoMenu addMenu(int index, String title) {
    AbstractEquoMenu item = getExistingChildren(title);
    if (item != null) {
      if (item instanceof EquoMenu) {
        return (EquoMenu) item;
      }
      return null;
    }
    EquoMenu newMenu = new EquoMenu(this, title);
    if (index >= 0) {
      children.add(index, newMenu);
    } else {
      children.add(newMenu);
    }
    return newMenu;
  }

  @Override
  public EquoMenu addMenu(String title) {
    return addMenu(-1, title);
  }

  @Override
  public EquoMenuItemSeparator addMenuItemSeparator() {
    EquoMenuItemSeparator separator = new EquoMenuItemSeparator(this);
    children.add(separator);
    return separator;
  }

  /**
   * Gets a menu by the title.
   * @param  itemTitle the title menu.
   * @return           a menu by the title. If it does not exist, it returns null.
   */
  public AbstractEquoMenu getItem(String itemTitle) {
    for (AbstractEquoMenu item : children) {
      if (item.getTitle().equals(itemTitle)) {
        return item;
      }
    }
    return null;
  }

  void removeChildren(AbstractEquoMenu element) {
    children.remove(element);
  }

  @Override
  void implement(MenuBuilder menuBuilder) {
    MenuBuilder menu = menuBuilder.addMenu(getTitle());
    for (AbstractEquoMenu item : children) {
      item.implement(menu);
    }
    if (iconPath != null) {
      menu.addIcon(iconPath);
    }
  }

  static AbstractEquoMenu getElement(IEquoMenu parent, MMenuElement element) {
    if (element instanceof MMenu) {
      EquoMenu menu = new EquoMenu(parent, element.getLabel());
      for (MMenuElement children : ((MMenu) element).getChildren()) {
        menu.addItem(AbstractEquoMenu.getElement(menu, children));
      }
      return menu;
    } else {
      return EquoMenuItem.getElement(parent, element);
    }
  }

  @Override
  public JsonObject serialize() {
    JsonArray jArr = new JsonArray();
    for (AbstractEquoMenu menu : children) {
      jArr.add(menu.serialize());
    }

    JsonObject jOb = new JsonObject();
    jOb.addProperty("type", CLASSNAME);
    jOb.addProperty("title", getTitle());
    jOb.add("children", jArr);
    return jOb;
  }

}
