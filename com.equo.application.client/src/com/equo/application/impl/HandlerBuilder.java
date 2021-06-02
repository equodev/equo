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

package com.equo.application.impl;

import java.util.Collections;
import java.util.List;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandParameter;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.commands.MHandler;
import org.eclipse.e4.ui.model.application.commands.impl.CommandsFactoryImpl;

import com.equo.application.util.IConstants;

/**
 * Builder to create new handlers for custom parameterized commands.
 */
public abstract class HandlerBuilder implements MParameterBuilder {

  private String commandParameterId;
  private String contributionUri;
  private MApplication mApplication;

  protected HandlerBuilder(MApplication mApplication, String commandParameterId,
      String contributionUri) {
    this.mApplication = mApplication;
    this.commandParameterId = commandParameterId;
    this.contributionUri = contributionUri;
  }

  /**
   * Allows subclasses to add multiple command parameters.
   * @return list of parameters to add into new created commands
   */
  protected List<MCommandParameter> createCommandParameters() {
    return Collections.emptyList();
  }

  protected MCommandParameter createCommandParameter(String id, String name, boolean isOptional) {
    MCommandParameter commandParameter = MCommandsFactory.INSTANCE.createCommandParameter();
    commandParameter.setElementId(id);
    commandParameter.setName(name);
    commandParameter.setOptional(isOptional);
    return commandParameter;
  }

  private MHandler createNewHandler(String id, String contributionUri) {
    MHandler newHandler = CommandsFactoryImpl.eINSTANCE.createHandler();
    newHandler.setElementId(id + IConstants.HANDLER_SUFFIX);
    newHandler.setContributionURI(contributionUri);
    return newHandler;
  }

  private MCommand createNewCommand(String id) {
    MCommand newCommand = CommandsFactoryImpl.eINSTANCE.createCommand();
    newCommand.setElementId(id + IConstants.COMMAND_SUFFIX);
    newCommand.setCommandName(id + IConstants.COMMAND_SUFFIX);
    return newCommand;
  }

  /**
   * Creates a new command and an associated handler.
   * @param  id command id
   * @return    new created command
   */
  public MCommand createCommandAndHandler(String id) {
    MCommand newCommand = createNewCommand(id);
    MCommandParameter commandParameter =
        createCommandParameter(commandParameterId, "Command Parameter Name", false);
    newCommand.getParameters().add(commandParameter);
    newCommand.getParameters().addAll(createCommandParameters());

    MHandler newHandler = createNewHandler(id, contributionUri);
    newHandler.setCommand(newCommand);

    mApplication.getCommands().add(newCommand);
    mApplication.getHandlers().add(newHandler);

    mApplication.getTransientData().put(newCommand.getElementId(), getRunnable());

    return newCommand;
  }

  protected MApplication getMApplication() {
    return mApplication;
  }

  protected abstract Runnable getRunnable();

}
