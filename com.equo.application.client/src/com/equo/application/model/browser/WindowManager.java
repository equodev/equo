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

package com.equo.application.model.browser;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.equo.ws.api.IEquoEventHandler;
import com.google.gson.Gson;

/**
 * API to perform window actions such as open new browsers.
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
   * Gets a WindowManager instance.
   * @return a WindowManager instance or null if there was an error getting the
   *         component
   */
  public static WindowManager getInstance() {
    BundleContext ctx = FrameworkUtil.getBundle(WindowManager.class).getBundleContext();
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
}
