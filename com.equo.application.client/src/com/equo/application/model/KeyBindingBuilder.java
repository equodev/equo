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

package com.equo.application.model;

import java.util.Optional;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MBindingTable;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.commands.MKeyBinding;

import com.equo.application.impl.MParameterBuilder;
import com.equo.application.util.IConstants;

interface KeyBindingBuilder extends MParameterBuilder {

  public static final String KEYBINDING_SUFFIX = ".keybinding";

  default MKeyBinding createKeyBinding(MCommand command, String shortcut) {
    MKeyBinding keyBinding = MCommandsFactory.INSTANCE.createKeyBinding();
    keyBinding.setKeySequence(shortcut);
    String commandId = command.getElementId();
    keyBinding.setElementId(commandId + KEYBINDING_SUFFIX);
    keyBinding.getTags().add(IConstants.USER_KEY_BINDING_TAG);
    keyBinding.setCommand(command);
    return keyBinding;
  }

  default Optional<MBindingTable>
      getDefaultBindingTable(EquoApplicationBuilder equoApplicationBuilder) {
    MApplication mApplication = equoApplicationBuilder.getmApplication();
    for (MBindingTable mBindingTable : mApplication.getBindingTables()) {
      if (mBindingTable.getElementId().equals(IConstants.DEFAULT_BINDING_TABLE)) {
        return Optional.of(mBindingTable);
      }
    }
    return Optional.empty();
  }

}
