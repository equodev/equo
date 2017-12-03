package com.make.equo.application.model;

import java.util.List;

import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandParameter;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;

import com.google.common.collect.Lists;
import com.make.equo.application.impl.HandlerBuilder;
import com.make.equo.application.util.IConstants;

public class MenuItemHandlerBuilder extends HandlerBuilder {

	private MenuItemBuilder menuItemBuilder;
	private Runnable runnable;

	MenuItemHandlerBuilder(MenuItemBuilder menuItemBuilder) {
		super(menuItemBuilder.getMenuBuilder().getOptionalFieldBuilder().getEquoApplicationBuilder().getmApplication(), IConstants.COMMAND_ID_PARAMETER, IConstants.PARAMETERIZED_COMMAND_CONTRIBUTION_URI);
		this.menuItemBuilder = menuItemBuilder;
	}

	public MenuItemBuilder onClick(Runnable runnable) {
		this.runnable = runnable;
		MHandledMenuItem menuItem = this.menuItemBuilder.getMenuItem();
		
		String id = menuItem.getElementId();
		
		MCommand newCommand = createCommandAndHandler(id);
		
		menuItem.setCommand(newCommand);
		String commandId = newCommand.getElementId();
		MParameter parameter = createMParameter(IConstants.COMMAND_ID_PARAMETER, commandId);
		menuItem.getParameters().add(parameter);
	
		return this.menuItemBuilder;
	}
	
	protected List<MCommandParameter> createCommandParameters() {
		MCommandParameter windowNameCommandParameter = createCommandParameter(
				IConstants.EQUO_WEBSOCKET_USER_EMITTED_EVENT, "User emitted event", true);
		return Lists.newArrayList(windowNameCommandParameter);
	}
	
	public MenuBuilder withMainMenu(String menuLabel) {
		return new MenuBuilder(menuItemBuilder.getMenuBuilder().getOptionalFieldBuilder()).addMenu(menuLabel);
	}

	@Override
	protected Runnable getRunnable() {
		return runnable;
	}

	public MenuItemBuilder onClick(Runnable runnable, String userEvent) {
		onClick(runnable);
		MHandledMenuItem menuItem = this.menuItemBuilder.getMenuItem();
		MParameter parameter = createMParameter(IConstants.EQUO_WEBSOCKET_USER_EMITTED_EVENT, userEvent);
		menuItem.getParameters().add(parameter);
		return this.menuItemBuilder;
	}
	
	public MenuItemBuilder onClick(String userEvent) {
		MenuItemBuilder menuItemBuilder = onClick(null, userEvent);
		return menuItemBuilder;
	}
	
	public MenuItemBuilder addShortcut(String keySequence) {
		new MenuItemShortcutBuilder(this.menuItemBuilder).addShorcut(keySequence);
		EquoApplicationBuilder equoApplicationBuilder = this.menuItemBuilder.getMenuBuilder().getOptionalFieldBuilder().getEquoApplicationBuilder();
		new GlobalShortcutBuilder(equoApplicationBuilder, this.menuItemBuilder.getMenuItem().getElementId(), this.runnable).addGlobalShortcut(keySequence);
		return this.menuItemBuilder;
	}
	
}
