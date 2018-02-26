package com.make.equo.application.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

import com.make.equo.application.util.IConstants;
import com.make.equo.ws.api.EquoEventHandler;

public class ParameterizedCommandHandler {

	@Execute
	public void execute(@Named("commandId") String commandId,
			@Named(IConstants.EQUO_WEBSOCKET_USER_EMITTED_EVENT) String userEvent, MApplication mApplication,
			EModelService modelService) {
		Runnable runnable = (Runnable) mApplication.getTransientData().get(commandId);

		if (runnable != null) {
			runnable.run();
		}

		if (userEvent != null) {
			EquoEventHandler equoEventHandler = new EquoEventHandler();
			equoEventHandler.send(userEvent);
		}
	}

}
