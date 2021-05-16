package com.equo.application.handlers;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.IEclipseContext;

import com.equo.application.util.IConstants;
import com.equo.ws.api.StringPayloadEquoRunnable;

public class ParameterizedCommandRunnable implements StringPayloadEquoRunnable {

	private static final long serialVersionUID = 1L;

	private String commandId;

	private IEclipseContext eclipseContext;

	public ParameterizedCommandRunnable(String commandId, IEclipseContext eclipseContext) {
		this.commandId = commandId;
		this.eclipseContext = eclipseContext;
	}

	@Override
	public void run(String payload) {
		ECommandService commandService = eclipseContext.get(ECommandService.class);
		Command command = commandService.getCommand(commandId + IConstants.COMMAND_SUFFIX);
		try {
			Parameterization[] params = new Parameterization[] {
					new Parameterization(command.getParameter(commandId), payload) };
			ParameterizedCommand parametrizedCommand = new ParameterizedCommand(command, params);
			EHandlerService handlerService = eclipseContext.get(EHandlerService.class);
			handlerService.executeHandler(parametrizedCommand);
		} catch (NotDefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
