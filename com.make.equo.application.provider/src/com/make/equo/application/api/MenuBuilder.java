package com.make.equo.application.client.api;

import com.make.equo.application.api.ApplicationModelService;
import com.make.equo.application.client.api.EquoApplication.OptionalFieldBuilder;

public class MenuBuilder {

	private OptionalFieldBuilder optionalFieldBuilder;
	private ApplicationModelService applicationModelService;
	private String parentId;
	
	public MenuBuilder(OptionalFieldBuilder optionalFieldBuilder, ApplicationModelService applicationModelService) {
		this.optionalFieldBuilder = optionalFieldBuilder;
		this.applicationModelService = applicationModelService;
	}

	public MenuBuilder(String menuLabel, OptionalFieldBuilder optionalFieldBuilder,
			ApplicationModelService applicationModelService) {
		this.parentId = menuLabel;
		this.optionalFieldBuilder = optionalFieldBuilder;
		this.applicationModelService = applicationModelService;
	}

	public HandlerBuilder addMenuItem(String label) {
		return new MenuItemBuilder(parentId, optionalFieldBuilder, applicationModelService).addMenuItem(label);
	}
	
	public MenuItemBuilder addMenu(String label) {
//		applicationModelService.addMenu(menuLabel, label);
		return new MenuItemBuilder(parentId, optionalFieldBuilder, applicationModelService).addMenu(label);
//		return new MenuBuilder(label, optionalFieldBuilder, applicationModelService)
	}
	
	public MenuBuilder withMainMenu(String label) {
		String menuId = applicationModelService.addMenu(label);
		return new MenuBuilder(menuId, optionalFieldBuilder, applicationModelService);
	}
	
//	public EquoApplication start() throws Exception {
//		return optionalFieldBuilder.start();
//	}
}
