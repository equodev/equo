package com.make.equo.application.model;

import java.util.List;

import org.eclipse.e4.ui.model.application.commands.MCommandParameter;

import com.google.common.collect.Lists;
import com.make.equo.application.impl.HandlerBuilder;
import com.make.equo.application.util.IConstants;

public abstract class ItemHandlerBuilder extends HandlerBuilder {
	
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
	
	protected abstract ItemBuilder onClick(Runnable runnable);
	
	protected abstract ItemBuilder addShortcut(String keySequence);
}
