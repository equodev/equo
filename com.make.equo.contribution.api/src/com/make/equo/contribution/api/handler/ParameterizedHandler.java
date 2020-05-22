package com.make.equo.contribution.api.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.eclipse.core.commands.Category;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.IParameter;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MBindingTable;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandParameter;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.commands.MHandler;
import org.eclipse.e4.ui.model.application.commands.MKeyBinding;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.commands.impl.CommandsFactoryImpl;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.make.equo.contribution.api.IEquoContributionManager;

public abstract class ParameterizedHandler {
	protected abstract String getCommandId();

	protected abstract IParameter[] getParameters();

	protected String getShortcut() {
		return "";
	}

	public void registerCommand(EHandlerService handlerService, ECommandService commandService, MApplication app) {
		String commandId = getCommandId();

		/*
		 * if (app == null) { Command command = commandService.getCommand(commandId); if
		 * (!command.isDefined()) { final String categoryName = getCategoryName();
		 * Category category = commandService.getCategory(categoryName); if
		 * (!category.isDefined()) { category.define(categoryName, ""); }
		 * command.define(commandId, "", category, getParameters()); }
		 * 
		 * handlerService.activateHandler(commandId, this); return; }
		 */

		MCommand newCommand = createCommandAndHandler(commandId, app);

		String shortcut = getShortcut();
		if (!shortcut.equals("")) {
			List<MKeyBinding> bindings = getDefaultBindingTable(app).get().getBindings();
			MKeyBinding globalKeyBinding = createKeyBinding(newCommand, shortcut);
			bindings.add(globalKeyBinding);
		}
	}

	Optional<MBindingTable> getDefaultBindingTable(MApplication mApplication) {
		for (MBindingTable mBindingTable : mApplication.getBindingTables()) {
			if (mBindingTable.getElementId().equals("com.make.equo.application.bindingtable.default")) {
				return Optional.of(mBindingTable);
			}
		}
		return Optional.empty();
	}

	MKeyBinding createKeyBinding(MCommand command, String shortcut) {
		MKeyBinding keyBinding = MCommandsFactory.INSTANCE.createKeyBinding();
		keyBinding.setKeySequence(shortcut);
		String commandId = command.getElementId();
		keyBinding.setElementId(commandId);
		keyBinding.setCommand(command);
		return keyBinding;
	}

	private MCommand createNewCommand(String id) {
		MCommand newCommand = CommandsFactoryImpl.eINSTANCE.createCommand();
		newCommand.setElementId(id);
		newCommand.setCommandName(id);
		return newCommand;
	}

	private MHandler createNewHandler(String id, String contributionUri) {
		MHandler newHandler = CommandsFactoryImpl.eINSTANCE.createHandler();
		newHandler.setElementId(id);
		newHandler.setContributionURI(contributionUri);
		return newHandler;
	}

	protected MCommandParameter createCommandParameter(String id, String name, boolean isOptional) {
		MCommandParameter commandParameter = MCommandsFactory.INSTANCE.createCommandParameter();
		commandParameter.setElementId(id);
		commandParameter.setName(name);
		commandParameter.setOptional(isOptional);
		return commandParameter;
	}

	protected List<MCommandParameter> createCommandParameters() {
		List<MCommandParameter> list = new ArrayList<>();
		for (IParameter parameter : getParameters()) {
			list.add(createCommandParameter(parameter.getId(), parameter.getName(), parameter.isOptional()));
		}
		return list;
	}

	public MCommand createCommandAndHandler(String id, MApplication mApplication) {
		MCommand newCommand = createNewCommand(id);
		newCommand.getParameters().addAll(createCommandParameters());

		String contributionUri = "bundleclass://" + FrameworkUtil.getBundle(this.getClass()).getSymbolicName() + "/"
				+ this.getClass().getName();

		MHandler newHandler = createNewHandler(id, contributionUri);
		newHandler.setCommand(newCommand);

		mApplication.getCommands().add(newCommand);
		mApplication.getHandlers().add(newHandler);

		return newCommand;
	}
}
