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

package com.equo.application.parts;

import static com.equo.application.util.IConstants.MAIN_URL_KEY;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.chromium.Browser;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.equo.comm.common.handler.IReceiveEventHandler;
import com.equo.comm.ws.provider.EquoWebSocketServiceImpl;
import com.equo.debug.api.DevtoolsDebugApi;

/**
 * Main part for Equo apps, where the browser with the web front-end is placed.
 */
public class SinglePagePart {

  @Inject
  private MPart thisPart;

  private Browser browser;
  
  private static boolean done;

  private static final String DEBUG_PROPERTY = "org.eclipse.swt.chromium.remote-debugging-port";

  private static final String DEBUG_PORT_PROPERTY = "chromium.port";

  @Inject
  private DevtoolsDebugApi devtool;

  @Inject
  public SinglePagePart(Composite parent) {
    //      Chromium.setCommandLine(new String[][] {
    //              new String[] {"proxy-server", "localhost:9896"},
    //              new String[] {"ignore-certificate-errors", null},
    //              new String[] {"allow-file-access-from-files", null},
    //              new String[] {"disable-web-security", null},
    //              new String[] {"enable-widevine-cdm", null},
    //              new String[] {"proxy-bypass-list", "127.0.0.1"}
    //      });
  }

  /**
   * Method called to construct the part.
   */
  @PostConstruct
  public void createControls(Composite parent) {
    String equoAppUrl = removeTrailingSlashes(thisPart.getProperties().get(MAIN_URL_KEY));
    int equoWsPort = -1;
    String debugPort = System.getProperty(DEBUG_PORT_PROPERTY);
    try {
      Optional<EquoWebSocketServiceImpl> websocket = findWsEventHandlerInstance();
      if (websocket.isPresent()) {
        equoWsPort = websocket.get().getPort();
      }
    } catch (Exception e) {
      // Could happen if the app is run without our open source IReceiveEventHandler implementation.
      // Log exception
    }
    if (equoAppUrl != null) {
      Composite composite = new Composite(parent, SWT.NONE);
      composite.setLayout(GridLayoutFactory.fillDefaults().create());
      // set debug property if it is necessary
      if (debugPort != null) {
        if (System.getProperty(DEBUG_PROPERTY).isEmpty()) {
          devtool.setDebugProperty(debugPort);
        }
      }
      browser = new Browser(composite, SWT.NONE);
      StringBuilder sb = new StringBuilder();
      sb.append(equoAppUrl);
      if (equoWsPort > 0) {
        sb.append("?equocommport=");
        sb.append(equoWsPort);
      }
      browser.setUrl(sb.toString());
      browser.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
    }

    // init debug mode if it is necessary
    if (!System.getProperty(DEBUG_PROPERTY).isEmpty()) {
      done = false;
      PaintListener paintListener = new PaintListener() {
        @Override
        public void paintControl(PaintEvent paintEvent) {
          if (!done) {
            if (browser == paintEvent.widget) {
              done = true;
              Thread thread = new Thread(() -> {
                devtool.startDebug(System.getProperty(DEBUG_PROPERTY));
              });
              thread.setDaemon(true);
              thread.start();
              browser.removePaintListener(this);
            }
          }
        }
      };
      browser.addPaintListener(paintListener);
    }
  }

  private String removeTrailingSlashes(String url) {
    if (url == null) {
      return null;
    }
    while (url.endsWith("/")) {
      url = url.substring(0, url.length() - 1);
    }
    return url;
  }

  public void loadUrl(String newUrl) {
    browser.setUrl(newUrl);
  }

  private Optional<EquoWebSocketServiceImpl> findWsEventHandlerInstance() {
    Bundle ctxBundle = FrameworkUtil.getBundle(getClass());
    if (ctxBundle != null) {
      BundleContext ctx = ctxBundle.getBundleContext();
      if (ctx != null) {
        ServiceReference<IReceiveEventHandler> serviceReference = (ServiceReference<
            IReceiveEventHandler>) ctx.getServiceReference(IReceiveEventHandler.class);
        if (serviceReference != null) {
          IReceiveEventHandler eventHandler = ctx.getService(serviceReference);
          if (eventHandler instanceof EquoWebSocketServiceImpl) {
            return Optional.of((EquoWebSocketServiceImpl) eventHandler);
          }
        }
      }
    }
    return Optional.empty();
  }

}
