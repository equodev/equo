package com.make.equo.application.handlers;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

public class AppStartupCompleteEventHandler implements EventHandler {
	private static AppStartupCompleteEventHandler instance = null;
	private Runnable runnable = null;

	public static AppStartupCompleteEventHandler getOrCreate() {
		if (instance == null) {
			instance = new AppStartupCompleteEventHandler();
		}
		return instance;
	}

	public static AppStartupCompleteEventHandler getInstance() {
		return instance;
	}

	private AppStartupCompleteEventHandler() {
	}

	@Override
	public void handleEvent(final Event event) {
		this.runnable.run();
	}

	public void setRunnable(Runnable runnable) {
		this.runnable = runnable;
	}

}