package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.commands.MBindingTable;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MKeyBinding;

public class GlobalShortcutBuilder extends HandlerBuilder implements KeyBindingBuilder {

	private static final String GLOBAL_SUFFIX = ".global";
	private String elementId;
	private Runnable runnable;

	GlobalShortcutBuilder(EquoApplicationBuilder equoApplicationBuilder, String elementId, Runnable runnable) {
		super(equoApplicationBuilder);
		this.elementId = elementId;
		this.runnable = runnable;
	}
	
	public void addGlobalShortcut(String shortcut) {
		MCommand newCommand = createCommandAndHandler(elementId + GLOBAL_SUFFIX);
		MKeyBinding globalKeyBinding = createKeyBinding(newCommand, shortcut);
		MBindingTable parentPartBindingTable = getEquoApplicationBuilder().getUrlMandatoryFieldBuilder().getBindingTable();
		parentPartBindingTable.getBindings().add(globalKeyBinding);
	}

	@Override
	Runnable getRunnable() {
		return runnable;
	}

}
