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

package com.equo.application.parts;

import static com.equo.application.util.IConstants.MAIN_URL_KEY;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.chromium.Browser;
import org.eclipse.swt.widgets.Composite;

import com.equo.comm.api.EquoCommHelper;

/**
 * Main part for Equo apps, where the browser with the web front-end is placed.
 */
public class SinglePagePart {

  @Inject
  private MPart thisPart;

  private Browser browser;

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
    int equoWsPort = EquoCommHelper.getCommService().getPort();
    if (equoAppUrl != null) {
      Composite composite = new Composite(parent, SWT.NONE);
      composite.setLayout(GridLayoutFactory.fillDefaults().create());
      browser = new Browser(composite, SWT.NONE);
      browser.setUrl(equoAppUrl + String.format("?equocommport=%s", equoWsPort));
      browser.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
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

}
