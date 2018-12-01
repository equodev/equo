package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuFactoryImpl;

import com.make.equo.application.impl.EnterFullScreenModeRunnable;
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

	public EquoApplicationBuilder start() {
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
	 * Add Exit menu item only if needed (Not needed in OSx) and executes the
	 * runnable before exiting the application
	 * 
	 * @param menuItemLabel the label of the exit menu item
	 * @return
	 */
	public MenuItemBuilder onBeforeExit(String menuItemLabel, Runnable runnable) {
		menuItem = createMenuItem(menuItemLabel);
		return onBeforeExit(runnable);
	}

	/**
	 * Executes the {@code run} method of this runnable before exiting the
	 * application
	 * 
	 * @param runnable a runnable object
	 * @return this
	 */
	public MenuItemBuilder onBeforeExit(Runnable runnable) {
		MApplication mApplication = this.getMenuBuilder().getOptionalFieldBuilder().getEquoApplicationBuilder()
				.getmApplication();
		mApplication.getTransientData().put(ICommandConstants.EXIT_COMMAND, runnable);
		MCommand command = mApplication.getCommand(ICommandConstants.EXIT_COMMAND);
		menuItem.setCommand(command);
		return this;
	}

	/**
	 * Add Preferences menu item only if needed (Not needed in OSx) and executes the
	 * runnable when the item is accessed
	 * 
	 * @param menuItemLabel the label of the preferences menu item
	 * @return
	 */
	public MenuItemBuilder onPreferences(String menuItemLabel, Runnable runnable) {
		menuItem = createMenuItem(menuItemLabel);
		return onPreferences(runnable);
	}

	/**
	 * Executes the {@code run} method of this runnable when the item is accessed
	 * 
	 * @param runnable a runnable object
	 * @return this
	 */
	public MenuItemBuilder onPreferences(Runnable runnable) {
		MApplication mApplication = this.getMenuBuilder().getOptionalFieldBuilder().getEquoApplicationBuilder()
				.getmApplication();
		mApplication.getTransientData().put(ICommandConstants.PREFERENCES_COMMAND, runnable);
		MCommand command = mApplication.getCommand(ICommandConstants.PREFERENCES_COMMAND);
		menuItem.setCommand(command);
		return this;
	}

	/**
	 * Add About menu item only if needed (Not needed in OSx) and executes the
	 * runnable before exiting the application
	 * 
	 * @param menuItemLabel the label of the exit menu item
	 * @return
	 */
	public MenuItemBuilder onAbout(String menuItemLabel, Runnable runnable) {
		menuItem = createMenuItem(menuItemLabel);
		return onAbout(runnable);
	}
	
	/**
	 * Executes the {@code run} method of this runnable when the item is accessed
	 * 
	 * @param runnable a runnable object
	 * @return this
	 */
	public MenuItemBuilder onAbout(Runnable runnable) {
		MApplication mApplication = this.getMenuBuilder().getOptionalFieldBuilder().getEquoApplicationBuilder()
				.getmApplication();
		mApplication.getTransientData().put(ICommandConstants.ABOUT_COMMAND, runnable);
		MCommand command = mApplication.getCommand(ICommandConstants.ABOUT_COMMAND);
		menuItem.setCommand(command);
		return this;
	}
	
	public MenuItemBuilder addFullScreenModeMenuItem(String menuItemLabel) {
		menuItem = createMenuItem(menuItemLabel);
		return onClick(EnterFullScreenModeRunnable.instance);
	}

}
