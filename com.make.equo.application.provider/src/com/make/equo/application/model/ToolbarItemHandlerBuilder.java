package com.make.equo.application.model;

import java.util.List;

import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandParameter;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;

import com.google.common.collect.Lists;
import com.make.equo.application.impl.HandlerBuilder;
import com.make.equo.application.util.IConstants;

public class ToolbarItemHandlerBuilder extends HandlerBuilder {

	private ToolbarItemBuilder toolbarItemBuilder;
	private Runnable runnable;
	private String userEvent;

	ToolbarItemHandlerBuilder(ToolbarItemBuilder toolbarItemBuilder) {
		super(toolbarItemBuilder.getToolbarBuilder().getOptionalFieldBuilder().getEquoApplicationBuilder()
				.getmApplication(), IConstants.COMMAND_ID_PARAMETER, IConstants.PARAMETERIZED_COMMAND_CONTRIBUTION_URI);
		this.toolbarItemBuilder = toolbarItemBuilder;
	}

	private ToolbarItemBuilder onClick(Runnable runnable) {
		this.runnable = runnable;
		MHandledToolItem toolbarItem = this.toolbarItemBuilder.getToolItem();

		String id = toolbarItem.getElementId();

		MCommand newCommand = createCommandAndHandler(id);

		toolbarItem.setCommand(newCommand);
		String commandId = newCommand.getElementId();

		MParameter commandIdparameter = createMParameter(IConstants.COMMAND_ID_PARAMETER, commandId);
		toolbarItem.getParameters().add(commandIdparameter);

		MParameter userEventParameter = createMParameter(IConstants.EQUO_WEBSOCKET_USER_EMITTED_EVENT, userEvent);
		toolbarItem.getParameters().add(userEventParameter);

		return this.toolbarItemBuilder;
	}

	public ToolbarItemBuilder onClick(Runnable runnable, String userEvent) {
		this.userEvent = userEvent;
		onClick(runnable);
		return this.toolbarItemBuilder;
	}

	@Override
	protected List<MCommandParameter> createCommandParameters() {
		MCommandParameter windowNameCommandParameter = createCommandParameter(
				IConstants.EQUO_WEBSOCKET_USER_EMITTED_EVENT, "User emitted event", true);
		return Lists.newArrayList(windowNameCommandParameter);
	}

	public ToolbarBuilder withToolbar() {
		return new ToolbarBuilder(toolbarItemBuilder.getToolbarBuilder().getOptionalFieldBuilder(),
				toolbarItemBuilder.getToolbarBuilder().getParent()).addToolbar();
	}

	@Override
	protected Runnable getRunnable() {
		return runnable;
	}

	public ToolbarItemBuilder addShortcut(String keySequence) {
		new ToolbarItemShortcutBuilder(this.toolbarItemBuilder, userEvent).addShortcut(keySequence);
		EquoApplicationBuilder equoApplicationBuilder = this.toolbarItemBuilder.getToolbarBuilder()
				.getOptionalFieldBuilder().getEquoApplicationBuilder();
		new GlobalShortcutBuilder(equoApplicationBuilder, this.toolbarItemBuilder.getToolItem().getElementId(),
				this.runnable, this.userEvent).addGlobalShortcut(keySequence);
		return this.toolbarItemBuilder;
	}

}
