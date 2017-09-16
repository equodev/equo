package com.make.equo.application;

import org.eclipse.swt.widgets.Display;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.application.ApplicationDescriptor;
import org.osgi.service.application.ApplicationException;
import org.osgi.service.component.annotations.Component;

import com.make.equo.application.api.ApplicationLauncher;

@Component
public class ApplicationLauncherImpl implements ApplicationLauncher {

	@Override
	public void launch(BundleContext context) {
		System.out.println("bundle conteeeexttt");
//		Activator.context = context;
//		System.out.println("bundle conteeaaaadasdaeexttt");
////		context.getse
		ServiceReference<?> serviceReference;
		try {
			serviceReference = context.getAllServiceReferences("org.osgi.service.application.ApplicationDescriptor", "(service.pid=com.make.equo.application)")[0];
			ApplicationDescriptor app = (ApplicationDescriptor) context.getService(serviceReference);
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					try {
						app.launch(null);
					} catch (ApplicationException e) {
						System.out.println("falla aca mal");
						e.printStackTrace();
					}
				}
			});
//			app.launch(null);
//			Display.getDefault().syncExec(new Runnable() {
//				public void run() {
//					try {
//						
//					} catch (ApplicationException e) {
//						System.out.println("falla aca mal");
//						e.printStackTrace();
//					}
//				}
//			});
		} catch (InvalidSyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
