/****************************************************************************
**
** Copyright (C) 2021 Equo
**
** This file is part of Equo Framework.
**
** Commercial License Usage
** Licensees holding valid commercial Equo licenses may use this file in
** accordance with the commercial license agreement provided with the
** Software or, alternatively, in accordance with the terms contained in
** a written agreement between you and Equo. For licensing terms
** and conditions see https://www.equoplatform.com/terms.
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

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.equo.application.handlers.BrowserParams;
import com.equo.comm.api.IEquoEventHandler;
import com.google.gson.Gson;

/**
 * API to perform window actions such as open new browsers or manage shortcuts.
 */
@Component(service = WindowManager.class)
public class WindowManager {

  @Reference
  private IEquoEventHandler eventHandler;

  public void openBrowser(String url) {
    BrowserParams browserParams = new BrowserParams(url);
    eventHandler.send("openBrowser", new Gson().toJsonTree(browserParams).getAsJsonObject());
  }

  public void openBrowser(String url, String browserName) {
    BrowserParams browserParams = new BrowserParams(url, browserName);
    eventHandler.send("openBrowser", new Gson().toJsonTree(browserParams).getAsJsonObject());
  }

  public void openBrowser(String url, String browserName, String position) {
    BrowserParams browserParams = new BrowserParams(url, browserName, position);
    eventHandler.send("openBrowser", new Gson().toJsonTree(browserParams).getAsJsonObject());
  }

  public void updateBrowser(String url) {
    BrowserParams browserParams = new BrowserParams(url);
    eventHandler.send("updateBrowser", new Gson().toJsonTree(browserParams).getAsJsonObject());
  }

  public void updateBrowser(String url, String browserName) {
    BrowserParams browserParams = new BrowserParams(url, browserName);
    eventHandler.send("updateBrowser", new Gson().toJsonTree(browserParams).getAsJsonObject());
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
    public void openBrowser(String url) {
      obtainInstance();
      instance.openBrowser(url);
    }

    @Override
    public void openBrowser(String url, String browserName) {
      obtainInstance();
      instance.openBrowser(url, browserName);
    }

    @Override
    public void openBrowser(String url, String browserName, String position) {
      obtainInstance();
      instance.openBrowser(url, browserName, position);
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
