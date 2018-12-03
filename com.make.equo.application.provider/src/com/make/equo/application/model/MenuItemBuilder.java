package com.make.equo.application.model;

import static com.make.equo.application.util.OSUtils.isMac;
import static com.make.equo.application.util.OSUtils.isWindows;
import static com.make.equo.application.util.OSUtils.isUnix;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuFactoryImpl;

import com.make.equo.application.impl.EnterFullScreenModeRunnable;
import com.make.equo.application.util.ICommandConstants;
import com.make.equo.application.util.IConstants;

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

	public MenuItemBuilder addMenuItem(String label) {
		menuItem = createMenuItem(label);
		return new MenuItemBuilder(this);
	}

	private MHandledMenuItem createMenuItem(String label) {
		MHandledMenuItem newMenuItem = MenuFactoryImpl.eINSTANCE.createHandledMenuItem();
		MMenu parentMenu = menuBuilder.getMenu();
		String menuItemId = parentMenu.getElementId() + "." + label.replaceAll("\\s+", "").toLowerCase();
		newMenuItem.setElementId(menuItemId);
		newMenuItem.setLabel(label);
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
	 * @param label the label of the exit menu item
	 * @return
	 */
	public MenuItemBuilder onBeforeExit(String label, Runnable runnable) {
		MApplication mApplication = this.getMenuBuilder().getOptionalFieldBuilder().getEquoApplicationBuilder()
				.getmApplication();
		MCommand command = mApplication.getCommand(ICommandConstants.EXIT_COMMAND);
		if (!isMac()) {
			menuItem = createMenuItem(label);
			menuItem.setCommand(command);
		}
		mApplication.getTransientData().put(ICommandConstants.EXIT_COMMAND, runnable);
		return this;

	}

	/**
	 * Executes the {@code run} method of this runnable before exiting the
	 * application
	 * 
	 * @param runnable a runnable object
	 * @return this
	 */
	public MenuItemBuilder onBeforeExit(Runnable runnable) {
		return onBeforeExit(IConstants.DEFAULT_EXIT_LABEL, runnable);
	}

	/**
	 * Add Preferences menu item only if needed (Not needed in OSx) and executes the
	 * runnable when the item is accessed
	 * 
	 * @param label the label of the preferences menu item
	 * @return
	 */
	public MenuItemBuilder onPreferences(String label, Runnable runnable) {
		MApplication mApplication = this.getMenuBuilder().getOptionalFieldBuilder().getEquoApplicationBuilder()
				.getmApplication();
		MCommand command = mApplication.getCommand(ICommandConstants.PREFERENCES_COMMAND);
		if (!isMac()) {
			menuItem = createMenuItem(label);
			menuItem.setCommand(command);
		}
		mApplication.getTransientData().put(ICommandConstants.PREFERENCES_COMMAND, runnable);
		return this;
	}

	/**
	 * Executes the {@code run} method of this runnable when the item is accessed
	 * 
	 * @param runnable a runnable object
	 * @return this
	 */
	public MenuItemBuilder onPreferences(Runnable runnable) {
		return onPreferences(IConstants.DEFAULT_PREFERENCES_LABEL, runnable);
	}

	/**
	 * Add About menu item only if needed (Not needed in OSx) and executes the
	 * runnable before exiting the application
	 * 
	 * @param label the label of the exit menu item
	 * @return
	 */
	public MenuItemBuilder onAbout(String label, Runnable runnable) {
		MApplication mApplication = this.getMenuBuilder().getOptionalFieldBuilder().getEquoApplicationBuilder()
				.getmApplication();
		MCommand command = mApplication.getCommand(ICommandConstants.ABOUT_COMMAND);
		if (!isMac()) {
			menuItem = createMenuItem(label);
			menuItem.setCommand(command);
		}
		mApplication.getTransientData().put(ICommandConstants.ABOUT_COMMAND, runnable);
		return this;
	}
	
	/**
	 * Executes the {@code run} method of this runnable when the item is accessed
	 * 
	 * @param runnable a runnable object
	 * @return this
	 */
	public MenuItemBuilder onAbout(Runnable runnable) {
		return onAbout(IConstants.DEFAULT_ABOUT_LABEL, runnable);
	}
	
	public MenuItemBuilder addFullScreenModeMenuItem(String label) {
		menuItem = createMenuItem(label);
		return onClick(EnterFullScreenModeRunnable.instance);
	}

}
