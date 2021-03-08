package com.make.equo.application.model;

import java.util.List;
import java.util.Optional;

import org.eclipse.e4.ui.model.application.commands.MBindingTable;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandParameter;
import org.eclipse.e4.ui.model.application.commands.MKeyBinding;
import org.eclipse.e4.ui.model.application.commands.MParameter;

import com.google.common.collect.Lists;
import com.make.equo.application.impl.HandlerBuilder;
import com.make.equo.application.util.IConstants;

public class GlobalShortcutBuilder extends HandlerBuilder implements KeyBindingBuilder {

	private static final String GLOBAL_SUFFIX = ".global";
	private String elementId;
	private Runnable runnable;
	private EquoApplicationBuilder equoApplicationBuilder;
	private String userEvent;

	GlobalShortcutBuilder(EquoApplicationBuilder equoApplicationBuilder, String elementId, Runnable runnable,
			String userEvent) {
		super(equoApplicationBuilder.getmApplication(), IConstants.COMMAND_ID_PARAMETER,
				IConstants.PARAMETERIZED_COMMAND_CONTRIBUTION_URI);
		this.equoApplicationBuilder = equoApplicationBuilder;
		this.elementId = elementId;
		this.runnable = runnable;
		this.userEvent = userEvent;
	}

	public GlobalShortcutBuilder(EquoApplicationBuilder equoApplicationBuilder) {
		this(equoApplicationBuilder, "", () -> {
		}, "");
	}

	public void addGlobalShortcut(String shortcut) {
		MCommand newCommand = createCommandAndHandler(elementId + GLOBAL_SUFFIX + "." + shortcut.trim().toLowerCase());
		MKeyBinding globalKeyBinding = createKeyBinding(newCommand, shortcut);

		MParameter commandIdParameter = createMParameter(IConstants.COMMAND_ID_PARAMETER, newCommand.getElementId());
		globalKeyBinding.getParameters().add(commandIdParameter);

		MParameter userEventParameter = createMParameter(IConstants.EQUO_WEBSOCKET_USER_EMITTED_EVENT, this.userEvent);
		globalKeyBinding.getParameters().add(userEventParameter);

		MBindingTable parentPartBindingTable = equoApplicationBuilder.getViewBuilder().getBindingTable();
		removeShortcutFromBindingTable(shortcut, parentPartBindingTable.getBindings());
		parentPartBindingTable.getBindings().add(globalKeyBinding);
	}

	public void addGlobalShortcutToExistingCommand(MCommand command, String shortcut) {
		if (!shortcut.equals("")) {
			MKeyBinding globalKeyBinding = createKeyBinding(command, shortcut);
			Optional<MBindingTable> bindingTable = getDefaultBindingTable(equoApplicationBuilder);
			if (bindingTable.isPresent()) {
				bindingTable.get().getBindings().add(globalKeyBinding);
			}
		}
	}

	public void removeShortcut(String shortcut) {
		MBindingTable parentPartBindingTable = equoApplicationBuilder.getViewBuilder().getBindingTable();
		removeShortcutFromBindingTable(shortcut, parentPartBindingTable.getBindings());
		removeShortcutFromBindingTable(shortcut, getDefaultBindingTable(equoApplicationBuilder).get().getBindings());
	}

	private void removeShortcutFromBindingTable(String shortcut, List<MKeyBinding> bindings) {
		MKeyBinding bindingToDelete = null;
		for (MKeyBinding binding : bindings) {
			if (binding.getKeySequence().equals(shortcut)) {
				bindingToDelete = binding;
				break;
			}
		}
		if (bindingToDelete != null) {
			bindings.remove(bindingToDelete);
		}
	}

	@Override
	protected Runnable getRunnable() {
		return runnable;
	}

	@Override
	protected List<MCommandParameter> createCommandParameters() {
		MCommandParameter windowNameCommandParameter = createCommandParameter(
				IConstants.EQUO_WEBSOCKET_USER_EMITTED_EVENT, "User emitted event", true);
		return Lists.newArrayList(windowNameCommandParameter);
	}
}
