package com.make.equo.application.model;

import java.util.Optional;

import org.eclipse.e4.ui.model.application.commands.MBindingTable;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MKeyBinding;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;

class MenuItemShortcutBuilder implements KeyBindingBuilder {

	private MenuItemBuilder menuItemBuilder;

	public MenuItemShortcutBuilder(MenuItemBuilder menuItemBuilder) {
		this.menuItemBuilder = menuItemBuilder;
	}
	
	public void addShorcut(String shortcut) {
		Optional<MBindingTable> bindingTable = getDefaultBindingTable(menuItemBuilder.menuBuilder.optionalFieldBuilder.equoApplicationBuilder);
		if (bindingTable.isPresent()) {
			MBindingTable mBindingTable = bindingTable.get();
			MHandledMenuItem menuItem = menuItemBuilder.menuItem;
			MCommand command = menuItem.getCommand();
			
			MKeyBinding keyBinding = createKeyBinding(command, shortcut);
			
			mBindingTable.getBindings().add(keyBinding);
		} else {
			//TODO add logging
			System.out.println("There is no default binding table created for the " + shortcut + " shortcut");
		}
	}

}
