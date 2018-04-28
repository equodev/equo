package com.make.equo.application.impl;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public enum EnterFullScreenModeRunnable implements Runnable {
	
	instance;

	private boolean fullScreen = false;

	@Override
	public void run() {
		Display defaultDisplay = Display.getDefault();
		fullScreen = !fullScreen;
		if (defaultDisplay != null) {
			Shell activeShell = defaultDisplay.getActiveShell();
			if (activeShell != null) {
				activeShell.setFullScreen(fullScreen);
			}
		}
	}

}
