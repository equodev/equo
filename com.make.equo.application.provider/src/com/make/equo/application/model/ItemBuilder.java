package com.make.equo.application.model;

public abstract class ItemBuilder {

	private ItemHandlerBuilder itemHandlerBuilder;
	

	public MenuBuilder withMainMenu(String menuLabel) {
		return new MenuBuilder(this.getOptionalFieldBuilder()).addMenu(menuLabel);
	}

	public ToolbarBuilder withToolbar() {
		return new ToolbarBuilder(this.getOptionalFieldBuilder(),
				this.getOptionalFieldBuilder().getEquoApplicationBuilder().getmWindow()).addToolbar();
	}
	
	public ItemBuilder addShortcut(String keySequence) {
		if (getItemHandlerBuilder() != null) {
			return (MenuItemBuilder)getItemHandlerBuilder().addShortcut(keySequence);
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

	public abstract OptionalViewBuilder getOptionalFieldBuilder();

	public abstract ItemBuilder onClick(Runnable runnable, String userEvent);
		
}