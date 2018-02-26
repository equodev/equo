package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuFactoryImpl;

import com.make.equo.application.util.ICommandConstants;

public class MenuItemBuilder {

	private MHandledMenuItem menuItem;
	private MenuBuilder menuBuilder;
	private MenuItemHandlerBuilder menuItemHandlerBuilder;

	MenuItemBuilder(MenuBuilder menuBuilder) {
		this.menuBuilder = menuBuilder;
	}

	MenuItemBuilder(MenuItemBuilder menuItemBuilder) {
		this.menuItem = menuItemBuilder.menuItem;
		this.menuBuilder = menuItemBuilder.menuBuilder;
	}

	public MenuItemBuilder addMenuItem(String menuItemLabel) {
		menuItem = createMenuItem(menuItemLabel);
		return new MenuItemBuilder(this);
	}

	private MHandledMenuItem createMenuItem(String menuItemlabel) {
		MHandledMenuItem newMenuItem = MenuFactoryImpl.eINSTANCE.createHandledMenuItem();
		MMenu parentMenu = menuBuilder.getMenu();
		String menuItemId = parentMenu.getElementId() + "." + menuItemlabel.replaceAll("\\s+", "").toLowerCase();
		newMenuItem.setElementId(menuItemId);
		newMenuItem.setLabel(menuItemlabel);
		parentMenu.getChildren().add(newMenuItem);
		return newMenuItem;
	}

	public MenuItemBuilder onClick(Runnable runnable) {
		return onClick(runnable, null);
	}

	public MenuItemBuilder onClick(Runnable runnable, String userEvent) {
		menuItemHandlerBuilder = new MenuItemHandlerBuilder(this);
		MenuItemBuilder menuItemBuilder = menuItemHandlerBuilder.onClick(runnable, userEvent);
		return menuItemBuilder;
	}

	public MenuItemBuilder onClick(String userEvent) {
		return onClick(null, userEvent);
	}

	public MenuBuilder addMenu(String menuLabel) {
		return new MenuBuilder(this.menuBuilder).addMenu(menuLabel);
	}

	public MenuBuilder withMainMenu(String menuLabel) {
		return new MenuBuilder(menuBuilder.getOptionalFieldBuilder()).addMenu(menuLabel);
	}

	public EquoApplication start() {
		return menuBuilder.getOptionalFieldBuilder().start();
	}

	public MenuItemSeparatorBuilder addMenuSeparator() {
		return new MenuItemSeparatorBuilder(this.menuBuilder).addMenuItemSeparator();
	}

	MHandledMenuItem getMenuItem() {
		return menuItem;
	}

	MenuBuilder getMenuBuilder() {
		return menuBuilder;
	}

	public MenuItemBuilder addShortcut(String keySequence) {
		if (menuItemHandlerBuilder != null) {
			return menuItemHandlerBuilder.addShortcut(keySequence);
		}
		// log that there is no menu item handler -> no onClick method was called.
		return this;
	}

	/**
	 * Add Exit menu item only if needed (Not needed in OSx) and executes the runnable
	 * before exiting the application
	 * 
	 * @param menuLabel
	 *            the label of the exit menu item
	 * @return
	 */
	public MenuItemBuilder onExit(String menuLabel, Runnable runnable) {
		MApplication mApplication = this.getMenuBuilder().getOptionalFieldBuilder()
				.getEquoApplicationBuilder().getmApplication();
		MCommand command = mApplication.getCommand(ICommandConstants.EXIT_COMMAND);
		menuItem = createMenuItem(menuLabel);
		menuItem.setCommand(command);
		return onExit(runnable);
	}

	/**
	 * Executes the {@code run} method of this runnable before exiting the
	 * application
	 * 
	 * @param runnable
	 *            a runnable object
	 * @return this
	 */
	public MenuItemBuilder onExit(Runnable runnable) {
		MApplication mApplication = this.getMenuBuilder().getOptionalFieldBuilder().getEquoApplicationBuilder()
				.getmApplication();
		mApplication.getTransientData().put(ICommandConstants.EXIT_COMMAND, runnable);
		return this;
	}
}
