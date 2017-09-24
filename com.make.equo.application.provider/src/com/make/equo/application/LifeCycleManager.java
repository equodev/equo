package com.make.equo.application;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.equinox.app.IApplicationContext;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.make.equo.application.api.ApplicationModelService;

public class LifeCycleManager {
	
	@ProcessAdditions
    void postContextCreate(IApplicationContext applicationContext, MApplication mainApplication) {
		// ServiceReference<?> serviceReference =
				// context.getServiceReference("com.make.equo.application.api.ApplicationLauncher");
				// ApplicationLauncher app= (ApplicationLauncher)
				// context.getService(serviceReference);
		System.out.println("viene bien pasa por lifecycle");
		BundleContext context = Activator.getContext();
		ServiceReference<?> serviceReference = context.getServiceReference(ApplicationModelService.class.getName());
		ApplicationModelService service = (ApplicationModelService) context.getService(serviceReference);
		mainApplication.getChildren().remove(0);
		mainApplication.getChildren().add(service.getMainWindow());
		System.out.println("viene bien terminaaa lifecycle");
//		Display.getDefault().syncExec(new Runnable() {
//			public void run() {
//				final Shell shell = new Shell(SWT.SHELL_TRIM);
//
//		        // register for startup completed event and close the shell
//		        eventBroker.subscribe(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE,
//		                new EventHandler() {
//		                    @Override
//		                    public void handleEvent(Event event) {
//		                        shell.close();
//		                        shell.dispose();
//		                        eventBroker.unsubscribe(this);
//		                    }
//		                });
//		        // close static splash screen
//		        context.applicationRunning();
//		        shell.open();
//			}
//		});
    }
}
