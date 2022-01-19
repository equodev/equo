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

import java.math.BigInteger;
import java.security.SecureRandom;

import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;

import com.google.gson.JsonObject;

/**
 * Menu item separator. Displayed in menus as a line separating group of menu
 * items.
 */
public class EquoMenuItemSeparator extends AbstractEquoMenu {
  public static final String CLASSNAME = "EquoMenuItemSeparator";

  EquoMenuItemSeparator(IEquoMenu parent) {
    super(parent, EquoMenuItemSeparator.getNextRandomString());
  }

  protected static final SecureRandom secureRandom = new SecureRandom();

  protected static String getNextRandomString() {
    BigInteger bInt = new BigInteger(130, secureRandom);
    return bInt.toString(32);
  }

  @Override
  void implement(MenuBuilder menuBuilder) {
    new MenuItemSeparatorBuilder(menuBuilder).addMenuItemSeparator();
  }

  static AbstractEquoMenu getElement(IEquoMenu parent, MMenuElement element) {
    return new EquoMenuItemSeparator(parent);
  }

  @Override
  public JsonObject serialize() {
    JsonObject jOb = new JsonObject();
    jOb.addProperty("type", CLASSNAME);
    return jOb;
  }

}
