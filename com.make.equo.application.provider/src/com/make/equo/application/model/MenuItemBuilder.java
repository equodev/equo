package com.make.equo.application.model;

import static com.make.equo.application.util.OSUtils.isMac;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuFactoryImpl;

import com.make.equo.application.impl.EnterFullScreenModeRunnable;
import com.make.equo.application.util.ICommandConstants;
import com.make.equo.application.util.IConstants;

public class MenuItemBuilder extends ItemBuilder {

	private MenuBuilder menuBuilder;

	MenuItemBuilder(MenuBuilder menuBuilder) {
		super(menuBuilder.getOptionalFieldBuilder());
		this.menuBuilder = menuBuilder;
	}

	MenuItemBuilder(MenuItemBuilder menuItemBuilder) {
		super(menuItemBuilder.getOptionalFieldBuilder());
		this.setItem(menuItemBuilder.getItem());
		this.menuBuilder = menuItemBuilder.menuBuilder;
	}

	public MenuItemBuilder addMenuItem(String label) {
		MHandledItem actualItem = this.getItem();
		this.setItem(createMenuItem(label));
		MenuItemBuilder newItemBuilder = new MenuItemBuilder(this);
		this.setItem(actualItem);
		return newItemBuilder;
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

	public MenuItemBuilder onClick(Runnable action) {
		return (MenuItemBuilder) onClick(action, null);
	}

	public MenuBuilder addMenu(String menuLabel) {
		return new MenuBuilder(this.menuBuilder).addMenu(menuLabel);
	}

	public MenuItemSeparatorBuilder addMenuSeparator() {
		return new MenuItemSeparatorBuilder(this.menuBuilder).addMenuItemSeparator();
	}
	
	@Override
	public MenuItemBuilder addShortcut(String keySequence) {
		return (MenuItemBuilder)super.addShortcut(keySequence);
	}

	/**
	 * Add Exit menu item only if needed (Not needed in OSx) and executes the
	 * runnable before exiting the application
	 * 
	 * @param label the label of the exit menu item
	 * @return
	 */
	public MenuItemBuilder onBeforeExit(String label, Runnable runnable) {
		MApplication mApplication = this.menuBuilder.getOptionalFieldBuilder().getEquoApplicationBuilder()
				.getmApplication();
		MCommand command = mApplication.getCommand(ICommandConstants.EXIT_COMMAND);
		if (!isMac()) {
			setItem(createMenuItem(label));
			this.getItem().setCommand(command);
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
		MApplication mApplication = this.menuBuilder.getOptionalFieldBuilder().getEquoApplicationBuilder()
				.getmApplication();
		MCommand command = mApplication.getCommand(ICommandConstants.PREFERENCES_COMMAND);
		if (!isMac()) {
			setItem(createMenuItem(label));
			this.getItem().setCommand(command);
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
		MApplication mApplication = this.menuBuilder.getOptionalFieldBuilder().getEquoApplicationBuilder()
				.getmApplication();
		MCommand command = mApplication.getCommand(ICommandConstants.ABOUT_COMMAND);
		if (!isMac()) {
			setItem(createMenuItem(label));
			this.getItem().setCommand(command);
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
		this.setItem(createMenuItem(label));
		return (MenuItemBuilder) onClick(EnterFullScreenModeRunnable.instance);
	}
}
