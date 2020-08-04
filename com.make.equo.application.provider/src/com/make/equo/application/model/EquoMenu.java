package com.make.equo.application.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class EquoMenu extends AbstractEquoMenu {
	private List<AbstractEquoMenu> children;
	public static final String CLASSNAME = "EquoMenu";

	EquoMenu(String title) {
		setTitle(title);
		children = new ArrayList<>();
	}

	public EquoMenu addItem(AbstractEquoMenu item) {
		children.add(item);
		return this;
	}

	public AbstractEquoMenu getItem(String itemTitle) {
		for (AbstractEquoMenu item : children) {
			if (item.getTitle().equals(itemTitle))
				return item;
		}
		return null;
	}

	@Override
	void implement(MenuBuilder menuBuilder) {
		MenuBuilder menu = menuBuilder.addMenu(getTitle());
		for (AbstractEquoMenu item : children) {
			item.implement(menu);
		}
	}

	static AbstractEquoMenu getElement(MMenuElement element) {
		if (element instanceof MMenu) {
			EquoMenu menu = new EquoMenu(element.getLabel());
			for (MMenuElement children : ((MMenu) element).getChildren()) {
				menu.addItem(AbstractEquoMenu.getElement(children));
			}
			return menu;
		} else {
			return EquoMenuItem.getElement(element);
		}
	}


	@Override
	public JsonObject serialize() {
		JsonArray jArr = new JsonArray();
		for(AbstractEquoMenu menu: children){
			jArr.add(menu.serialize());
		}
		
		JsonObject jOb = new JsonObject();
		jOb.addProperty("type", CLASSNAME);
		jOb.addProperty("title", getTitle());
		jOb.add("children", jArr);
		return jOb;
	}
	
}
