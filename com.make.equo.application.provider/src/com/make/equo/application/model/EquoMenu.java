package com.make.equo.application.model;

import java.util.ArrayList;
import java.util.List;

public class EquoMenu extends AbstractEquoMenu {
	private List<AbstractEquoMenu> children;
	
	public EquoMenu(String title) {
		setTitle(title);
		children = new ArrayList<>();
	}
	
	public EquoMenu addItem(AbstractEquoMenu item) {
		children.add(item);
		return this;
	}

	@Override
	void implement(MenuBuilder menuBuilder) {
		MenuBuilder menu = menuBuilder.addMenu(getTitle());
		for (AbstractEquoMenu item: children) {
			item.implement(menu);
		}
	}
	
}
