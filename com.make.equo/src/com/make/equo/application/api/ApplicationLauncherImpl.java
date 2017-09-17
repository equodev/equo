package com.make.equo.application.api;

import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.application.ApplicationDescriptor;
import org.osgi.service.application.ApplicationException;
import org.osgi.service.component.annotations.Component;

import com.make.equo.application.Activator;
import com.make.equo.application.api.ApplicationLauncher;

@Component
public class ApplicationLauncherImpl implements ApplicationLauncher {

	@Override
	public void launch(Map<String, String[]> arguments) {
		System.out.println("Launching Equo App...");
		ServiceReference<?> serviceReference;
		try {
			BundleContext context = Activator.getContext();
			serviceReference = context.getAllServiceReferences("org.osgi.service.application.ApplicationDescriptor",
					"(service.pid=com.make.equo.application)")[0];
			ApplicationDescriptor app = (ApplicationDescriptor) context.getService(serviceReference);
			app.launch(arguments);
		} catch (InvalidSyntaxException | ApplicationException e1) {
			e1.printStackTrace();
		}
	}
}
