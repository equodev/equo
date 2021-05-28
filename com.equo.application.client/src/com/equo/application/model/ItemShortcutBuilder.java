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
          createMParameter(IConstants.EQUO_WEBSOCKET_USER_EMITTED_EVENT, this.userEvent);
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
