package com.equo.application.handlers;

import java.util.Optional;

import javax.inject.Named;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

import com.equo.application.model.browser.BrowserParams;
import com.equo.application.model.browser.IPositionConstants;
import com.equo.application.util.IConstants;
import com.google.gson.Gson;

/**
 * Handler for open browser command.
 */
public class OpenBrowserCommandHandler implements BrowserCommandHandler {

  /**
   * Executes this handler.
   * @param browserParams  params of the request to open a browser
   * @param mApplication   model of the current application
   * @param commandService Eclipse command service
   * @param handlerService Eclipse handler service
   * @param modelService   Eclipse model service
   */
  @Execute
  public void execute(@Named(IConstants.EQUO_WEBSOCKET_OPEN_BROWSER) String browserParams,
      MApplication mApplication, ECommandService commandService, EHandlerService handlerService,
      EModelService modelService) {
    Gson gsonParser = new Gson();
    BrowserParams params = gsonParser.fromJson(browserParams, BrowserParams.class);
    if (params.getPosition() != null) {
      if (params.getPosition().equals(IPositionConstants.POPUP)) {
        openBrowserAsWindow(commandService, handlerService, params);
      } else {
        Optional<MPart> existingBrowser = existingBrowserFor(mApplication, params, modelService);
        if (existingBrowser.isPresent()) {
          executeUpdateBrowserCommand(browserParams, commandService, handlerService);
        } else {
          openBrowserAsSidePart(commandService, handlerService, params);
        }
      }
    } else {
      openBrowserAsWindow(commandService, handlerService, params);
    }
  }

  private void executeUpdateBrowserCommand(String browserParams, ECommandService commandService,
      EHandlerService handlerService) {
    String commandParameterId = IConstants.EQUO_WEBSOCKET_UPDATE_BROWSER;
    Command command = commandService.getCommand(commandParameterId + IConstants.COMMAND_SUFFIX);
    Parameterization[] commandParams;
    try {
      commandParams = new Parameterization[] {
          new Parameterization(command.getParameter(commandParameterId), browserParams) };
      ParameterizedCommand parametrizedCommand = new ParameterizedCommand(command, commandParams);
      handlerService.executeHandler(parametrizedCommand);
    } catch (NotDefinedException e) {
      // TODO log exception, must not reach this state.
      e.printStackTrace();
    }
  }

  private void openBrowserAsSidePart(ECommandService commandService, EHandlerService handlerService,
      BrowserParams browserParams) {
    String commandParameterId = IConstants.EQUO_OPEN_BROWSER_AS_SIDE_PART;
    Command command = commandService.getCommand(commandParameterId + IConstants.COMMAND_SUFFIX);
    Parameterization[] params;
    try {
      params = new Parameterization[] {
          new Parameterization(command.getParameter(commandParameterId), browserParams.getUrl()),
          new Parameterization(command.getParameter(IConstants.EQUO_BROWSER_PART_NAME),
              browserParams.getName()),
          new Parameterization(command.getParameter(IConstants.EQUO_BROWSER_PART_POSITION),
              browserParams.getPosition()) };
      ParameterizedCommand parametrizedCommand = new ParameterizedCommand(command, params);
      handlerService.executeHandler(parametrizedCommand);
    } catch (NotDefinedException e) {
      // TODO log exception, must not reach this state.
      e.printStackTrace();
    }
  }

  private void openBrowserAsWindow(ECommandService commandService, EHandlerService handlerService,
      BrowserParams browserParams) {
    String commandParameterId = IConstants.EQUO_OPEN_BROWSER_AS_WINDOW;
    Command command = commandService.getCommand(commandParameterId + IConstants.COMMAND_SUFFIX);
    Parameterization[] params;
    try {
      params = new Parameterization[] {
          new Parameterization(command.getParameter(commandParameterId), browserParams.getUrl()),
          new Parameterization(command.getParameter(IConstants.EQUO_BROWSER_WINDOW_NAME),
              browserParams.getName()) };
      ParameterizedCommand parametrizedCommand = new ParameterizedCommand(command, params);
      handlerService.executeHandler(parametrizedCommand);
    } catch (NotDefinedException e) {
      // TODO log exception, must not reach this state.
      e.printStackTrace();
    }
  }
}
