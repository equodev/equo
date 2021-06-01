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

import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimBar;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuFactoryImpl;

/**
 * Builder for app toolbars.
 */
public class ToolbarBuilder {

  private OptionalViewBuilder optionalFieldBuilder;
  private MToolBar toolbar;
  private MTrimmedWindow parent;

  ToolbarBuilder(OptionalViewBuilder optionalFieldBuilder, MTrimmedWindow window) {
    this.parent = window;
    this.optionalFieldBuilder = optionalFieldBuilder;
  }

  ToolbarBuilder(ToolbarBuilder toolbarBuilder) {
    this.toolbar = toolbarBuilder.toolbar;
    this.parent = toolbarBuilder.parent;
    this.optionalFieldBuilder = toolbarBuilder.optionalFieldBuilder;
  }

  public ToolbarBuilder addToolbar() {
    this.toolbar = createToolbar();
    return new ToolbarBuilder(this);
  }

  private MToolBar createToolbar() {
    MToolBar newToolbar = MenuFactoryImpl.eINSTANCE.createToolBar();
    newToolbar.setOnTop(true);
    newToolbar.setVisible(true);
    newToolbar.setElementId("com.equo.main.toolbar");
    MTrimBar trimbar = MBasicFactory.INSTANCE.createTrimBar();
    this.parent.getTrimBars().add(trimbar);
    this.parent.getTrimBars().get(0).getChildren().add(newToolbar);
    return newToolbar;
  }

  public ToolbarItemBuilder addToolItem(String iconId, String text) {
    return new ToolbarItemBuilder(this).addToolItem(iconId, text);
  }

  OptionalViewBuilder getOptionalFieldBuilder() {
    return optionalFieldBuilder;
  }

  MTrimmedWindow getParent() {
    return parent;
  }

  MToolBar getToolbar() {
    return toolbar;
  }

}
