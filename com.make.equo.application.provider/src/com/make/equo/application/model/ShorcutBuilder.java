package com.make.equo.application.model;

import java.util.Optional;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MBindingTable;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandParameter;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.commands.MHandler;
import org.eclipse.e4.ui.model.application.commands.MKeyBinding;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.commands.impl.CommandsFactoryImpl;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;

import com.make.equo.application.util.IConstants;

public class ShorcutBuilder {

	private HandlerBuilder handlerBuilder;

	public ShorcutBuilder(HandlerBuilder handlerBuilder) {
		this.handlerBuilder = handlerBuilder;
	}
	
	public MenuItemBuilder addShorcut(String shortcut) {
		Optional<MBindingTable> bindingTable = getBindingTable(IConstants.DEFAULT_BINDING_TABLE);
		if (bindingTable.isPresent()) {
			MBindingTable mBindingTable = bindingTable.get();
			MenuItemBuilder menuItemBuilder = handlerBuilder.getMenuItemBuilder();
			MHandledMenuItem menuItem = menuItemBuilder.menuItem;
			MCommand command = menuItem.getCommand();
			
			MCommandParameter commandParameter = createCommandParameter(IConstants.COMMAND_ID_PARAMETER);
			command.getParameters().add(commandParameter);
			
			MKeyBinding keyBinding = createKeyBinding(command.getElementId(), shortcut);
			keyBinding.setCommand(command);
			MParameter parameter = createMParameter(IConstants.COMMAND_ID_PARAMETER, command.getElementId());
			keyBinding.getParameters().add(parameter);
			
			MApplication mApplication = handlerBuilder.getMenuItemBuilder().menuBuilder.optionalFieldBuilder.equoApplicationBuilder.mApplication;
			mApplication.getTransientData().put(command.getElementId(), handlerBuilder.getRunnable());
			
			
			mBindingTable.getBindings().add(keyBinding);
			
			MKeyBinding globalShorcut = createGlobalShorcut(menuItem.getElementId(), shortcut);
			MBindingTable parentPartBindingTable = handlerBuilder.getMenuItemBuilder().menuBuilder.optionalFieldBuilder.equoApplicationBuilder.urlMandatoryFieldBuilder.getBindingTable();
			parentPartBindingTable.getBindings().add(globalShorcut);
//			Optional<MBindingTable> globalBindingTable = getBindingTable("com.make.equo.application.bindingtable.default");
//			globalBindingTable.get().getBindings().add(globalShorcut);
		} else {
			//TODO add logging.
			System.out.println("There is no default binding table.");
		}
		return new MenuItemBuilder(handlerBuilder.getMenuItemBuilder());
	}

	private MKeyBinding createGlobalShorcut(String id, String shortcut) {
		MCommand newCommand = createNewCommand(id);
		MCommandParameter commandParameter = createCommandParameter(IConstants.COMMAND_ID_PARAMETER);
		newCommand.getParameters().add(commandParameter);
		
		MHandler newHandler = createNewHandler(id, "bundleclass://com.make.equo.application.provider/com.make.equo.application.handlers.ParameterizedCommandHandler");
		newHandler.setCommand(newCommand);
		
		MKeyBinding globalKeyBinding = createKeyBinding(newCommand.getElementId(), shortcut);
		globalKeyBinding.setCommand(newCommand);
		MParameter parameter = createMParameter(IConstants.COMMAND_ID_PARAMETER, newCommand.getElementId());
		globalKeyBinding.getParameters().add(parameter);
		
		MApplication mApplication = handlerBuilder.getMenuItemBuilder().menuBuilder.optionalFieldBuilder.equoApplicationBuilder.mApplication;
		mApplication.getCommands().add(newCommand);
		mApplication.getHandlers().add(newHandler);
		
		mApplication.getTransientData().put(newCommand.getElementId(), handlerBuilder.getRunnable());
		
		return globalKeyBinding;
	}
	
//	private void addParameterTo(MKeyBinding globalKeyBinding, MCommand command) {
//		String commandId = command.getElementId();
//		MParameter parameter = createMParameter(commandId, commandId);
//		globalKeyBinding.getParameters().add(parameter);
//	}

	private MCommandParameter createCommandParameter(String id) {
		MCommandParameter commandParameter = MCommandsFactory.INSTANCE.createCommandParameter();
		commandParameter.setElementId(id);
		commandParameter.setName("Command Parameter Name");
		commandParameter.setOptional(false);
		return commandParameter;
	}

	private MHandler createNewHandler(String id, String contributionUri) {
		MHandler newHandler = CommandsFactoryImpl.eINSTANCE.createHandler();
		newHandler.setElementId(id + IConstants.HANDLER_SUFFIX + ".global") ;
		newHandler.setContributionURI(contributionUri);
		return newHandler;
	}

	private MCommand createNewCommand(String id) {
		MCommand newCommand = CommandsFactoryImpl.eINSTANCE.createCommand();
		newCommand.setElementId(id + IConstants.COMMAND_SUFFIX + ".global") ;
		newCommand.setCommandName(id + IConstants.COMMAND_SUFFIX + ".global");
		return newCommand;
	}
	
	private MKeyBinding createKeyBinding(String id, String shortcut) {
		MKeyBinding keyBinding = MCommandsFactory.INSTANCE.createKeyBinding();
		keyBinding.setKeySequence(shortcut);
		keyBinding.setElementId(id + ".keybinding");
		keyBinding.getTags().add(IConstants.USER_KEY_BINDING_TAG);
		return keyBinding;
	}
	
	private MParameter createMParameter(String id, String value) {
		MParameter parameter = MCommandsFactory.INSTANCE.createParameter();
		// set the identifier for the corresponding command parameter
		parameter.setName(id);
		parameter.setValue(value);
		return parameter;
	}
	
	private Optional<MBindingTable> getBindingTable(String id) {
		MApplication mApplication = handlerBuilder.getMenuItemBuilder().menuBuilder.optionalFieldBuilder.equoApplicationBuilder.mApplication;
		for (MBindingTable mBindingTable : mApplication.getBindingTables()) {
			if (mBindingTable.getElementId().equals(id)) {
				return Optional.of(mBindingTable);
			}
		}
		return Optional.empty();
	}

}
