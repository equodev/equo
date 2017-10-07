package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuSeparator;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuFactoryImpl;

public class MenuItemSeparatorBuilder {

	private MenuBuilder menuBuilder;

	public MenuItemSeparatorBuilder(MenuBuilder menuBuilder) {
		this.menuBuilder = menuBuilder;
		
	}

	public MenuItemSeparatorBuilder addMenuItemSeparator() {
		MMenuSeparator menuSeparator = MenuFactoryImpl.eINSTANCE.createMenuSeparator();
		MMenu parentMenu = menuBuilder.getMenu();
		parentMenu.getChildren().add(menuSeparator);
		return this;
	}
	
	public MenuBuilder addMenu(String menuLabel) {
		return new MenuBuilder(this.menuBuilder).addMenu(menuLabel);
	} 
	
	public MenuItemBuilder addMenuItem(String menuItemLabel) {
		return new MenuBuilder(this.menuBuilder).addMenuItem(menuItemLabel);
	}
}
