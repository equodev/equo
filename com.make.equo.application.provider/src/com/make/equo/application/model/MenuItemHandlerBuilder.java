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
	private String userEvent;

	MenuItemHandlerBuilder(MenuItemBuilder menuItemBuilder) {
		super(menuItemBuilder.getMenuBuilder().getOptionalFieldBuilder().getEquoApplicationBuilder().getmApplication(),
				IConstants.COMMAND_ID_PARAMETER, IConstants.PARAMETERIZED_COMMAND_CONTRIBUTION_URI);
		this.menuItemBuilder = menuItemBuilder;
	}

	private MenuItemBuilder onClick(Runnable runnable) {
		this.runnable = runnable;
		MHandledMenuItem menuItem = this.menuItemBuilder.getMenuItem();

		String id = menuItem.getElementId();

		MCommand newCommand = createCommandAndHandler(id);

		menuItem.setCommand(newCommand);
		String commandId = newCommand.getElementId();

		MParameter commandIdparameter = createMParameter(IConstants.COMMAND_ID_PARAMETER, commandId);
		menuItem.getParameters().add(commandIdparameter);

		MParameter userEventParameter = createMParameter(IConstants.EQUO_WEBSOCKET_USER_EMITTED_EVENT, userEvent);
		menuItem.getParameters().add(userEventParameter);

		return this.menuItemBuilder;
	}

	@Override
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

	MenuItemBuilder onClick(Runnable runnable, String userEvent) {
		this.userEvent = userEvent;
		onClick(runnable);
		return this.menuItemBuilder;
	}

	MenuItemBuilder addShortcut(String keySequence) {
		new MenuItemShortcutBuilder(this.menuItemBuilder, userEvent).addShortcut(keySequence);
		EquoApplicationBuilder equoApplicationBuilder = this.menuItemBuilder.getMenuBuilder().getOptionalFieldBuilder()
				.getEquoApplicationBuilder();
		new GlobalShortcutBuilder(equoApplicationBuilder, this.menuItemBuilder.getMenuItem().getElementId(),
				this.runnable, this.userEvent).addGlobalShortcut(keySequence);
		return this.menuItemBuilder;
	}

}
