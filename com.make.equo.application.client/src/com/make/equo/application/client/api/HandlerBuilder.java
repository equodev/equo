package com.make.equo.application.client.api;

import com.make.equo.application.api.ApplicationModelService;
import com.make.equo.application.model.IMenuHandler;

public class HandlerBuilder {

	private String handlerId;
	private MenuItemBuilder menuItemBuilder;
	private ApplicationModelService applicationModelService;

	public HandlerBuilder(String handlerId, MenuItemBuilder menuItemBuilder, ApplicationModelService applicationModelService) {
		this.handlerId = handlerId;
		this.menuItemBuilder = menuItemBuilder;
		this.applicationModelService = applicationModelService;
	}
	
	public MenuItemBuilder onClick(IMenuHandler menuHandler) {
		applicationModelService.addHandler(handlerId, menuHandler);
		return menuItemBuilder;
	}
	
}
