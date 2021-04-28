package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuSeparator;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuFactoryImpl;

/**
 * 
 * Equo menu item separator builder for Java.
 *
 */
public class MenuItemSeparatorBuilder {

	private MenuBuilder menuBuilder;

	MenuItemSeparatorBuilder(MenuBuilder menuBuilder) {
		this.menuBuilder = menuBuilder;
	}

	/**
	 * Adds a separator between menus.
	 * 
	 * @return this.
	 */
	public MenuItemSeparatorBuilder addMenuItemSeparator() {
		MMenuSeparator menuSeparator = MenuFactoryImpl.eINSTANCE.createMenuSeparator();
		MMenu parentMenu = menuBuilder.getMenu();
		parentMenu.getChildren().add(menuSeparator);
		return this;
	}

	/**
	 * Adds a new menu that will contain other menus.
	 * 
	 * @param menuLabel the menu title.
	 * @return the MenuBuilder instance.
	 */
	public MenuBuilder addMenu(String menuLabel) {
		return new MenuBuilder(this.menuBuilder).addMenu(menuLabel);
	}

	/**
	 * Adds a new menu item that will not contain other menus.
	 * 
	 * @param menuItemLabel the menu title.
	 * @return the MenuItemBuilder instance.
	 */
	public MenuItemBuilder addMenuItem(String menuItemLabel) {
		return new MenuBuilder(this.menuBuilder).addMenuItem(menuItemLabel);
	}

	/**
	 * Adds new menu item with full screen mode.
	 * 
	 * @param menuItemLabel the menu title.
	 * @return the MenuItemBuilder instance.
	 */
	public MenuItemBuilder addFullScreenModeMenuItem(String menuItemLabel) {
		return new MenuBuilder(this.menuBuilder).addFullScreenModeMenuItem(menuItemLabel);
	}
}
