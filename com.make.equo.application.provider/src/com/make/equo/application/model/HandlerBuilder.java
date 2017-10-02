package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MHandler;
import org.eclipse.e4.ui.model.application.commands.impl.CommandsFactoryImpl;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;

import com.make.equo.application.util.IConstants;

public class HandlerBuilder {

	private MenuItemBuilder menuItemBuilder;

	public HandlerBuilder(MenuItemBuilder menuItemBuilder) {
		this.menuItemBuilder = menuItemBuilder;
	}

	public MenuItemBuilder onClick(Runnable runnable) {
		MCommand command = addHandler(runnable);
		MHandledMenuItem menuItem = this.menuItemBuilder.menuItem;
		menuItem.setCommand(command);
		menuItem.getTransientData().put(IConstants.RUNNABLE_OBJECT, runnable);
		return new MenuItemBuilder(menuItemBuilder);
	}

	private MCommand addHandler(Runnable runnable) {
		MCommand command = createNewCommand();
		MHandler newHandler = CommandsFactoryImpl.eINSTANCE.createHandler();
		newHandler.setElementId(menuItemBuilder.menuItem.getElementId() + IConstants.HANDLER_SUFFIX);
		newHandler.setCommand(command);
		newHandler.setContributionURI(
				"bundleclass://com.make.equo.application.provider/com.make.equo.application.handlers.DefaultHandler");
		newHandler.getTransientData().put(IConstants.DEFAULT_HANDLER_IMPL_ID, runnable);
		
		MApplication mApplication = this.menuItemBuilder.menuBuilder.optionalFieldBuilder.equoApplicationBuilder.mApplication;
		mApplication.getCommands().add(command);
		mApplication.getHandlers().add(newHandler);
		return command;
	}

	private MCommand createNewCommand() {
		MCommand newCommand = CommandsFactoryImpl.eINSTANCE.createCommand();
		newCommand.setElementId(menuItemBuilder.menuItem.getElementId() + IConstants.COMMAND_SUFFIX);
		newCommand.setCommandName(menuItemBuilder.menuItem.getElementId() + IConstants.COMMAND_SUFFIX);
		return newCommand;
	}

}
