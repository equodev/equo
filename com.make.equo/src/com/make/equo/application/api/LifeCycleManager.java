package com.make.equo.application.api;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.application.ApplicationException;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

public class LifeCycleManager {
	
	@PostContextCreate
    void postContextCreate(final IEventBroker eventBroker, IApplicationContext context) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				final Shell shell = new Shell(SWT.SHELL_TRIM);

		        // register for startup completed event and close the shell
		        eventBroker.subscribe(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE,
		                new EventHandler() {
		                    @Override
		                    public void handleEvent(Event event) {
		                        shell.close();
		                        shell.dispose();
		                        eventBroker.unsubscribe(this);
		                    }
		                });
		        // close static splash screen
		        context.applicationRunning();
		        shell.open();
			}
		});
    }
}
