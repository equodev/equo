package com.make.equo.application.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.make.equo.application.util.ICommandConstants;

public class AboutHandler {

	@Execute
	public void execute(Shell shell, MApplication mApplication) {
		Runnable runnable = (Runnable) mApplication.getTransientData().get(ICommandConstants.ABOUT_COMMAND);
		if (runnable != null) {
			runnable.run();
		}
	}

}
