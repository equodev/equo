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

	EquoMenu(IEquoMenu parent, String title) {
		super(parent, title);
		children = new ArrayList<>();
	}

	EquoMenu addItem(AbstractEquoMenu item) {
		children.add(item);
		return this;
	}

	private AbstractEquoMenu getExistingChildren(String title) {
		return children.stream().filter(ch -> ch.getTitle().equals(title)).findFirst().orElseGet(null);
	}

	@Override
	public EquoMenuItem addMenuItem(String title) {
		AbstractEquoMenu item = getExistingChildren(title);
		if (item != null) {
			if (item instanceof EquoMenuItem) {
				return (EquoMenuItem) item;
			}
			return null;
		}
		EquoMenuItem newItem = new EquoMenuItem(this, title);
		children.add(newItem);
		return newItem;
	}
	
	@Override
	public EquoMenu addMenu(String title) {
		AbstractEquoMenu item = getExistingChildren(title);
		if (item != null) {
			if (item instanceof EquoMenu) {
				return (EquoMenu) item;
			}
			return null;
		}
		EquoMenu newMenu = new EquoMenu(this, title);
		children.add(newMenu);
		return newMenu;
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

	static AbstractEquoMenu getElement(IEquoMenu parent, MMenuElement element) {
		if (element instanceof MMenu) {
			EquoMenu menu = new EquoMenu(parent, element.getLabel());
			for (MMenuElement children : ((MMenu) element).getChildren()) {
				menu.addItem(AbstractEquoMenu.getElement(menu, children));
			}
			return menu;
		} else {
			return EquoMenuItem.getElement(parent, element);
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

	@Override
	public EquoMenuItemSeparator addMenuItemSeparator() {
		EquoMenuItemSeparator separator = new EquoMenuItemSeparator(this);
		children.add(separator);
		return separator;
	}
	
}
