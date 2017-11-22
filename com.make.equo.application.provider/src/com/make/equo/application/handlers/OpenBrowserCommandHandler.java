package com.make.equo.application.handlers;

import javax.inject.Named;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;

import com.google.gson.Gson;
import com.make.equo.application.util.IConstants;
import com.make.equo.application.util.IPositionConstants;

public class OpenBrowserCommandHandler {

	@Execute
	public void execute(@Named(IConstants.EQUO_WEBSOCKET_OPEN_BROWSER) String browserParams, MApplication mApplication,
			ECommandService commandService, EHandlerService handlerService) {
		Gson gsonParser = new Gson();
		BrowserActionMessage broserParamsObject = gsonParser.fromJson(browserParams, BrowserActionMessage.class);
		BrowserParams params = broserParamsObject.getParams();
		if (params.getPosition() != null) {
			if (params.getPosition().equals(IPositionConstants.POPUP)) {
				openBrowserAsWindow(commandService, handlerService, params);
			} else {
				OpenBrowserAsSidePart(commandService, handlerService, params);
			}
		} else {
			openBrowserAsWindow(commandService, handlerService, params);
		}
	}

	private void OpenBrowserAsSidePart(ECommandService commandService, EHandlerService handlerService,
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
