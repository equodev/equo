/****************************************************************************
**
** Copyright (C) 2021 Equo
**
** This file is part of the Equo SDK.
**
** Commercial License Usage
** Licensees holding valid commercial Equo licenses may use this file in
** accordance with the commercial license agreement provided with the
** Software or, alternatively, in accordance with the terms contained in
** a written agreement between you and Equo. For licensing terms
** and conditions see https://www.equo.dev/terms.
**
** GNU General Public License Usage
** Alternatively, this file may be used under the terms of the GNU
** General Public License version 3 as published by the Free Software
** Foundation. Please review the following
** information to ensure the GNU General Public License requirements will
** be met: https://www.gnu.org/licenses/gpl-3.0.html.
**
****************************************************************************/

package com.equo.application.model;

import static com.equo.application.util.OsUtils.isMac;

import java.net.URL;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.impl.CommandsFactoryImpl;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuFactoryImpl;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.equo.application.api.IEquoApplication;
import com.equo.application.impl.EnterFullScreenModeRunnable;
import com.equo.application.util.ICommandConstants;
import com.equo.application.util.IConstants;
import com.equo.logging.client.api.Logger;
import com.equo.logging.client.api.LoggerFactory;

/**
 * Equo menu item builder for Java.
 */
public class MenuItemBuilder extends ItemBuilder {
  private static MCommand disabledCommand;
  private static Logger logger = LoggerFactory.getLogger(MenuItemBuilder.class);

  static {
    disabledCommand = CommandsFactoryImpl.eINSTANCE.createCommand();
    disabledCommand.setElementId("DISABLEDCOMMAND");
  }

  private MenuBuilder menuBuilder;

  MenuItemBuilder(MenuBuilder menuBuilder) {
    super(menuBuilder.getOptionalFieldBuilder());
    this.menuBuilder = menuBuilder;
  }

  MenuItemBuilder(OptionalViewBuilder optionalViewBuilder, MHandledItem item,
      MenuBuilder menuBuilder) {
    super(optionalViewBuilder);
    this.setItem(item);
    this.menuBuilder = menuBuilder;
  }

  /**
   * Adds a new menu item that will not contain other menus.
   * @param  label the item name.
   * @return       the MenuItemBuilder instance.
   */
  public MenuItemBuilder addMenuItem(String label) {
    return new MenuItemBuilder(this.getOptionalFieldBuilder(), createMenuItem(label), menuBuilder);
  }

  /**
   * Adds an icon to a menu item.
   * @param  iconPath the icon relative path to resources folder.
   * @return          the MenuItemBuilder instance.
   */
  public MenuItemBuilder withIcon(String iconPath) {
    BundleContext bundleContext = FrameworkUtil.getBundle(getClass()).getBundleContext();
    ServiceReference<IEquoApplication> serviceReference =
        bundleContext.getServiceReference(IEquoApplication.class);
    IEquoApplication app = bundleContext.getService(serviceReference);
    Bundle bundle = FrameworkUtil.getBundle(app.getClass());
    URL path = bundle.getResource(iconPath);

    if (path == null) {
      logger.warn("Problem loading " + this.getItem().getLabel() + " icon");
      return this;
    }

    this.getItem().setIconURI(path.toString());
    return this;
  }

  private MHandledMenuItem createMenuItem(String label) {
    MHandledMenuItem newMenuItem = MenuFactoryImpl.eINSTANCE.createHandledMenuItem();
    MMenu parentMenu = menuBuilder.getMenu();
    String menuItemId =
        parentMenu.getElementId() + "." + label.replaceAll("\\s+", "").toLowerCase();
    newMenuItem.setElementId(menuItemId);
    newMenuItem.setLabel(label);
    newMenuItem.setCommand(disabledCommand);
    parentMenu.getChildren().add(newMenuItem);
    return newMenuItem;
  }

  /**
   * Sets the runnable in menu element.
   * @param  runnable runnable to be executed.
   * @return          this.
   */
  public MenuItemBuilder onClick(Runnable runnable) {
    return onClick(runnable, null);
  }

  /**
   * Sets the action and runneble in menu element.
   * @param  runnable the runnable.
   * @param  action   the action name.
   * @return          this.
   */
  public MenuItemBuilder onClick(Runnable runnable, String action) {
    return (MenuItemBuilder) super.onClick(runnable, action);
  }

  /**
   * Sets the action in menu element.
   * @param  action the action name.
   * @return        this.
   */
  public MenuItemBuilder onClick(String action) {
    return onClick(null, action);
  }

  /**
   * Adds a new menu that will contain other menus.
   * @param  menuLabel the menu title.
   * @return           the MenuBuilder instance.
   */
  public MenuBuilder addMenu(String menuLabel) {
    return new MenuBuilder(this.menuBuilder).addMenu(menuLabel);
  }

  /**
   * Adds a separator between menus.
   * @return the MenuItemSeparatorBuilder instance.
   */
  public MenuItemSeparatorBuilder addMenuSeparator() {
    return new MenuItemSeparatorBuilder(this.menuBuilder).addMenuItemSeparator();
  }

  @Override
  public MenuItemBuilder withShortcut(String keySequence) {
    return (MenuItemBuilder) super.withShortcut(keySequence);
  }

  /**
   * Adds a Exit menu item only if needed (Not needed in OSx) and executes the
   * runnable before exiting the application.
   * @param  label the label of the exit menu item.
   * @return       this.
   */
  public MenuItemBuilder onBeforeExit(String label, Runnable runnable) {
    MApplication mApplication =
        this.menuBuilder.getOptionalFieldBuilder().getEquoApplicationBuilder().getmApplication();
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
   * application.
   * @param  runnable a runnable object.
   * @return          this.
   */
  public MenuItemBuilder onBeforeExit(Runnable runnable) {
    return onBeforeExit(IConstants.DEFAULT_EXIT_LABEL, runnable);
  }

  /**
   * Adds a Preferences menu item only if needed (Not needed in OSx) and executes
   * the runnable when the item is accessed.
   * @param  label the label of the preferences menu item.
   * @return       this.
   */
  public MenuItemBuilder onPreferences(String label, Runnable runnable) {
    MApplication mApplication =
        this.menuBuilder.getOptionalFieldBuilder().getEquoApplicationBuilder().getmApplication();
    MCommand command = mApplication.getCommand(ICommandConstants.PREFERENCES_COMMAND);
    if (!isMac()) {
      setItem(createMenuItem(label));
      this.getItem().setCommand(command);
    }
    mApplication.getTransientData().put(ICommandConstants.PREFERENCES_COMMAND, runnable);
    return this;
  }

  /**
   * Executes the {@code run} method of this runnable when the item is accessed.
   * @param  runnable a runnable object.
   * @return          this.
   */
  public MenuItemBuilder onPreferences(Runnable runnable) {
    return onPreferences(IConstants.DEFAULT_PREFERENCES_LABEL, runnable);
  }

  /**
   * Adds a About menu item only if needed (Not needed in OSx) and executes the
   * runnable before exiting the application.
   * @param  label the label of the exit menu item.
   * @return       this.
   */
  public MenuItemBuilder onAbout(String label, Runnable runnable) {
    MApplication mApplication =
        this.menuBuilder.getOptionalFieldBuilder().getEquoApplicationBuilder().getmApplication();
    MCommand command = mApplication.getCommand(ICommandConstants.ABOUT_COMMAND);
    if (!isMac()) {
      setItem(createMenuItem(label));
      this.getItem().setCommand(command);
    }
    mApplication.getTransientData().put(ICommandConstants.ABOUT_COMMAND, runnable);
    return this;
  }

  /**
   * Executes the {@code run} method of this runnable when the item is accessed.
   * @param  runnable a runnable object.
   * @return          this.
   */
  public MenuItemBuilder onAbout(Runnable runnable) {
    return onAbout(IConstants.DEFAULT_ABOUT_LABEL, runnable);
  }

  /**
   * Adds a new menu item with full screen mode.
   * @param  label the menu title.
   * @return       this.
   */
  public MenuItemBuilder addFullScreenModeMenuItem(String label) {
    this.setItem(createMenuItem(label));
    return (MenuItemBuilder) onClick(EnterFullScreenModeRunnable.instance);
  }
}
