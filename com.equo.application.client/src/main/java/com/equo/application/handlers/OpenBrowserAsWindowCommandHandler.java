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

package com.equo.application.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;

import com.equo.application.util.IConstants;

/**
 * Handler for open browser in a new window.
 */
public class OpenBrowserAsWindowCommandHandler {

  /**
   * Executes the handler.
   * @param url          address to be loaded in the new browser
   * @param windowName   name of the new window. If null, the new window will have
   *                     no title bar
   * @param mApplication model of the current application
   * @param sync         toolkit to synchronize back into the UI-Thread
   */
  @Execute
  public void execute(@Named(IConstants.EQUO_OPEN_BROWSER_AS_WINDOW) String url,
      @Named(IConstants.EQUO_BROWSER_WINDOW_NAME) String windowName, MApplication mApplication,
      UISynchronize sync) {
    sync.syncExec(() -> {
      MTrimmedWindow windowToOpen = MBasicFactory.INSTANCE.createTrimmedWindow();
      if (windowName != null) {
        windowToOpen.setLabel(windowName);
      } else {
        //remove title bar
      }
      mApplication.getChildren().add(windowToOpen);

      MPart part = MBasicFactory.INSTANCE.createPart();
      part.setElementId(IConstants.MAIN_PART_ID + "." + windowName != null ? windowName : "popup");
      part.setContributionURI(IConstants.SINGLE_PART_CONTRIBUTION_URI);
      part.getProperties().put(IConstants.MAIN_URL_KEY, url);

      windowToOpen.getChildren().add(part);

      windowToOpen.setVisible(true);
      windowToOpen.setOnTop(true);
      windowToOpen.setToBeRendered(true);
    });
  }
}
