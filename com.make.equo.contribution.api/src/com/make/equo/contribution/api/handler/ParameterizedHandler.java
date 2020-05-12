package com.make.equo.contribution.api.handler;

import org.eclipse.core.commands.Category;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.IParameter;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;

public abstract class ParameterizedHandler {
	protected abstract String getCommandId();

	protected abstract String getCategoryName();

	protected abstract IParameter[] getParameters();

	public void registerCommand(EHandlerService handlerService, ECommandService commandService) {
		String commandId = getCommandId();
		Command command = commandService.getCommand(commandId);
		if (!command.isDefined()) {
			final String categoryName = getCategoryName();
			Category category = commandService.getCategory(categoryName);
			if (!category.isDefined()) {
				category.define(categoryName, "");
			}
			command.define(commandId, "", category, getParameters());
		}

		handlerService.activateHandler(commandId, this);
	}
}
