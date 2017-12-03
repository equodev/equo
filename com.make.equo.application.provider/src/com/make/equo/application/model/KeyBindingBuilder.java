package com.make.equo.application.model;

import java.util.Optional;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MBindingTable;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.commands.MKeyBinding;

import com.make.equo.application.impl.MParameterBuilder;
import com.make.equo.application.util.IConstants;

interface KeyBindingBuilder extends MParameterBuilder{
	
	public static final String KEYBINDING_SUFFIX = ".keybinding";

	default MKeyBinding createKeyBinding(MCommand command, String shortcut) {
		MKeyBinding keyBinding = MCommandsFactory.INSTANCE.createKeyBinding();
		keyBinding.setKeySequence(shortcut);
		String commandId = command.getElementId();
		keyBinding.setElementId(commandId + KEYBINDING_SUFFIX);
		keyBinding.getTags().add(IConstants.USER_KEY_BINDING_TAG);
		keyBinding.setCommand(command);
		return keyBinding;
	}
	
	default Optional<MBindingTable> getDefaultBindingTable(EquoApplicationBuilder equoApplicationBuilder) {
		MApplication mApplication = equoApplicationBuilder.getmApplication();
		for (MBindingTable mBindingTable : mApplication.getBindingTables()) {
			if (mBindingTable.getElementId().equals(IConstants.DEFAULT_BINDING_TABLE)) {
				return Optional.of(mBindingTable);
			}
		}
		return Optional.empty();
	}
	
}
