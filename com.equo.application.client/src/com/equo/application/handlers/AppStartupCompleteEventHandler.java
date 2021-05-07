package com.equo.application.handlers;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import com.equo.contribution.api.IEquoContributionManager;

public class AppStartupCompleteEventHandler implements EventHandler {
	private static AppStartupCompleteEventHandler instance = null;
	private Runnable runnable = null;
	private IEquoContributionManager equoContributionManager = null;

	public synchronized static AppStartupCompleteEventHandler getInstance() {
		if (instance == null) {
			instance = new AppStartupCompleteEventHandler();
		}
		return instance;
	}

	private AppStartupCompleteEventHandler() {
	}

	@Override
	public void handleEvent(final Event event) {
		if (this.equoContributionManager != null) {
			for (Runnable runnable : this.equoContributionManager.getContributionStartProcedures()) {
				runnable.run();
			}
		}
		if (this.runnable != null) {
			this.runnable.run();
		}
	}

	public void setRunnable(Runnable runnable) {
		this.runnable = runnable;
	}

	public void setEquoContributionManager(IEquoContributionManager equoContributionManager) {
		this.equoContributionManager = equoContributionManager;
	}

}