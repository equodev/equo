package com.make.equo.application.model;

import java.util.Optional;

import org.eclipse.e4.ui.model.application.commands.MBindingTable;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MKeyBinding;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;

import com.make.equo.application.util.IConstants;

class MenuItemShortcutBuilder implements KeyBindingBuilder {

	private MenuItemBuilder menuItemBuilder;
	private String userEvent;

	MenuItemShortcutBuilder(MenuItemBuilder menuItemBuilder, String userEvent) {
		this.menuItemBuilder = menuItemBuilder;
		this.userEvent = userEvent;
	}

	void addShortcut(String shortcut) {
		Optional<MBindingTable> bindingTable = getDefaultBindingTable(
				menuItemBuilder.getMenuBuilder().getOptionalFieldBuilder().getEquoApplicationBuilder());
		if (bindingTable.isPresent()) {
			MBindingTable mBindingTable = bindingTable.get();
			MHandledMenuItem menuItem = menuItemBuilder.getMenuItem();
			MCommand command = menuItem.getCommand();

			MKeyBinding keyBinding = createKeyBinding(command, shortcut);

			MParameter commandParameterId = createMParameter(IConstants.COMMAND_ID_PARAMETER, command.getElementId());
			keyBinding.getParameters().add(commandParameterId);
			MParameter userEventParameter = createMParameter(IConstants.EQUO_WEBSOCKET_USER_EMITTED_EVENT,
					this.userEvent);
			keyBinding.getParameters().add(userEventParameter);

			mBindingTable.getBindings().add(keyBinding);
		} else {
			// TODO add logging
			System.out.println("There is no default binding table created for the " + shortcut + " shortcut");
		}
	}

}
