package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;

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

	@Override
	protected Runnable getRunnable() {
		return runnable;
	}

	public ToolbarItemBuilder onClick(Runnable runnable, String userEvent) {
		this.userEvent = userEvent;
		onClick(runnable);
		return this.toolbarItemBuilder;
	}

//	ToolbarItemBuilder addShortcut(String keySequence) {
//		new MenuItemShortcutBuilder(this.toolbarItemBuilder, userEvent).addShortcut(keySequence);
//		EquoApplicationBuilder equoApplicationBuilder = this.toolbarItemBuilder.getMenuBuilder().getOptionalFieldBuilder()
//				.getEquoApplicationBuilder();
//		new GlobalShortcutBuilder(equoApplicationBuilder, this.toolbarItemBuilder.getMenuItem().getElementId(),
//				this.runnable, this.userEvent).addGlobalShortcut(keySequence);
//		return this.toolbarItemBuilder;
//	

}
