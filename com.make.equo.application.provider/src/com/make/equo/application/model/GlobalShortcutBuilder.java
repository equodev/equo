package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.commands.MBindingTable;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MKeyBinding;

import com.make.equo.application.impl.HandlerBuilder;
import com.make.equo.application.util.IConstants;

public class GlobalShortcutBuilder extends HandlerBuilder implements KeyBindingBuilder {

	private static final String GLOBAL_SUFFIX = ".global";
	private String elementId;
	private Runnable runnable;
	private EquoApplicationBuilder equoApplicationBuilder;

	GlobalShortcutBuilder(EquoApplicationBuilder equoApplicationBuilder, String elementId, Runnable runnable) {
		super(equoApplicationBuilder.getmApplication(), IConstants.COMMAND_ID_PARAMETER, IConstants.PARAMETERIZED_COMMAND_CONTRIBUTION_URI);
		this.equoApplicationBuilder = equoApplicationBuilder;
		this.elementId = elementId;
		this.runnable = runnable;
	}
	
	public void addGlobalShortcut(String shortcut) {
		MCommand newCommand = createCommandAndHandler(elementId + GLOBAL_SUFFIX);
		MKeyBinding globalKeyBinding = createKeyBinding(newCommand, shortcut);
		MBindingTable parentPartBindingTable = equoApplicationBuilder.getUrlMandatoryFieldBuilder().getBindingTable();
		parentPartBindingTable.getBindings().add(globalKeyBinding);
	}

	@Override
	protected Runnable getRunnable() {
		return runnable;
	}

}
