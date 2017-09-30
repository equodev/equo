package com.make.equo.application.client.api;

import com.make.equo.application.api.ApplicationModelService;
import com.make.equo.application.client.api.EquoApplication.OptionalFieldBuilder;

public class MenuItemBuilder {

	private OptionalFieldBuilder optionalFieldBuilder;
	private ApplicationModelService applicationModelService;
	private String parentMenu;

	public MenuItemBuilder(String parentMenu, OptionalFieldBuilder optionalFieldBuilder, ApplicationModelService applicationModelService) {
		this.optionalFieldBuilder = optionalFieldBuilder;
		this.applicationModelService = applicationModelService;
		this.parentMenu = parentMenu;
	}

	public MenuItemBuilder addMenu(String label) {
		String id = applicationModelService.addMenu(parentMenu, label);
		return new MenuItemBuilder(id, optionalFieldBuilder, applicationModelService);
	}
	
	public HandlerBuilder addMenuItem(String label) {		
		String id = applicationModelService.addMenuItem(parentMenu, label);
		return new HandlerBuilder(id, this, applicationModelService);
	}
	
	public EquoApplication start() throws Exception {
		return optionalFieldBuilder.start();
	}
	
	public MenuBuilder withTooltip(String tooltip) {
		return null;
	}
}
