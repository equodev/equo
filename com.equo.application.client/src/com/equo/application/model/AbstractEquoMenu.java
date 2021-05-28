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
