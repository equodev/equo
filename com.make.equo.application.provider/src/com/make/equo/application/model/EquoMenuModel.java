package com.make.equo.application.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;

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
				for (MMenuElement children : menu.getChildren()) {
					AbstractEquoMenu subMenu = AbstractEquoMenu.getElement(children);
					if (subMenu instanceof EquoMenu) {
						model.addMenu((EquoMenu) subMenu);
					}
				}
			}
		}
		return model;
	}
}
