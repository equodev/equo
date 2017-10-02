package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.ui.menu.MMenu;

public final class OptionalFieldBuilder {

	EquoApplicationBuilder equoApplicationBuilder;
	MMenu mainMenu;

	OptionalFieldBuilder(EquoApplicationBuilder equoApplicationBuilder) {
		this.equoApplicationBuilder = equoApplicationBuilder;
	}

	public EquoApplication start() {
		return equoApplicationBuilder.equoApplication;
	}

	public MenuBuilder withMainMenu(String menuLabel) {
		mainMenu = equoApplicationBuilder.mWindow.getMainMenu();
		return new MenuBuilder(this).addMenu(menuLabel);
	}
}