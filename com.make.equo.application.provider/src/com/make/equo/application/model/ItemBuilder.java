package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;

public abstract class ItemBuilder {

	private ItemHandlerBuilder itemHandlerBuilder;
	private MHandledItem item;

	public MenuBuilder withMainMenu(String menuLabel) {
		return new MenuBuilder(this.getOptionalFieldBuilder()).addMenu(menuLabel);
	}

	public ToolbarBuilder withToolbar() {
		return new ToolbarBuilder(this.getOptionalFieldBuilder(),
				this.getOptionalFieldBuilder().getEquoApplicationBuilder().getmWindow()).addToolbar();
	}

	public ItemBuilder addShortcut(String keySequence) {
		if (getItemHandlerBuilder() != null) {
			return (MenuItemBuilder) getItemHandlerBuilder().addShortcut(keySequence);
		}
		// log that there is no menu item handler -> no onClick method was called.
		return this;
	}

	public EquoApplicationBuilder start() {
		return this.getOptionalFieldBuilder().start();
	}

	protected ItemHandlerBuilder getItemHandlerBuilder() {
		return itemHandlerBuilder;
	}

	protected void setItemHandlerBuilder(ItemHandlerBuilder itemHandlerBuilder) {
		this.itemHandlerBuilder = itemHandlerBuilder;
	}

	protected MHandledItem getItem() {
		return this.item;
	}

	protected void setItem(MHandledItem item) {
		this.item = item;
	}

	public abstract OptionalViewBuilder getOptionalFieldBuilder();
	
	public ItemBuilder onClick(Runnable runnable, String action) {
		this.setItemHandlerBuilder(new ItemHandlerBuilder(this));
		ItemBuilder menuItemBuilder = getItemHandlerBuilder().onClick(runnable, action);
		return menuItemBuilder;
	}

}