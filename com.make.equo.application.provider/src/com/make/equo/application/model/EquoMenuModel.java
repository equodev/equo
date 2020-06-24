package com.make.equo.application.model;

import java.util.ArrayList;
import java.util.List;

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
}
