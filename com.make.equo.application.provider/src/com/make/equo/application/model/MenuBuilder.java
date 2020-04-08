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
		if(parentMenu==null) {
			parentMenu = optionalFieldBuilder.getEquoApplicationBuilder().getmWindow().getMainMenu();
		}
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
	
	public MenuItemBuilder onBeforeExit(String label, Runnable runnable) {
		return new MenuItemBuilder(this).onBeforeExit(label, runnable);
	}
	
	public MenuItemBuilder onBeforeExit(Runnable runnable) {
		return new MenuItemBuilder(this).onBeforeExit(runnable);
	}
	
	public MenuItemBuilder onPreferences(String label, Runnable runnable) {
		return new MenuItemBuilder(this).onPreferences(label, runnable);
	}
	
	public MenuItemBuilder onPreferences(Runnable runnable) {
		return new MenuItemBuilder(this).onPreferences(runnable);
	}
	
	public MenuItemBuilder onAbout(String label, Runnable runnable) {
		return new MenuItemBuilder(this).onAbout(label, runnable);
	}
	
	public MenuItemBuilder onAbout(Runnable runnable) {
		return new MenuItemBuilder(this).onAbout(runnable);
	}
	
	public MenuItemBuilder addFullScreenModeMenuItem(String menuItemLabel) {
		return new MenuItemBuilder(this).addFullScreenModeMenuItem(menuItemLabel);
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
}
