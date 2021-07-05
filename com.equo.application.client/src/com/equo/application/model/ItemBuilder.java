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

import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;

import com.equo.application.util.IConstants;

/**
 * Abstract menu item builder.
 */
public abstract class ItemBuilder {

  private ItemHandlerBuilder itemHandlerBuilder;
  private MHandledItem item;
  private OptionalViewBuilder viewBuilder;

  /**
   * Creates a new instance.
   */
  public ItemBuilder(OptionalViewBuilder viewBuilder) {
    this.viewBuilder = viewBuilder;
  }

  /**
   * Creates a root menu, visible in the bar.
   * @param  menuLabel the menu title.
   * @return           the MenuBuilder instance.
   */
  public MenuBuilder withMainMenu(String menuLabel) {
    return new MenuBuilder(this.getOptionalFieldBuilder()).addMenu(menuLabel);
  }

  /**
   * Adds a toolbar to current builder instance.
   * @return the ToolbarBuilder instance.
   */
  public ToolbarBuilder withToolbar() {
    return new ToolbarBuilder(this.getOptionalFieldBuilder(),
        this.getOptionalFieldBuilder().getEquoApplicationBuilder().getmWindow()).addToolbar();
  }

  /**
   * Adds the shortcut in item builder.
   * @param  keySequence the shortcut command.
   * @return             this.
   */
  public ItemBuilder addShortcut(String keySequence) {
    if (getItemHandlerBuilder() != null) {
      return getItemHandlerBuilder().addShortcut(keySequence);
    }
    // log that there is no menu item handler -> no onClick method was called.
    return this;
  }

  /**
   * Starts Equo application.
   * @return the EquoApplicationBuilder instance.
   */
  public EquoApplicationBuilder start() {
    return this.getOptionalFieldBuilder().start();
  }

  protected ItemHandlerBuilder getItemHandlerBuilder() {
    return itemHandlerBuilder;
  }

  protected void setItemHandlerBuilder(ItemHandlerBuilder itemHandlerBuilder) {
    this.itemHandlerBuilder = itemHandlerBuilder;
  }

  MHandledItem getItem() {
    return this.item;
  }

  protected void setItem(MHandledItem item) {
    this.item = item;
  }

  OptionalViewBuilder getOptionalFieldBuilder() {
    return this.viewBuilder;
  }

  /**
   * Sets the action to be executed when the item gets clicked.
   * @param  runnable runnable to be executed.
   * @param  action   name of the comm event to be executed.
   * @return          the ItemBuilder instance.
   */
  public ItemBuilder onClick(Runnable runnable, String action) {
    this.setItemHandlerBuilder(new ItemHandlerBuilder(this));
    this.getItem().getTransientData().put(IConstants.IS_AN_EQUO_MODEL_ELEMENT, true);
    this.getItem().getTransientData().put(IConstants.EQUO_COMM_USER_EMITTED_EVENT, action);
    ItemBuilder itemBuilder = getItemHandlerBuilder().onClick(runnable, action);
    return itemBuilder;
  }

}
