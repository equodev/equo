package com.make.equo.application.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;

public class ParameterizedCommandHandler {

	@Execute
	public void execute(@Named("commandId") String commandId, MApplication mApplication) {
		System.out.println("sisis parametrizadoooo handler");
		Runnable runnable = (Runnable) mApplication.getTransientData().get(commandId);
		runnable.run();
	}
	
}
