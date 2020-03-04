package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;

import com.make.equo.application.util.IConstants;

public class MenuItemHandlerBuilder extends ItemHandlerBuilder {

	MenuItemHandlerBuilder(MenuItemBuilder menuItemBuilder) {
		super(menuItemBuilder);

	}

	@Override
	protected MenuItemBuilder onClick(Runnable runnable) {
		this.runnable = runnable;
		MHandledMenuItem menuItem = getMenuItemBuilder().getMenuItem();

		String id = menuItem.getElementId();

		MCommand newCommand = createCommandAndHandler(id);

		menuItem.setCommand(newCommand);
		String commandId = newCommand.getElementId();

		MParameter commandIdparameter = createMParameter(IConstants.COMMAND_ID_PARAMETER, commandId);
		menuItem.getParameters().add(commandIdparameter);

		MParameter userEventParameter = createMParameter(IConstants.EQUO_WEBSOCKET_USER_EMITTED_EVENT, userEvent);
		menuItem.getParameters().add(userEventParameter);

		return getMenuItemBuilder();
	}

	public MenuItemBuilder getMenuItemBuilder() {
		return (MenuItemBuilder) super.itemBuilder;
	}

	public MenuBuilder withMainMenu(String menuLabel) {
		return new MenuBuilder(getMenuItemBuilder().getMenuBuilder().getOptionalFieldBuilder()).addMenu(menuLabel);
	}

	public ItemBuilder addShortcut(String keySequence) {
		new MenuItemShortcutBuilder(getMenuItemBuilder(), userEvent).addShortcut(keySequence);
		EquoApplicationBuilder equoApplicationBuilder = getMenuItemBuilder().getMenuBuilder().getOptionalFieldBuilder()
				.getEquoApplicationBuilder();
		new GlobalShortcutBuilder(equoApplicationBuilder, getMenuItemBuilder().getMenuItem().getElementId(),
				this.runnable, this.userEvent).addGlobalShortcut(keySequence);
		return getMenuItemBuilder();
	}

}
