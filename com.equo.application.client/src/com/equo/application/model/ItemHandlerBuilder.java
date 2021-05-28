package com.equo.application.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandParameter;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;

import com.equo.application.impl.HandlerBuilder;
import com.equo.application.util.IConstants;
import com.google.common.collect.Lists;

/**
 * Builder for the handler associated to a menu item.
 */
public class ItemHandlerBuilder extends HandlerBuilder {

  protected Runnable runnable;
  protected String userEvent;
  protected ItemBuilder itemBuilder;

  ItemHandlerBuilder(ItemBuilder itemBuilder) {
    super(itemBuilder.getOptionalFieldBuilder().getEquoApplicationBuilder().getmApplication(),
        IConstants.COMMAND_ID_PARAMETER, IConstants.PARAMETERIZED_COMMAND_CONTRIBUTION_URI);
    this.itemBuilder = itemBuilder;
  }

  /**
   * Sets the action to be executed when the item gets clicked.
   * @param  runnable  runnable to be executed.
   * @param  userEvent name of the ws event to be executed.
   * @return           the ItemBuilder instance.
   */
  public ItemBuilder onClick(Runnable runnable, String userEvent) {
    this.userEvent = userEvent;
    onClick(runnable);
    return this.itemBuilder;
  }

  private ItemBuilder onClick(Runnable runnable) {
    this.runnable = runnable;
    MHandledItem item = getItemBuilder().getItem();
    item.getTransientData().put(IConstants.ITEM_RUNNABLE, runnable);
    item.getTransientData().put(IConstants.ITEM_ACTION, userEvent);

    String id = item.getElementId();

    MCommand newCommand = createCommandAndHandler(id);

    item.setCommand(newCommand);
    String commandId = newCommand.getElementId();

    MParameter commandIdparameter = createMParameter(IConstants.COMMAND_ID_PARAMETER, commandId);
    item.getParameters().add(commandIdparameter);

    MParameter userEventParameter =
        createMParameter(IConstants.EQUO_WEBSOCKET_USER_EMITTED_EVENT, userEvent);
    item.getParameters().add(userEventParameter);

    addCommandToHandler(item, newCommand);

    return getItemBuilder();
  }

  protected void addCommandToHandler(MHandledItem item, MCommand command) {
    IEclipseContext eclipseContext = getMApplication().getContext();
    if (eclipseContext == null) {
      return;
    }
    ECommandService commandService = eclipseContext.get(ECommandService.class);
    if (commandService != null) {
      Map<String, Object> parameters = new HashMap<>(4);
      for (MParameter param : item.getParameters()) {
        parameters.put(param.getName(), param.getValue());
      }
      ParameterizedCommand parmCmd =
          commandService.createCommand(command.getElementId(), parameters);
      item.setWbCommand(parmCmd);
    }
  }

  ItemBuilder getItemBuilder() {
    return this.itemBuilder;
  }

  /**
   * Sets a shortcut for the current menu item.
   * @param keySequence string format of the shortcut
   */
  public ItemBuilder addShortcut(String keySequence) {
    itemBuilder.getItem().getTransientData().put(IConstants.ITEM_SHORTCUT, keySequence);
    new ItemShortcutBuilder(this.getItemBuilder(), userEvent).addShortcut(keySequence);
    EquoApplicationBuilder equoApplicationBuilder =
        getItemBuilder().getOptionalFieldBuilder().getEquoApplicationBuilder();
    new GlobalShortcutBuilder(equoApplicationBuilder, getItemBuilder().getItem().getElementId(),
        this.runnable, this.userEvent).addGlobalShortcut(keySequence);
    return getItemBuilder();
  }

  /**
   * Add a new toolbar into the app.
   * @return a builder with which to create a new toolbar
   */
  public ToolbarBuilder withToolbar() {
    return new ToolbarBuilder(getItemBuilder().getOptionalFieldBuilder(),
        getItemBuilder().getOptionalFieldBuilder().getEquoApplicationBuilder().getmWindow())
            .addToolbar();
  }

  public MenuBuilder withMainMenu(String menuLabel) {
    return new MenuBuilder(getItemBuilder().getOptionalFieldBuilder()).addMenu(menuLabel);
  }

  @Override
  protected List<MCommandParameter> createCommandParameters() {
    MCommandParameter windowNameCommandParameter = createCommandParameter(
        IConstants.EQUO_WEBSOCKET_USER_EMITTED_EVENT, "User emitted event", true);
    return Lists.newArrayList(windowNameCommandParameter);
  }

  @Override
  protected Runnable getRunnable() {
    return runnable;
  }

}
