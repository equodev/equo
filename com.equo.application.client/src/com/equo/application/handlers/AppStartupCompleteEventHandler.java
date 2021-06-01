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

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import com.equo.contribution.api.IEquoContributionManager;

/**
 * Handle code execution when application starts.
 */
public class AppStartupCompleteEventHandler implements EventHandler {
  private static AppStartupCompleteEventHandler instance = null;
  private Runnable runnable = null;
  private IEquoContributionManager equoContributionManager = null;

  /**
   * Gets the singleton instance of this class.
   * @return class instance
   */
  public static synchronized AppStartupCompleteEventHandler getInstance() {
    if (instance == null) {
      instance = new AppStartupCompleteEventHandler();
    }
    return instance;
  }

  private AppStartupCompleteEventHandler() {
  }

  @Override
  public void handleEvent(final Event event) {
    if (this.equoContributionManager != null) {
      for (Runnable runnable : this.equoContributionManager.getContributionStartProcedures()) {
        runnable.run();
      }
    }
    if (this.runnable != null) {
      this.runnable.run();
    }
  }

  public void setRunnable(Runnable runnable) {
    this.runnable = runnable;
  }

  public void setEquoContributionManager(IEquoContributionManager equoContributionManager) {
    this.equoContributionManager = equoContributionManager;
  }

}
