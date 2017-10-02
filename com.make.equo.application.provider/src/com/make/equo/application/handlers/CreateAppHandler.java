package com.make.equo.application.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.commands.MHandler;

public class CreateAppHandler {
	
	@Inject
	MHandler mHandler;

	@Execute
	public void execute() {
		System.out.println(this);
		System.out.println("Se ejecuta el handler");
	}
}
