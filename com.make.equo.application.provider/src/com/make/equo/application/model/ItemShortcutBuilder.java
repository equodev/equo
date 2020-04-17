package com.make.equo.application.model;

import java.util.Optional;

import org.eclipse.e4.ui.model.application.commands.MBindingTable;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MKeyBinding;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;

import com.make.equo.application.util.IConstants;

public class ItemShortcutBuilder implements KeyBindingBuilder {

	private ItemBuilder itemBuilder;
	private String userEvent;

	ItemShortcutBuilder(ItemBuilder itemBuilder, String userEvent) {
		this.itemBuilder = itemBuilder;
		this.userEvent = userEvent;
	}

	void addShortcut(String shortcut) {
		Optional<MBindingTable> bindingTable = getDefaultBindingTable(
				itemBuilder.getOptionalFieldBuilder().getEquoApplicationBuilder());
		if (bindingTable.isPresent()) {
			MBindingTable mBindingTable = bindingTable.get();
			MHandledItem toolItem = itemBuilder.getItem();
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
