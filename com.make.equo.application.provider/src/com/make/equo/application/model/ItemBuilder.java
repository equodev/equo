package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;

import com.make.equo.application.util.IConstants;

/**
 * Abstract Equo menu builder for Java.
 */
public abstract class ItemBuilder {

	private ItemHandlerBuilder itemHandlerBuilder;
	private MHandledItem item;
	private OptionalViewBuilder viewBuilder;

	/**
	 * Creates a new instance.
	 * 
	 * @param viewBuilder
	 */
	public ItemBuilder(OptionalViewBuilder viewBuilder) {
		this.viewBuilder = viewBuilder;
	}

	/**
	 * Creates a root menu, visible in the bar.
	 * 
	 * @param menuLabel the menu title.
	 * @return the MenuBuilder instance.
	 */
	public MenuBuilder withMainMenu(String menuLabel) {
		return new MenuBuilder(this.getOptionalFieldBuilder()).addMenu(menuLabel);
	}

	/**
	 * Adds a toolbar to current builder instance.
	 * 
	 * @return the ToolbarBuilder instance.
	 */
	public ToolbarBuilder withToolbar() {
		return new ToolbarBuilder(this.getOptionalFieldBuilder(),
				this.getOptionalFieldBuilder().getEquoApplicationBuilder().getmWindow()).addToolbar();
	}

	/**
	 * Adds the shortcut in item builder.
	 * 
	 * @param keySequence the shortcut command.
	 * @return this.
	 */
	public ItemBuilder addShortcut(String keySequence) {
		if (getItemHandlerBuilder() != null) {
			return getItemHandlerBuilder().addShortcut(keySequence);
		}
		// log that there is no menu item handler -> no onClick method was called.
		return this;
	}

	/**
	 * Starts Equo application.
	 * 
	 * @return the EquoApplicationBuilder instance.
	 */
	public EquoApplicationBuilder start() {
		return this.getOptionalFieldBuilder().start();
	}

	protected ItemHandlerBuilder getItemHandlerBuilder() {
		return itemHandlerBuilder;
	}

	protected void setItemHandlerBuilder(ItemHandlerBuilder itemHandlerBuilder) {
		this.itemHandlerBuilder = itemHandlerBuilder;
	}

	MHandledItem getItem() {
		return this.item;
	}

	protected void setItem(MHandledItem item) {
		this.item = item;
	}

	OptionalViewBuilder getOptionalFieldBuilder() {
		return this.viewBuilder;
	}

	/**
	 * Sets the action and runneble in menu element.
	 * 
	 * @param runnable
	 * @param action   the action name.
	 * @return the ItemBuilder instance.
	 */
	public ItemBuilder onClick(Runnable runnable, String action) {
		this.setItemHandlerBuilder(new ItemHandlerBuilder(this));
		this.getItem().getTransientData().put(IConstants.IS_AN_EQUO_MODEL_ELEMENT, true);
		this.getItem().getTransientData().put(IConstants.EQUO_WEBSOCKET_USER_EMITTED_EVENT, action);
		ItemBuilder itemBuilder = getItemHandlerBuilder().onClick(runnable, action);
		return itemBuilder;
	}

}