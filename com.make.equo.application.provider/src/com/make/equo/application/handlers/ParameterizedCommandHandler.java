package com.make.equo.application.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.widgets.Display;

import com.make.equo.application.util.IConstants;
import com.make.equo.ws.api.IEquoEventHandler;

public class ParameterizedCommandHandler {

	@Execute
	public void execute(@Named("commandId") String commandId,
			@Named(IConstants.EQUO_WEBSOCKET_USER_EMITTED_EVENT) String userEvent, MApplication mApplication,
			EModelService modelService, IEquoEventHandler equoEventHandler) {
		Runnable runnable = (Runnable) mApplication.getTransientData().get(commandId);

		if (runnable != null) {
			Display display = Display.getDefault();
			try {
				runnable.run();
			} catch (RuntimeException exception) {
				display.getRuntimeExceptionHandler().accept(exception);
			} catch (Error error) {
				display.getErrorHandler().accept(error);
			}
		}

		if (userEvent != null) {
			equoEventHandler.send(userEvent);
		}
	}

}
