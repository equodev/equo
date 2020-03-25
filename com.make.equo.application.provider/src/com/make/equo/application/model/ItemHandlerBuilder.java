package com.make.equo.application.model;

import java.util.List;

import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandParameter;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;

import com.google.common.collect.Lists;
import com.make.equo.application.impl.HandlerBuilder;
import com.make.equo.application.util.IConstants;

public class ItemHandlerBuilder extends HandlerBuilder {
	
	protected Runnable runnable;
	protected String userEvent;
	protected ItemBuilder itemBuilder;

	ItemHandlerBuilder(ItemBuilder itemBuilder){
		super(itemBuilder.getOptionalFieldBuilder().getEquoApplicationBuilder()
				.getmApplication(), IConstants.COMMAND_ID_PARAMETER, IConstants.PARAMETERIZED_COMMAND_CONTRIBUTION_URI);
		this.itemBuilder = itemBuilder;
	}
	
	public ItemBuilder onClick(Runnable runnable, String userEvent) {
		this.userEvent = userEvent;
		onClick(runnable);
		return this.itemBuilder;
	}
	
	private ItemBuilder onClick(Runnable runnable) {
		this.runnable = runnable;
		MHandledItem item = getItemBuilder().getItem();

		String id = item.getElementId();

		MCommand newCommand = createCommandAndHandler(id);

		item.setCommand(newCommand);
		String commandId = newCommand.getElementId();

		MParameter commandIdparameter = createMParameter(IConstants.COMMAND_ID_PARAMETER, commandId);
		item.getParameters().add(commandIdparameter);

		MParameter userEventParameter = createMParameter(IConstants.EQUO_WEBSOCKET_USER_EMITTED_EVENT, userEvent);
		item.getParameters().add(userEventParameter);

		return getItemBuilder();
	}

	
	ItemBuilder getItemBuilder() {
		return this.itemBuilder;
	}
	
	public ItemBuilder addShortcut(String keySequence) {
		new ItemShortcutBuilder(this.getItemBuilder(), userEvent).addShortcut(keySequence);
		EquoApplicationBuilder equoApplicationBuilder = getItemBuilder().getOptionalFieldBuilder()
				.getEquoApplicationBuilder();
		new GlobalShortcutBuilder(equoApplicationBuilder, getItemBuilder().getItem().getElementId(),
				this.runnable, this.userEvent).addGlobalShortcut(keySequence);
		return getItemBuilder();
	}
	
	public ToolbarBuilder withToolbar() {
		return new ToolbarBuilder(getItemBuilder().getOptionalFieldBuilder(),
				getItemBuilder().getOptionalFieldBuilder().getEquoApplicationBuilder().getmWindow()).addToolbar();
	}
	
	public MenuBuilder withMainMenu(String menuLabel) {
		return new MenuBuilder(getItemBuilder().getOptionalFieldBuilder()).addMenu(menuLabel);
	}
	
	@Override
	protected List<MCommandParameter> createCommandParameters() {
		MCommandParameter windowNameCommandParameter = createCommandParameter(
				IConstants.EQUO_WEBSOCKET_USER_EMITTED_EVENT, "User emitted event", true);
		return Lists.newArrayList(windowNameCommandParameter);
	}
	
	@Override
	protected Runnable getRunnable() {
		return runnable;
	}
	
}
