package com.make.equo.api;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.application.ApplicationDescriptor;
import org.osgi.service.application.ApplicationException;

import com.make.equo.Activator;

public class EquoApplicationStarter {

	public static void start() {
		ServiceReference<?> serviceReference;
		try {
			BundleContext context = Activator.getContext();
			serviceReference = context.getAllServiceReferences(
					"org.osgi.service.application.ApplicationDescriptor", "(service.pid=com.make.equo.application)")[0];
			ApplicationDescriptor app = (ApplicationDescriptor) context.getService(serviceReference);
			app.launch(null);
		} catch (InvalidSyntaxException e) {
			// TODO Auto-generated catch block
			System.out.println("error aca en invalid syntax ex");
			e.printStackTrace();
		} catch (ApplicationException e) {
			System.out.println("error aca en ApplicationException");
			e.printStackTrace();
		}
	}
}
