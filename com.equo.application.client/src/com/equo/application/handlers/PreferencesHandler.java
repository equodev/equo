package com.equo.application.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;

import com.equo.application.util.ICommandConstants;

public class PreferencesHandler {

	@Execute
	public void execute(MApplication mApplication) {
		Runnable runnable = (Runnable) mApplication.getTransientData().get(ICommandConstants.PREFERENCES_COMMAND);
		if (runnable != null) {
			runnable.run();
		}
	}

}