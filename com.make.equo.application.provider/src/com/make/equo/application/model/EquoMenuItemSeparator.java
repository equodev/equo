package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;

import com.google.gson.JsonObject;

public class EquoMenuItemSeparator extends AbstractEquoMenu {
	public static final String CLASSNAME = "EquoMenuItemSeparator";

	@Override
	void implement(MenuBuilder menuBuilder) {
		new MenuItemSeparatorBuilder(menuBuilder).addMenuItemSeparator();
	}

	static AbstractEquoMenu getElement(MMenuElement element) {
		return new EquoMenuItemSeparator();
	}

	@Override
	public JsonObject serialize() {
		JsonObject jOb = new JsonObject();
		jOb.addProperty("type", CLASSNAME);
		return jOb;
	}

}
