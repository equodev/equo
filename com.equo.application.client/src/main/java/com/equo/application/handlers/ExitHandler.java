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

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.IWorkbench;

import com.equo.application.util.ICommandConstants;

/**
 * Handler for application exit.
 */
public class ExitHandler {

  /**
   * Executes the handler.
   * @param workbench    workbench instance
   * @param mApplication model of the current application
   */
  @Execute
  public void execute(IWorkbench workbench, MApplication mApplication) {
    Runnable runnable =
        (Runnable) mApplication.getTransientData().get(ICommandConstants.EXIT_COMMAND);
    if (runnable != null) {
      runnable.run();
    }
    workbench.close();
  }

}
