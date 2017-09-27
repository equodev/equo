package com.make.equo.application.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.commands.MHandler;

import com.make.equo.application.model.IMenuHandler;
import com.make.equo.application.util.IConstants;

public class DefaultHandler {
	
	@Inject
	private MHandler thisHandler;
	
	@Execute
	public void execute() {
		System.out.println("Se ejecuta el handler");
		IMenuHandler menuHandler = (IMenuHandler) thisHandler.getTransientData().get(IConstants.DEFAULT_HANDLER_IMPL_ID);
		menuHandler.execute();
	}
}
