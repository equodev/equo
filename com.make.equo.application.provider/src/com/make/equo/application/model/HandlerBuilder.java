package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandParameter;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.commands.MHandler;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.commands.impl.CommandsFactoryImpl;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;

import com.make.equo.application.util.IConstants;

public class HandlerBuilder {

	private MenuItemBuilder menuItemBuilder;
	private Runnable runnable;
	MParameter parameter;

	public HandlerBuilder(MenuItemBuilder menuItemBuilder) {
		this.menuItemBuilder = menuItemBuilder;
	}

	public HandlerBuilder onClick(Runnable runnable) {
		this.runnable = runnable;
		MHandledMenuItem menuItem = this.menuItemBuilder.menuItem;
		
		MCommand command = createNewCommand();
		MHandler handler = createNewHandler();
		
		handler.setCommand(command);
		
		menuItem.setCommand(command);
		String commandId = command.getElementId();
		parameter = createMParameter(IConstants.COMMAND_ID_PARAMETER, commandId);
		menuItem.getParameters().add(parameter);
		
		MApplication mApplication = this.menuItemBuilder.menuBuilder.optionalFieldBuilder.equoApplicationBuilder.mApplication;
		mApplication.getCommands().add(command);
		mApplication.getHandlers().add(handler);
		
		mApplication.getTransientData().put(commandId, runnable);
//		mApplication.getTransientData().put(IConstants.RUNNABLE_OBJECT, runnable);
		return this;
	}

	private MParameter createMParameter(String id, String value) {
		MParameter parameter = MCommandsFactory.INSTANCE.createParameter();
		// set the identifier for the corresponding command parameter
		parameter.setElementId(id);
		parameter.setName(id);
		parameter.setValue(value);
		return parameter;
	}

	private MHandler createNewHandler() {
		MHandler newHandler = CommandsFactoryImpl.eINSTANCE.createHandler();
		newHandler.setElementId(menuItemBuilder.menuItem.getElementId() + IConstants.HANDLER_SUFFIX);
		newHandler.setContributionURI(
				"bundleclass://com.make.equo.application.provider/com.make.equo.application.handlers.ParameterizedCommandHandler");
		return newHandler;
	}

	private MCommand createNewCommand() {
		MCommand newCommand = CommandsFactoryImpl.eINSTANCE.createCommand();
		newCommand.setElementId(menuItemBuilder.menuItem.getElementId() + IConstants.COMMAND_SUFFIX);
		newCommand.setCommandName(menuItemBuilder.menuItem.getElementId() + IConstants.COMMAND_SUFFIX);
		MCommandParameter commandId = MCommandsFactory.INSTANCE.createCommandParameter();
		commandId.setElementId(IConstants.COMMAND_ID_PARAMETER);
		commandId.setName("Command Parameter");
		commandId.setOptional(false);
		newCommand.getParameters().add(commandId);
		return newCommand;
	}
	
	public MenuItemBuilder addShorcut(String shortcut) {
		return new ShorcutBuilder(this).addShorcut(shortcut);
	}
	
	Runnable getRunnable() {
		return runnable;
	}
	
	MenuItemBuilder getMenuItemBuilder() {
		return menuItemBuilder;
	}
}
