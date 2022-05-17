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

import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuFactoryImpl;

/**
 * Builder for toolbar items.
 */
public class ToolbarItemBuilder extends ItemBuilder {

  private ToolbarBuilder toolbarBuilder;

  ToolbarItemBuilder(ToolbarBuilder toolbarBuilder) {
    super(toolbarBuilder.getOptionalFieldBuilder());
    this.toolbarBuilder = toolbarBuilder;
  }

  ToolbarItemBuilder(OptionalViewBuilder optionalFieldBuilder, MHandledItem item,
      ToolbarBuilder toolbarBuilder) {
    super(optionalFieldBuilder);
    this.setItem(item);
    this.toolbarBuilder = toolbarBuilder;
  }

  /**
   * Adds a new item.
   * @param iconId a URL to the item icon.
   * @param text the text of the item.
   */
  public ToolbarItemBuilder addToolItem(String iconId, String text) {

    return new ToolbarItemBuilder(this.getOptionalFieldBuilder(), createToolItem(iconId, text),
        this.toolbarBuilder);
  }

  private MHandledToolItem createToolItem(String iconId, String text) {
    MHandledToolItem newToolItem = MenuFactoryImpl.eINSTANCE.createHandledToolItem();
    newToolItem.setIconURI(iconId);
    String itemId = toolbarBuilder.getToolbar().getElementId() + "."
        + text.replace(" ", "_").replaceAll("\\s+", "").toLowerCase();
    newToolItem.setElementId(itemId);
    newToolItem.setTooltip(text);
    newToolItem.setVisible(true);
    toolbarBuilder.getToolbar().getChildren().add(newToolItem);
    return newToolItem;
  }

  public ToolbarItemBuilder onClick(Runnable runnable) {
    return onClick(runnable, null);
  }

  public ToolbarItemBuilder onClick(Runnable runnable, String action) {
    return (ToolbarItemBuilder) super.onClick(runnable, action);
  }

  public ToolbarItemBuilder onClick(String action) {
    return onClick(null, action);
  }

  @Override
  public ToolbarItemBuilder withShortcut(String keySequence) {
    return (ToolbarItemBuilder) super.withShortcut(keySequence);
  }

  public ToolbarBuilder addToolbar() {
    return new ToolbarBuilder(this.toolbarBuilder).addToolbar();
  }

  public ToolbarBuilder getToolbarBuilder() {
    return toolbarBuilder;
  }
}
