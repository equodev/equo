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
