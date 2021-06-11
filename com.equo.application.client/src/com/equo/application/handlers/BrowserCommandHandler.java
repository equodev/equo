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

package com.equo.application.handlers;

import java.util.List;
import java.util.Optional;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

import com.equo.application.util.IConstants;
import com.equo.logging.client.api.Logger;
import com.equo.logging.client.api.LoggerFactory;

/**
 * Interface of a handler for browser commands such as change browser URL or
 * open a new browser.
 */
public interface BrowserCommandHandler {
  /**
   * Check if a browser with the given name already exists.
   * @param  mApplication  model of the current application
   * @param  browserParams params of the request to open/update a browser
   * @param  modelService  Eclipse model service
   * @return               an optional with the application Part of the browser,
   *                       or an empty optional if it does not exists
   */
  default Optional<MPart> existingBrowserFor(MApplication mApplication, BrowserParams browserParams,
      EModelService modelService) {
    Logger logger = LoggerFactory.getLogger(BrowserCommandHandler.class);
    String browserName = browserParams.getName();
    if (browserName != null) {
      String browserIdSuffix = browserName.toLowerCase();
      String browserId = IConstants.EQUO_BROWSER_IN_PARTSTACK_ID + "." + browserIdSuffix;
      if (IConstants.MAIN_PART_ID.equals(browserName)) {
        browserId = IConstants.MAIN_PART_ID;
      }
      logger.debug("The browser id is " + browserId);
      List<MPart> partElements =
          modelService.findElements(mApplication, browserId, MPart.class, null);
      if (!partElements.isEmpty()) {
        return Optional.of(partElements.get(0));
      }
    }
    return Optional.empty();
  }
}
