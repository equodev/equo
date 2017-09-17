package com.make.equo.application.client;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	@Override
	public void start(BundleContext context) throws Exception {
//		System.out.println("bundle conteeeexttt");
		Activator.context = context;
//		System.out.println("bundle conteeaaaadasdaeexttt");
//////		context.getse
//		ServiceReference<?> serviceReference = Activator.getContext().getAllServiceReferences("org.osgi.service.application.ApplicationDescriptor", "(service.pid=com.make.equo.application)")[0];
//		ApplicationDescriptor app = (ApplicationDescriptor) Activator.getContext().getService(serviceReference);
////		Display.getDefault().syncExec(new Runnable() {
////			public void run() {
////				try {
//					app.launch(null);
////				} catch (ApplicationException e) {
////					System.out.println("falla aca mal");
////					e.printStackTrace();
////				}
////			}
////		});
//		new EquoApplicationClient().start();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
	}

	public static BundleContext getContext() {
		return context;
	}
	
	public void start() {
		
	}

}
