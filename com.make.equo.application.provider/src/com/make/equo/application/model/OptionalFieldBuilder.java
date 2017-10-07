package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.ui.menu.MMenu;

public class OptionalFieldBuilder {

	private EquoApplicationBuilder equoApplicationBuilder;
	private MMenu mainMenu;

	OptionalFieldBuilder(EquoApplicationBuilder equoApplicationBuilder) {
		this.equoApplicationBuilder = equoApplicationBuilder;
	}

	public EquoApplication start() {
		return equoApplicationBuilder.getEquoApplication();
	}

	public MenuBuilder withMainMenu(String menuLabel) {
		mainMenu = equoApplicationBuilder.getmWindow().getMainMenu();
		return new MenuBuilder(this).addMenu(menuLabel);
	}

	EquoApplicationBuilder getEquoApplicationBuilder() {
		return equoApplicationBuilder;
	}

	MMenu getMainMenu() {
		return mainMenu;
	}
}