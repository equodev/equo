package com.make.equo.application.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.IWorkbench;

import com.make.equo.application.util.ICommandConstants;

public class ExitHandler {

	@Execute
	public void execute(IWorkbench workbench, MApplication mApplication) {
		Runnable runnable = (Runnable) mApplication.getTransientData().get(ICommandConstants.EXIT_COMMAND);
		if (runnable != null) {
			runnable.run();
		}
		workbench.close();
	}

}
