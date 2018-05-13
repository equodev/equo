package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuFactoryImpl;

public class MenuBuilder {

	private OptionalViewBuilder optionalFieldBuilder;
	private MMenu parentMenu;
	private MMenu menu;

	MenuBuilder(OptionalViewBuilder optionalFieldBuilder) {
		this.parentMenu = optionalFieldBuilder.getMainMenu();
		this.optionalFieldBuilder = optionalFieldBuilder;
	}

	MenuBuilder(MenuBuilder menuBuilder) {
		this.parentMenu = menuBuilder.menu;
		this.menu = menuBuilder.menu;
		this.optionalFieldBuilder = menuBuilder.optionalFieldBuilder;
	}

	public MenuBuilder addMenu(String label) {
		menu = createMenu(parentMenu, label);
		return new MenuBuilder(this);
	}

	private MMenu createMenu(MMenu parentMenu, String menuLabel) {
		MMenu newMenu = MenuFactoryImpl.eINSTANCE.createMenu();
		newMenu.setElementId(parentMenu.getElementId() + "." + menuLabel.replaceAll("\\s+", "").toLowerCase());
		newMenu.setLabel(menuLabel);
		parentMenu.getChildren().add(newMenu);
		return newMenu;
	}

	public MenuItemBuilder addMenuItem(String label) {
		return new MenuItemBuilder(this).addMenuItem(label);
	}

	OptionalViewBuilder getOptionalFieldBuilder() {
		return optionalFieldBuilder;
	}

	MMenu getParentMenu() {
		return parentMenu;
	}

	MMenu getMenu() {
		return menu;
	}

	public MenuItemBuilder addFullScreenModeMenuItem(String menuItemLabel) {
		return new MenuItemBuilder(this).addFullScreenModeMenuItem(menuItemLabel);
	}

}
