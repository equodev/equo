package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;

import com.make.equo.application.util.IConstants;

public class MenuItemHandlerBuilder extends HandlerBuilder {

	private MenuItemBuilder menuItemBuilder;
	private Runnable runnable;

	public MenuItemHandlerBuilder(MenuItemBuilder menuItemBuilder) {
		super(menuItemBuilder.menuBuilder.optionalFieldBuilder.equoApplicationBuilder);
		this.menuItemBuilder = menuItemBuilder;
	}

	public MenuItemHandlerBuilder onClick(Runnable runnable) {
		this.runnable = runnable;
		MHandledMenuItem menuItem = this.menuItemBuilder.menuItem;
		
		String id = menuItemBuilder.menuItem.getElementId();
		
		MCommand newCommand = createCommandAndHandler(id);
		
		menuItem.setCommand(newCommand);
		String commandId = newCommand.getElementId();
		MParameter parameter = createMParameter(IConstants.COMMAND_ID_PARAMETER, commandId);
		menuItem.getParameters().add(parameter);
	
		return this;
	}
	
	public MenuItemBuilder addShorcut(String shortcut) {
		new MenuItemShortcutBuilder(this.menuItemBuilder).addShorcut(shortcut);
		new GlobalShortcutBuilder(this.getEquoApplicationBuilder(), this.menuItemBuilder.menuItem.getElementId(), this.getRunnable()).addGlobalShortcut(shortcut);
		return this.menuItemBuilder;
	}

	@Override
	Runnable getRunnable() {
		return runnable;
	}
}
