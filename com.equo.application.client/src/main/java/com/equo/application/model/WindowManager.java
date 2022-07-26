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

import static org.eclipse.swt.SWT.NO_TRIM;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.equo.application.handlers.BrowserParams;
import com.equo.application.model.window.WindowClient;
import com.equo.comm.api.ICommService;
import com.google.gson.Gson;

/**
 * API to perform window actions such as open new browsers or manage shortcuts.
 */
@Component(service = WindowManager.class)
public class WindowManager {

  @Reference
  private ICommService commService;

  private Display display;

  private Display getDisplay() {
    MApplication mApplication = EquoApplicationModel.getApplicaton().getMainApplication();
    if (display == null || display.isDisposed()) {
      display = mApplication.getContext().get(Display.class);
    }
    return display;
  }

  /**
   * Creates a window with given url.
   * @param url to open inside the browser
   */
  public WindowClient createWindow(String url) {
    return new WindowClient(url, commService);
  }

  /**
   * Creates a window with the given browserName containing a browser pointing to
   * the given url.
   * @param browserName the window name
   * @param url         to open inside the browser
   */
  public WindowClient createWindow(String url, String browserName) {
    return new WindowClient(url, browserName, commService);
  }

  public void updateBrowser(String url) {
    BrowserParams browserParams = new BrowserParams(url);
    commService.send("updateBrowser", new Gson().toJsonTree(browserParams).getAsJsonObject());
  }

  public void updateBrowser(String url, String browserName) {
    BrowserParams browserParams = new BrowserParams(url, browserName);
    commService.send("updateBrowser", new Gson().toJsonTree(browserParams).getAsJsonObject());
  }

  /**
   * Adds a global shortcut with a custom runnable.
   * @param keySequence string format of the shortcut.
   * @param runnable    the runnable.
   */
  public void addShortcut(String keySequence, Runnable runnable) {
    addShortcut(keySequence, runnable, null);
  }

  /**
   * Adds a global shortcut which calls the given event name.
   * @param keySequence string format of the shortcut.
   * @param userEvent   the user event name to call.
   */
  public void addShortcut(String keySequence, String userEvent) {
    addShortcut(keySequence, null, userEvent);
  }

  private void addShortcut(String keySequence, Runnable runnable, String userEvent) {
    EquoApplicationBuilder equoAppBuilder = EquoApplicationBuilder.getCurrentBuilder();
    new GlobalShortcutBuilder(equoAppBuilder,
        equoAppBuilder.getViewBuilder().getPart().getElementId(), runnable, userEvent)
            .addGlobalShortcut(keySequence);
  }

  /**
   * Removes a global shortcut from the app.
   * @param keySequence string format of the shortcut.
   */
  public void removeShortcut(String keySequence) {
    EquoApplicationBuilder equoAppBuilder = EquoApplicationBuilder.getCurrentBuilder();
    new GlobalShortcutBuilder(equoAppBuilder,
        equoAppBuilder.getViewBuilder().getPart().getElementId(), null, null)
            .removeShortcut(keySequence);
  }

  /**
   * Finds and returns the top level window.
   * @return top level window or null
   */
  public MWindow getTopLevelWindow() {
    MApplication mApplication = EquoApplicationModel.getApplicaton().getMainApplication();
    for (MWindow window : mApplication.getChildren()) {
      if (window.getTags().contains("topLevel")) {
        return window;
      }
    }
    return null;
  }

  /**
   * Finds and returns the window with the specified name.
   * @param  name the window name
   * @return      found window or null
   */
  public MWindow getWindow(String name) {
    if (name == null) {
      return null;
    }
    MApplication mApplication = EquoApplicationModel.getApplicaton().getMainApplication();
    for (MWindow window : mApplication.getChildren()) {
      if (window.getLabel().equals(name)) {
        return window;
      }
    }
    return null;
  }

  /**
   * Moves the top level window to the given coordinates.
   * @param x coordinate
   * @param y coordinate
   */
  public void moveWindow(int x, int y) {
    moveWindow(getTopLevelWindow(), x, y);
  }

  /**
   * Moves the window with the given name to the given coordinates.
   * @param name the window name
   * @param x    coordinate
   * @param y    coordinate
   */
  public void moveWindow(String name, int x, int y) {
    MWindow window = getWindow(name);
    moveWindow(window, x, y);
  }

  /**
   * Moves the given window to the given coordinates.
   * @param window to move
   * @param x      coordinate
   * @param y      coordinate
   */
  public void moveWindow(MWindow window, int x, int y) {
    if (window.getWidget() instanceof Shell) {
      getDisplay().syncExec(() -> {
        Shell shell = (Shell) window.getWidget();
        Rectangle newBounds = shell.getBounds();
        newBounds.x = x;
        newBounds.y = y;
        shell.setBounds(newBounds);
      });
    } else {
      window.setX(x);
      window.setY(y);
    }
  }

  /**
   * Resizes the top level window to the given width and height.
   * @param width  new width for the window
   * @param height new height for the window
   */
  public void resizeWindow(int width, int height) {
    resizeWindow(getTopLevelWindow(), width, height);
  }

  /**
   * Resizes the window with the given name to match given width and height.
   * @param name   the window name
   * @param width  new width for the window
   * @param height new height for the window
   */
  public void resizeWindow(String name, int width, int height) {
    MWindow window = getWindow(name);
    resizeWindow(window, width, height);
  }

  /**
   * Resizes the given window to match the given width and height.
   * @param window to resize
   * @param width  new width for the window
   * @param height new height for the window
   */
  public void resizeWindow(MWindow window, int width, int height) {
    if (window.getWidget() instanceof Shell) {
      getDisplay().syncExec(() -> {
        Shell shell = (Shell) window.getWidget();
        Rectangle newBounds = shell.getBounds();
        newBounds.width = width;
        newBounds.height = height;
        shell.setBounds(newBounds);
      });
    } else {
      window.setWidth(width);
      window.setHeight(height);
    }
  }

  /**
   * Minimizes the window with the given name.
   * @param name the window name
   */
  public void minimizeWindow(String name) {
    minimizeWindow(getWindow(name));
  }

  /**
   * Minimizes the given window.
   * @param window to minimize
   */
  public void minimizeWindow(MWindow window) {
    Shell windowShell = (Shell) window.getWidget();
    getDisplay().syncExec(() -> {
      windowShell.setMaximized(false);
      windowShell.setMinimized(true);
    });
  }

  /**
   * Maximizes the window with the given name.
   * @param name the window name
   */
  public void maximizeWindow(String name) {
    maximizeWindow(getWindow(name));
  }

  /**
   * Maximizes the given window.
   * @param window to maximize
   */
  public void maximizeWindow(MWindow window) {
    Shell windowShell = (Shell) window.getWidget();
    getDisplay().syncExec(() -> {
      if ((windowShell.getStyle() & NO_TRIM) == NO_TRIM) {
        windowShell.setMinimized(false);
        windowShell.setBounds(Display.getDefault().getPrimaryMonitor().getBounds());
      } else {
        windowShell.setMinimized(false);
        windowShell.setMaximized(true);
      }
    });
  }

  /**
   * Gets a WindowManager instance.
   * @return a WindowManager instance or null if there was an error getting the
   *         component
   */
  public static WindowManager getInstance() {
    Bundle ctxBundle = FrameworkUtil.getBundle(WindowManager.class);
    if (ctxBundle == null) {
      return new LazyWindowManager();
    }
    BundleContext ctx = ctxBundle.getBundleContext();
    if (ctx != null) {
      @SuppressWarnings("unchecked")
      ServiceReference<WindowManager> serviceReference =
          (ServiceReference<WindowManager>) ctx.getServiceReference(WindowManager.class.getName());
      if (serviceReference != null) {
        return ctx.getServiceObjects(serviceReference).getService();
      }
    }
    return null;
  }

  private static class LazyWindowManager extends WindowManager {
    private WindowManager instance;

    private void obtainInstance() {
      if (instance == null) {
        instance = WindowManager.getInstance();
      }
    }

    @Override
    public WindowClient createWindow(String url) {
      obtainInstance();
      return instance.createWindow(url);
    }

    @Override
    public WindowClient createWindow(String url, String browserName) {
      obtainInstance();
      return instance.createWindow(url, browserName);
    }

    @Override
    public void updateBrowser(String url) {
      obtainInstance();
      instance.updateBrowser(url);
    }

    @Override
    public void updateBrowser(String url, String browserName) {
      obtainInstance();
      instance.updateBrowser(url, browserName);
    }

    @Override
    public void addShortcut(String keySequence, Runnable runnable) {
      obtainInstance();
      instance.addShortcut(keySequence, runnable);
    }

    @Override
    public void addShortcut(String keySequence, String userEvent) {
      obtainInstance();
      instance.addShortcut(keySequence, userEvent);
    }

    @Override
    public void removeShortcut(String keySequence) {
      obtainInstance();
      instance.removeShortcut(keySequence);
    }
  }
}
