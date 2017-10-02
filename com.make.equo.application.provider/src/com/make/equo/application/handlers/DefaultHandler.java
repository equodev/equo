package com.make.equo.application.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;

import com.make.equo.application.util.IConstants;

public class DefaultHandler {
	
	@Execute
	public void execute(MHandledMenuItem handledMenuItem) {
		Runnable runnable = (Runnable) handledMenuItem.getTransientData().get(IConstants.RUNNABLE_OBJECT);
		runnable.run();
	}
}
