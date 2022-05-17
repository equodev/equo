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

package com.equo.application.model.util;

import static com.equo.application.util.ICommandConstants.ABOUT_COMMAND;
import static com.equo.application.util.ICommandConstants.EXIT_COMMAND;
import static com.equo.application.util.ICommandConstants.PREFERENCES_COMMAND;

import java.util.List;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MHandler;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Component;

import com.equo.application.model.EquoApplicationModel;
import com.equo.application.model.OptionalViewBuilder;
import com.equo.application.util.IConstants;

/**
 * MenuModelHelper containing a few useful functions.
 */
@Component(service = MenuModelHelper.class)
public class MenuModelHelper {

  public static final String ENABLE_WEB_MENU_SYSTEM_PROPERTY = "enableWebMenu";

  /**
   * Gets a WindowManager instance.
   * @return a WindowManager instance or null if there was an error getting the
   *         component
   */
  public static MenuModelHelper getInstance() {
    Bundle ctxBundle = FrameworkUtil.getBundle(MenuModelHelper.class);
    BundleContext ctx = ctxBundle.getBundleContext();
    if (ctx != null) {
      @SuppressWarnings("unchecked")
      ServiceReference<MenuModelHelper> serviceReference = (ServiceReference<MenuModelHelper>) ctx
          .getServiceReference(MenuModelHelper.class.getName());
      if (serviceReference != null) {
        return ctx.getServiceObjects(serviceReference).getService();
      }
    }
    return null;
  }

  /**
   * Removes the shortcut from a given menu element.
   * @param element             to remove the shortcut from
   * @param optionalViewBuilder the instance used to construct the menu item
   */
  public void removeShortcutFromItem(MMenuElement element,
      OptionalViewBuilder optionalViewBuilder) {
    if (element instanceof MHandledMenuItem) {
      MHandledMenuItem item = (MHandledMenuItem) element;
      Object shortcut = item.getTransientData().get(IConstants.ITEM_SHORTCUT);
      if (shortcut != null) {
        optionalViewBuilder.removeShortcut((String) shortcut);
      }
    }
  }

  /**
   * Removes the associated commands and handlers from a given menu element.
   * @param element to remove the commands and handlers from
   */
  public void removeAsocciatedCommandsAndHandlers(MMenuElement element) {
    if (element instanceof MHandledMenuItem) {
      MHandledMenuItem item = (MHandledMenuItem) element;
      MCommand itemCommand = item.getCommand();
      String commandId = itemCommand.getElementId();
      if (commandId.equals(EXIT_COMMAND) || commandId.equals(ABOUT_COMMAND)
          || commandId.equals(PREFERENCES_COMMAND)) {
        return;
      }
      MHandler itemCommandHandler =
          (MHandler) item.getCommand().getTransientData().get("thisHandler");

      MApplication mApplication = EquoApplicationModel.getApplicaton().getMainApplication();

      mApplication.getCommands().remove(itemCommand);
      if (itemCommandHandler != null) {
        mApplication.getHandlers().remove(itemCommandHandler);
      } else {
        for (MHandler handler : mApplication.getHandlers()) {
          if (handler.getCommand() == itemCommand) {
            mApplication.getHandlers().remove(handler);
            break;
          }
        }
      }
    }
  }

  /**
   * Removes the given menu element recursively.
   * @param element             to remove
   * @param optionalViewBuilder the instance used to construct the menu item
   */
  public void removeRecursively(MMenuElement element, OptionalViewBuilder optionalViewBuilder) {
    if (element instanceof MMenu) {
      MMenu menu = (MMenu) element;
      List<MMenuElement> children = menu.getChildren();
      for (MMenuElement child : children) {
        removeShortcutFromItem(child, optionalViewBuilder);
        removeAsocciatedCommandsAndHandlers(child);
        removeRecursively(child, optionalViewBuilder);
        child.setVisible(false);
      }
      children.clear();
    }
    removeShortcutFromItem(element, optionalViewBuilder);
    element.setVisible(false);
  }

}
