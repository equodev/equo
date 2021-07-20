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

import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuItem;

import com.equo.application.util.IConstants;
import com.google.gson.JsonObject;

/**
 * Equo menu. Represents a menu item model for building Menu from JavaScript
 * api.
 */
public class EquoMenuItem extends AbstractEquoMenu {
  private Runnable runnable = null;
  private String action = null;
  private String shortcut = null;
  public static final String CLASSNAME = "EquoMenuItem";

  EquoMenuItem(IEquoMenu parent, String title) {
    super(parent, title);
  }

  /**
   * Adds the runnable in menu element.
   * @param  runnable the runable action.
   * @return          the EquoMenuItem instance.
   */
  public EquoMenuItem onClick(Runnable runnable) {
    this.runnable = runnable;
    return this;
  }

  /**
   * Adds the action in menu element.
   * @param  action the string action.
   * @return        the EquoMenuItem instance.
   */
  public EquoMenuItem onClick(String action) {
    this.action = action;
    return this;
  }

  /**
   * Adds the shortcut in menu element.
   * @param  shortcut the shortcut command.
   * @return          the EquoMenuItem instance.
   */
  public EquoMenuItem withShortcut(String shortcut) {
    this.shortcut = shortcut;
    return this;
  }

  @Override
  void implement(MenuBuilder menuBuilder) {
    MenuItemBuilder itemBuilder = menuBuilder.addMenuItem(getTitle());
    if (runnable != null || action != null) {
      itemBuilder = itemBuilder.onClick(runnable, action);
      if (shortcut != null) {
        itemBuilder.addShortcut(shortcut);
      }
    }
    if (iconPath != null) {
      itemBuilder.addIcon(iconPath);
    }
  }

  /**
   * Adds an icon to a menu item.
   * @param  iconPath the icon relative path to resources folder.
   * @return          the EquoMenuItem instance.
   */
  public EquoMenuItem addIcon(String iconPath) {
    this.iconPath = iconPath;
    return this;
  }

  static AbstractEquoMenu getElement(IEquoMenu parent, MMenuElement element) {
    if (element instanceof MMenuItem) {
      MMenuItem menuItem = ((MMenuItem) element);

      String shortcut = null;
      Object shortcutObject = menuItem.getTransientData().get(IConstants.ITEM_SHORTCUT);
      if (shortcutObject != null) {
        shortcut = (String) shortcutObject;
      }

      Runnable runnable = null;
      Object runnableObject = menuItem.getTransientData().get(IConstants.ITEM_RUNNABLE);
      if (runnableObject != null) {
        runnable = (Runnable) runnableObject;
      }

      String action = null;
      Object actionObject = menuItem.getTransientData().get(IConstants.ITEM_ACTION);
      if (actionObject != null) {
        action = (String) actionObject;
      }

      EquoMenuItem item = new EquoMenuItem(parent, element.getLabel());
      item.onClick(runnable);
      item.withShortcut(shortcut);
      item.onClick(action);
      return item;
    } else {
      return EquoMenuItemSeparator.getElement(parent, element);
    }
  }

  @Override
  public JsonObject serialize() {
    JsonObject jOb = new JsonObject();
    jOb.addProperty("type", CLASSNAME);
    jOb.addProperty("title", getTitle());

    if (shortcut != null) {
      jOb.addProperty("shortcut", shortcut);
    }

    if (action != null) {
      jOb.addProperty("action", action);
    }
    
    if (iconPath != null) {
      jOb.addProperty("iconPath", iconPath);
    }
    return jOb;
  }

}
