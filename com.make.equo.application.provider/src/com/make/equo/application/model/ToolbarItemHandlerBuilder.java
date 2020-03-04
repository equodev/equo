package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;

import com.make.equo.application.util.IConstants;

public class ToolbarItemHandlerBuilder extends ItemHandlerBuilder {

	ToolbarItemHandlerBuilder(ToolbarItemBuilder toolbarItemBuilder) {
		super(toolbarItemBuilder);
	}

	public ToolbarItemBuilder getToolbarItemBuilder() {
		return (ToolbarItemBuilder) super.itemBuilder;
	}

	@Override
	protected ToolbarItemBuilder onClick(Runnable runnable) {
		super.runnable = runnable;
		MHandledToolItem toolbarItem = getToolbarItemBuilder().getToolItem();

		String id = toolbarItem.getElementId();

		MCommand newCommand = createCommandAndHandler(id);

		toolbarItem.setCommand(newCommand);
		String commandId = newCommand.getElementId();

		MParameter commandIdparameter = createMParameter(IConstants.COMMAND_ID_PARAMETER, commandId);
		toolbarItem.getParameters().add(commandIdparameter);

		MParameter userEventParameter = createMParameter(IConstants.EQUO_WEBSOCKET_USER_EMITTED_EVENT, userEvent);
		toolbarItem.getParameters().add(userEventParameter);

		return getToolbarItemBuilder();
	}

	public ToolbarBuilder withToolbar() {
		return new ToolbarBuilder(getToolbarItemBuilder().getToolbarBuilder().getOptionalFieldBuilder(),
				getToolbarItemBuilder().getToolbarBuilder().getParent()).addToolbar();
	}

	@Override
	public ToolbarItemBuilder addShortcut(String keySequence) {
		new ToolbarItemShortcutBuilder(getToolbarItemBuilder(), userEvent).addShortcut(keySequence);
		EquoApplicationBuilder equoApplicationBuilder = getToolbarItemBuilder().getToolbarBuilder()
				.getOptionalFieldBuilder().getEquoApplicationBuilder();
		new GlobalShortcutBuilder(equoApplicationBuilder, getToolbarItemBuilder().getToolItem().getElementId(),
				this.runnable, this.userEvent).addGlobalShortcut(keySequence);
		return getToolbarItemBuilder();
	}

}
