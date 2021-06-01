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

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;

import com.equo.application.util.ICommandConstants;

/**
 * Handler for 'Preferences' menu item.
 */
public class PreferencesHandler {

  /**
   * Executes the handler.
   * @param mApplication model of the current application
   */
  @Execute
  public void execute(MApplication mApplication) {
    Runnable runnable =
        (Runnable) mApplication.getTransientData().get(ICommandConstants.PREFERENCES_COMMAND);
    if (runnable != null) {
      runnable.run();
    }
  }

}
