package com.make.equo.application.model;

import java.util.Optional;

import org.eclipse.e4.ui.model.application.commands.MBindingTable;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MKeyBinding;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;

import com.make.equo.application.util.IConstants;

public class ToolbarItemShortcutBuilder implements KeyBindingBuilder{
	
	private ToolbarItemBuilder toolbarItemBuilder;
	private String userEvent;

	ToolbarItemShortcutBuilder(ToolbarItemBuilder toolbarItemBuilder, String userEvent) {
		this.toolbarItemBuilder = toolbarItemBuilder;
		this.userEvent = userEvent;
	}

	void addShortcut(String shortcut) {
		Optional<MBindingTable> bindingTable = getDefaultBindingTable(
				toolbarItemBuilder.getToolbarBuilder().getOptionalFieldBuilder().getEquoApplicationBuilder());
		if (bindingTable.isPresent()) {
			MBindingTable mBindingTable = bindingTable.get();
			MHandledToolItem toolItem = toolbarItemBuilder.getToolItem();
			MCommand command = toolItem.getCommand();

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
