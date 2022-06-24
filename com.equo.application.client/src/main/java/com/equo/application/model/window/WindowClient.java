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

package com.equo.application.model.window;

import org.eclipse.swt.SWT;

import com.equo.application.handlers.BrowserParams;
import com.equo.application.util.IPositionConstants;
import com.equo.comm.api.ICommService;
import com.google.gson.Gson;

/**
 * Window manager client. 
 */
public class WindowClient {
  private BrowserParams browserParams;
  private ICommService commService;

  public WindowClient(String url, ICommService commService) {
    browserParams = new BrowserParams(url, "", IPositionConstants.POPUP, SWT.SHELL_TRIM);
    this.commService = commService;
  }

  public WindowClient(String url, String browserName, ICommService commService) {
    browserParams = new BrowserParams(url, browserName, IPositionConstants.POPUP, SWT.SHELL_TRIM);
    this.commService = commService;
  }

  public void build() {
    commService.send("openBrowser", new Gson().toJsonTree(browserParams).getAsJsonObject());
  }

  public WindowClient setUrl(String url) {
    browserParams.setUrl(url);
    return this;
  }

  public WindowClient windowName(String browserName) {
    browserParams.setName(browserName);
    return this;
  }
  
  public WindowClient popup() {
    browserParams.setPosition(IPositionConstants.POPUP);
    return this;
  }
  
  public WindowClient top() {
    browserParams.setPosition(IPositionConstants.TOP);
    return this;
  }
  
  public WindowClient right() {
    browserParams.setPosition(IPositionConstants.RIGHT);
    return this;
  }
  
  public WindowClient bottom() {
    browserParams.setPosition(IPositionConstants.BOTTOM);
    return this;
  }
  
  public WindowClient left() {
    browserParams.setPosition(IPositionConstants.LEFT);
    return this;
  }

  public WindowClient frameless() {
    browserParams.setStyle(SWT.NO_TRIM);
    return this;
  }
  
  public String getName() {
    return browserParams.getName();
  }
  
  public String getUrl() {
    return browserParams.getUrl();
  }
  
  public String getPosition() {
    return browserParams.getPosition();
  }
  
  public BrowserParams getBrowserParams() {
    return this.browserParams;
  }
}