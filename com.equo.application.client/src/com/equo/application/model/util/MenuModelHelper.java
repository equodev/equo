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
import org.osgi.service.component.annotations.Reference;

import com.equo.application.model.OptionalViewBuilder;
import com.equo.application.util.IConstants;

/**
 * MenuModelHelper containing a few useful functions.
 */
@Component(service = MenuModelHelper.class)
public class MenuModelHelper {

  public static final String ENABLE_WEB_MENU_SYSTEM_PROPERTY = "enableWebMenu";

  @Reference
  private MApplication mApplication;

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
