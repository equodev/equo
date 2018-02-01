package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;

import com.make.equo.application.util.ICommandConstants;

public class ExitMenuItemHandlerBuilder {

	private MenuItemBuilder menuItemBuilder;

	public ExitMenuItemHandlerBuilder(MenuItemBuilder menuItemBuilder) {
		this.menuItemBuilder = menuItemBuilder;
	}

	public MenuItemBuilder onExit(Runnable runnable) {
		MApplication mApplication = menuItemBuilder.getMenuBuilder().getOptionalFieldBuilder()
				.getEquoApplicationBuilder().getmApplication();
		MCommand command = mApplication.getCommand(ICommandConstants.EXIT_COMMAND);

		MHandledMenuItem menuItem = this.menuItemBuilder.getMenuItem();
		menuItem.setCommand(command);

		mApplication.getTransientData().put(ICommandConstants.EXIT_COMMAND, runnable);
		return menuItemBuilder;
	}

}
