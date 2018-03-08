package com.make.equo.application.model;

import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.ui.menu.MMenu;

import com.make.equo.server.api.IEquoServer;

public class OptionalFieldBuilder {

	private EquoApplicationBuilder equoApplicationBuilder;
	private MMenu mainMenu;

	@Inject
	private IEquoServer equoServer;

	OptionalFieldBuilder(EquoApplicationBuilder equoApplicationBuilder) {
		this.equoApplicationBuilder = equoApplicationBuilder;
	}

	public EquoApplication start() {
		equoServer.startServer();
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