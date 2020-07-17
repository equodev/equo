package com.make.equo.application.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class EquoMenuModel {
	private List<EquoMenu> menus = new ArrayList<>();

	public void addMenu(EquoMenu menu) {
		menus.add(menu);
	}

	void implement(MenuBuilder menuBuilder) {
		for (EquoMenu menu : menus) {
			menu.implement(menuBuilder);
		}
	}

	private EquoMenu getMenu(String menu) {
		for (EquoMenu m : menus) {
			if (m.getTitle().equals(menu))
				return m;
		}
		return null;
	}

	/**
	 * Gets the menu element under the given path
	 * 
	 * @param path The path to get the element, separated by "/". If the element
	 *             it's under File -> New -> Project, then the argument must be
	 *             "File/New/Project"
	 * @return
	 */
	public AbstractEquoMenu get(String path) {
		String[] steps = path.split("/"); // TODO: a way to escape
		AbstractEquoMenu currentItem = null;
		for (String item : steps) {
			if (currentItem == null) {
				currentItem = getMenu(item);
			} else {
				if (currentItem instanceof EquoMenu) {
					currentItem = ((EquoMenu) currentItem).getItem(item);
				} else {
					return null;
				}
			}
			if (currentItem == null) { // Path wrong
				return null;
			}
		}
		return currentItem;
	}

	/**
	 * Gets the menu model that is currently shown
	 * 
	 * @return
	 */
	public static EquoMenuModel getActiveModel() {
		EquoMenuModel model = new EquoMenuModel();
		EquoApplicationBuilder builder = EquoApplicationBuilder.getCurrentBuilder();
		if (builder != null) {
			OptionalViewBuilder optionalViewBuilder = builder.getOptionalViewBuilder();
			if (optionalViewBuilder != null) {
				MMenu menu = optionalViewBuilder.getMainMenu();
				if (menu != null) {
					for (MMenuElement children : menu.getChildren()) {
						AbstractEquoMenu subMenu = AbstractEquoMenu.getElement(children);
						if (subMenu instanceof EquoMenu) {
							model.addMenu((EquoMenu) subMenu);
						}
					}
				}
			}
		}
		return model;
	}
	
	public JsonObject serialize() {
		JsonArray jArr = new JsonArray();
		for(EquoMenu menu: menus) {
			jArr.add(menu.serialize());
		}
		
		JsonObject jOb = new JsonObject();
		jOb.add("menus", jArr);
		return jOb;
	}
}
