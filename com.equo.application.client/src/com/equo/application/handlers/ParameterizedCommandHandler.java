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

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.widgets.Display;

import com.equo.application.util.IConstants;
import com.equo.comm.api.IEquoEventHandler;

/**
 * Handler for parameterized commands.
 */
public class ParameterizedCommandHandler {

  /**
   * Executes the handler.
   * @param commandId        command to be executed
   * @param userEvent        event to respond once the command has been executed
   * @param mApplication     model of the current application
   * @param modelService     Eclipse model service
   * @param equoEventHandler handler for comm events.
   */
  @Execute
  public void execute(@Named("commandId") String commandId,
      @Named(IConstants.EQUO_COMM_USER_EMITTED_EVENT) String userEvent,
      MApplication mApplication, EModelService modelService, IEquoEventHandler equoEventHandler) {
    Runnable runnable = (Runnable) mApplication.getTransientData().get(commandId);

    if (runnable != null) {
      Display display = Display.getDefault();
      try {
        runnable.run();
      } catch (RuntimeException exception) {
        display.getRuntimeExceptionHandler().accept(exception);
      } catch (Error error) {
        display.getErrorHandler().accept(error);
      }
    }

    if (userEvent != null) {
      equoEventHandler.send(userEvent);
    }
  }

}
