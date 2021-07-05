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

import java.util.List;
import java.util.Optional;

import org.eclipse.e4.ui.model.application.commands.MBindingTable;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MKeyBinding;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;

import com.equo.application.util.IConstants;
import com.equo.logging.client.api.Logger;
import com.equo.logging.client.api.LoggerFactory;

/**
 * Builder for the shortcut associated to a menu item.
 */
public class ItemShortcutBuilder implements KeyBindingBuilder {
  protected static final Logger logger = LoggerFactory.getLogger(ItemShortcutBuilder.class);

  private ItemBuilder itemBuilder;
  private String userEvent;

  ItemShortcutBuilder(ItemBuilder itemBuilder, String userEvent) {
    this.itemBuilder = itemBuilder;
    this.userEvent = userEvent;
  }

  void addShortcut(String shortcut) {
    Optional<MBindingTable> bindingTable =
        getDefaultBindingTable(itemBuilder.getOptionalFieldBuilder().getEquoApplicationBuilder());
    if (bindingTable.isPresent()) {
      MBindingTable mBindingTable = bindingTable.get();
      MHandledItem toolItem = itemBuilder.getItem();
      MCommand command = toolItem.getCommand();

      MKeyBinding keyBinding = createKeyBinding(command, shortcut);

      MParameter commandParameterId =
          createMParameter(IConstants.COMMAND_ID_PARAMETER, command.getElementId());
      keyBinding.getParameters().add(commandParameterId);
      MParameter userEventParameter =
          createMParameter(IConstants.EQUO_COMM_USER_EMITTED_EVENT, this.userEvent);
      keyBinding.getParameters().add(userEventParameter);

      List<MKeyBinding> bindings = mBindingTable.getBindings();
      removeShortcut(bindings, shortcut);
      bindings.add(keyBinding);
    } else {
      logger.debug("There is no default binding table created for the " + shortcut + " shortcut");
    }
  }

  private void removeShortcut(List<MKeyBinding> bindings, String shortcut) {
    MKeyBinding bindingToDelete = null;
    for (MKeyBinding binding : bindings) {
      if (binding.getKeySequence().equals(shortcut)) {
        bindingToDelete = binding;
        break;
      }
    }
    if (bindingToDelete != null) {
      bindings.remove(bindingToDelete);
    }
  }

}
