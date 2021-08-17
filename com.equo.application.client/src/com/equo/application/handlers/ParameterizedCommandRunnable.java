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

import java.util.function.Consumer;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.IEclipseContext;

import com.equo.application.util.IConstants;

/**
 * Runnable to be executed when its associated command is called.
 */
public class ParameterizedCommandRunnable implements Consumer<String> {

  private String commandId;

  private IEclipseContext eclipseContext;

  public ParameterizedCommandRunnable(String commandId, IEclipseContext eclipseContext) {
    this.commandId = commandId;
    this.eclipseContext = eclipseContext;
  }

  @Override
  public void accept(String payload) {
    ECommandService commandService = eclipseContext.get(ECommandService.class);
    Command command = commandService.getCommand(commandId + IConstants.COMMAND_SUFFIX);
    try {
      Parameterization[] params =
          new Parameterization[] { new Parameterization(command.getParameter(commandId), payload) };
      ParameterizedCommand parametrizedCommand = new ParameterizedCommand(command, params);
      EHandlerService handlerService = eclipseContext.get(EHandlerService.class);
      handlerService.executeHandler(parametrizedCommand);
    } catch (NotDefinedException e) {
      e.printStackTrace();
    }
  }

}
