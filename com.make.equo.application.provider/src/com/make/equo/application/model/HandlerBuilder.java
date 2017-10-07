package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandParameter;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.commands.MHandler;
import org.eclipse.e4.ui.model.application.commands.impl.CommandsFactoryImpl;

import com.make.equo.application.util.IConstants;

public abstract class HandlerBuilder implements MParameterBuilder {
	
	private static final String PARAMETERIZED_COMMAND_CONTRIBUTION_URI = "bundleclass://com.make.equo.application.provider/com.make.equo.application.handlers.ParameterizedCommandHandler";
	private EquoApplicationBuilder equoApplicationBuilder;

	HandlerBuilder(EquoApplicationBuilder equoApplicationBuilder) {
		this.equoApplicationBuilder = equoApplicationBuilder;
	}

	MCommandParameter createCommandParameter(String id) {
		MCommandParameter commandParameter = MCommandsFactory.INSTANCE.createCommandParameter();
		commandParameter.setElementId(id);
		commandParameter.setName("Command Parameter Name");
		commandParameter.setOptional(false);
		return commandParameter;
	}

	MHandler createNewHandler(String id, String contributionUri) {
		MHandler newHandler = CommandsFactoryImpl.eINSTANCE.createHandler();
		newHandler.setElementId(id + IConstants.HANDLER_SUFFIX) ;
		newHandler.setContributionURI(contributionUri);
		return newHandler;
	}

	MCommand createNewCommand(String id) {
		MCommand newCommand = CommandsFactoryImpl.eINSTANCE.createCommand();
		newCommand.setElementId(id + IConstants.COMMAND_SUFFIX) ;
		newCommand.setCommandName(id + IConstants.COMMAND_SUFFIX);
		return newCommand;
	}
	
	MCommand createCommandAndHandler(String id) {
		MCommand newCommand = createNewCommand(id);
		MCommandParameter commandParameter = createCommandParameter(IConstants.COMMAND_ID_PARAMETER);
		newCommand.getParameters().add(commandParameter);
		
		MHandler newHandler = createNewHandler(id, PARAMETERIZED_COMMAND_CONTRIBUTION_URI);
		newHandler.setCommand(newCommand);
		
		MApplication mApplication = getEquoApplicationBuilder().getmApplication();
		mApplication.getCommands().add(newCommand);
		mApplication.getHandlers().add(newHandler);
		
		mApplication.getTransientData().put(newCommand.getElementId(), getRunnable());
		
		return newCommand;
	}
	
	abstract Runnable getRunnable();

	EquoApplicationBuilder getEquoApplicationBuilder() {
		return equoApplicationBuilder;
	}
}
