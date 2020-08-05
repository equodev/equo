package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;

import com.google.gson.JsonObject;

public class EquoMenuItemSeparator extends AbstractEquoMenu {
	public static final String CLASSNAME = "EquoMenuItemSeparator";

	EquoMenuItemSeparator(IEquoMenu parent, String title) {
		super(parent, title);
	}

	@Override
	void implement(MenuBuilder menuBuilder) {
		new MenuItemSeparatorBuilder(menuBuilder).addMenuItemSeparator();
	}

	static AbstractEquoMenu getElement(IEquoMenu parent, MMenuElement element) {
		return new EquoMenuItemSeparator(parent, "");
	}

	@Override
	public JsonObject serialize() {
		JsonObject jOb = new JsonObject();
		jOb.addProperty("type", CLASSNAME);
		return jOb;
	}

	@Override
	public EquoMenuItem addMenuItem(String title) {
		return ((AbstractEquoMenu) getParent()).addMenuItem(title);
	}

	@Override
	public EquoMenu addMenu(String title) {
		return ((AbstractEquoMenu) getParent()).addMenu(title);
	}

}
