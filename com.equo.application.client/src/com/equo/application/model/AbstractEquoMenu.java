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

import com.google.gson.JsonObject;

/**
 * Abstract Equo menu. Represents a generic menu model for building Menu from
 * JavaScript api.
 */
public abstract class AbstractEquoMenu implements IEquoMenu {
  private String title;
  private IEquoMenu parent;
  protected String iconPath = null;

  AbstractEquoMenu(IEquoMenu parent, String title) {
    this.parent = parent;
    setTitle(title);
  }

  protected IEquoMenu getParent() {
    return parent;
  }

  /**
   * Sets the title for menu.
   * @param title the title for menu.
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Implements the construction in a ui model.
   * @param menuBuilder menu builder in which to implement the current element
   */
  abstract void implement(MenuBuilder menuBuilder);

  /**
   * Gets the menu title.
   * @return the menu title.
   */
  public String getTitle() {
    return this.title;
  }
  
  /**
   * Gets the menu icon path.
   * @return the menu icon path.
   */
  public String getIconPath() {
    return this.title;
  }

  /**
   * Gets an element of AbstractEquoMenu that represents an existing element of
   * the Eclipse menu model.
   * @param  element the element of the Eclipse model to be represented.
   * @return         an element of AbstractEquoMenu that represents an existing
   *                 element of the Eclipse menu model.
   */
  static AbstractEquoMenu getElement(IEquoMenu parent, MMenuElement element) {
    return EquoMenu.getElement(parent, element);
  }

  /**
   * Serializes the menu and its children recursively in a JsonObject.
   * @return the menu and its children recursively in a JsonObject.
   */
  public abstract JsonObject serialize();

  @Override
  public EquoMenu withMainMenu(String title) {
    return getParent().withMainMenu(title);
  }

  /**
   * Adds a new menu that will not contain other menus.
   * @param  title the menu title.
   * @return       a new menu that will not contain other menus.
   */
  public EquoMenuItem addMenuItem(String title) {
    return ((AbstractEquoMenu) getParent()).addMenuItem(title);
  }

  /**
   * Adds a new menu that will not contain other menus.
   * @param  title the menu title.
   * @return       a new menu that will contain other menus.
   */
  public EquoMenu addMenu(String title) {
    return ((AbstractEquoMenu) getParent()).addMenu(title);
  }

  /**
   * Adds a separator between menus.
   * @return a EquoMenuItemSeparator instance.
   */
  public EquoMenuItemSeparator addMenuItemSeparator() {
    return ((AbstractEquoMenu) getParent()).addMenuItemSeparator();
  }

  @Override
  public void setApplicationMenu() {
    getParent().setApplicationMenu();
  }

}
