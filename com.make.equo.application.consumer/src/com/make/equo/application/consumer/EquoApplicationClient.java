package com.make.equo.application.consumer;

import com.make.equo.application.api.ApplicationLauncher;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.adaptor.EclipseStarter;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

public class EquoApplicationClient {

//	private ApplicationLauncher applicationLauncherService;
//
//	public void equoClient() {
//		 System.out.println("El app launcher es" + applicationLauncherService);
//	}
//
//	public synchronized void setApplicationLauncherService(ApplicationLauncher applicationLauncherService) {
//		System.out.println("Service was set. Thank you DS!");
//		this.applicationLauncherService = applicationLauncherService;
//	}
//	//
//	public synchronized void unsetApplicationLauncherService(ApplicationLauncher applicationLauncherService) {
//		System.out.println("Service was unset. Why did you do this to me?");
//        if (this.applicationLauncherService == applicationLauncherService) {
//            this.applicationLauncherService = null;
//        }
//	}

//	@SuppressWarnings("restriction")
	public static Object start() throws Exception {
		// String[] equinoxArgs = {"-console","1234","-noExit"};
		// BundleContext context = EclipseStarter.startup(equinoxArgs,null);

		Map<String, String> initProps = new HashMap<String, String>();

		//// initProps.put("osgi.splashPath", "platform:/base/plugins/com.make.equo");
		// // corresponding to generated config.ini
		//
		// initProps.put("eclipse.product", "equo.product");
		//
		initProps.put("osgi.clean", "true");
		initProps.put("osgi.dev", "true");
		// initProps.put("osgi.debug", "true");
		////
		initProps.put("osgi.install.area", "this.installLocation");
		initProps.put("osgi.instance.area", "this.workspace");
		initProps.put("osgi.configuration.area", "this.config");

		//
		initProps.put("osgi.bundles",
				"org.eclipse.swt.examples.addressbook,\n" + "com.github.jnr.ffi,\n" + "      com.github.jnr.jffi,\n"
						+ "      com.github.jnr.jffi.native,\n" + "      com.github.jnr.x86asm,\n"
						+ "      com.ibm.icu,\n" + "      com.make.cef,\n" + "      com.make.cef.osx.x86_64,\n" +
						// " com.make.equo,\n" +
						"      javax.annotation,\n" + "      javax.inject,\n" + "      javax.xml,\n"
						+ "      org.apache.batik.css,\n" + "      org.apache.batik.util,\n"
						+ "      org.apache.batik.util.gui,\n" + "      org.apache.commons.jxpath,\n"
						+ "      org.apache.commons.logging,\n" + "      org.apache.felix.gogo.command,\n"
						+ "      org.apache.felix.gogo.runtime,\n" + "      org.apache.felix.scr,\n"
						+ "      org.eclipse.core.commands,\n" + "      org.eclipse.core.contenttype,\n"
						+ "      org.eclipse.core.databinding,\n" + "      org.eclipse.core.databinding.beans,\n"
						+ "      org.eclipse.core.databinding.observable,\n"
						+ "      org.eclipse.core.databinding.property,\n" + "      org.eclipse.core.expressions,\n"
						+ "      org.eclipse.core.filesystem,\n" + "      org.eclipse.core.filesystem.macosx,\n"
						+ "      org.eclipse.core.jobs,\n" + "      org.eclipse.core.resources,\n"
						+ "      org.eclipse.core.runtime@start,\n" + "      org.eclipse.e4.core.commands,\n"
						+ "      org.eclipse.e4.core.contexts,\n" + "      org.eclipse.e4.core.di,\n"
						+ "      org.eclipse.e4.core.di.annotations,\n" + "      org.eclipse.e4.core.di.extensions,\n"
						+ "      org.eclipse.e4.core.di.extensions.supplier,\n"
						+ "      org.eclipse.e4.core.services,\n" + "      org.eclipse.e4.emf.xpath,\n"
						+ "      org.eclipse.e4.ui.bindings,\n" + "      org.eclipse.e4.ui.css.core,\n"
						+ "      org.eclipse.e4.ui.css.swt,\n" + "      org.eclipse.e4.ui.css.swt.theme,\n"
						+ "      org.eclipse.e4.ui.di,\n" + "      org.eclipse.e4.ui.model.workbench,\n"
						+ "      org.eclipse.e4.ui.services,\n" + "      org.eclipse.e4.ui.widgets,\n"
						+ "      org.eclipse.e4.ui.workbench,\n" + "      org.eclipse.e4.ui.workbench.renderers.swt,\n"
						+ "      org.eclipse.e4.ui.workbench.renderers.swt.cocoa,\n"
						+ "      org.eclipse.e4.ui.workbench.swt,\n" + "      org.eclipse.e4.ui.workbench3,\n"
						+ "      org.eclipse.emf.common,\n" + "      org.eclipse.emf.databinding,\n"
						+ "      org.eclipse.emf.ecore,\n" + "      org.eclipse.emf.ecore.change,\n"
						+ "      org.eclipse.emf.ecore.xmi,\n" + "      org.eclipse.equinox.app,\n"
						+ "      org.eclipse.equinox.common,\n" + "      org.eclipse.equinox.concurrent,\n"
						+ "      org.eclipse.equinox.ds@start,\n" +
						// " org.eclipse.equinox.p2.reconciler.dropins@start,\n" +
						"      org.eclipse.equinox.event@start,\n" + "      org.eclipse.equinox.preferences,\n"
						+ "      org.eclipse.equinox.registry,\n" + "      org.eclipse.equinox.util,\n"
						+ "      org.eclipse.jface,\n" + "      org.eclipse.jface.databinding,\n"
						+ "      org.eclipse.osgi,\n" + "      org.eclipse.osgi.compatibility.state,\n"
						+ "      org.eclipse.osgi.services,\n" + "      org.eclipse.osgi.util,\n"
						+ "      org.eclipse.swt,\n" + "      org.eclipse.swt.cocoa.macosx.x86_64,\n"
						+ "      org.objectweb.asm,\n" + "      org.objectweb.asm.analysis,\n"
						+ "      org.objectweb.asm.commons,\n" + "      org.objectweb.asm.tree,\n"
						+ "      org.objectweb.asm.util,\n" + "      org.w3c.css.sac,\n" + "      org.w3c.dom.events,\n"
						+ "      org.w3c.dom.smil,\n" + "      org.w3c.dom.svg,");
		initProps.put("osgi.bundles.defaultStartLevel", "1");
		// according to config.ini
		initProps.put("osgi.instance.area", "/Users/seba/eclipse-workspace/../runtime-equo.product");
		initProps.put("osgi.dev",
				"file:/Users/seba/eclipse-workspace/.metadata/.plugins/org.eclipse.pde.core/equo.product/dev.properties");
		initProps.put("osgi.os", "macosx");
		initProps.put("osgi.ws", "cocoa");
		initProps.put("osgi.arch", "x86_64");
		initProps.put("eclipse.consoleLog", "true");
		initProps.put("osgi.os", "macosx");
		initProps.put("eclipse.consoleLog", "true");
		initProps.put("eclipse.application", "com.make.equo.application");
//		initProps.put("osgi.parentClassloader","app");

		// initProps.put("osgi.user.area", "this.userArea");
		// initProps.put("osgi.noShutdown", "false");

		// initProps.put("eclipse.ignoreApp", "true");
		
		initProps.put("eclipse.application.launchDefault", "true");
		initProps.put("eclipse.allowAppRelaunch", "true");
		// osgiWayRun(initProps);
		EclipseStarter.setInitialProperties(initProps);

		// "-consoleLog", "-clearPersistedState", "-application",
		// "com.make.equo.application"};
		// String[] equinoxArgs = {"-product", "com.make.equo.product", "-data",
		// "/Users/seba/eclipse-workspace/../runtime-equo.product", "-dev",
		// "file:/Users/seba/eclipse-workspace/.metadata/.plugins/org.eclipse.pde.core/equo.product/dev.properties",
		// "-os", "macosx", "-ws", "cocoa", "-arch", "x86_64", "-consoleLog",
		// "-clearPersistedState", "-application",
		// "org.eclipse.e4.ui.workbench.swt.E4Application"};
		String[] equinoxArgs = { "-data", "/Users/seba/eclipse-workspace/../runtime-equo.product", "-dev",
				"file:/Users/seba/eclipse-workspace/.metadata/.plugins/org.eclipse.pde.core/equo.product/dev.properties",
				"-os", "macosx", "-ws", "cocoa", "-arch", "x86_64", "-consoleLog", "-clearPersistedState",
				"-application", "com.make.equo.application"};
		// EclipseStarter.run(equinoxArgs, null);
		//
		BundleContext context = EclipseStarter.startup(equinoxArgs, null);
		//
		// Bundle installBundle =
		// context.installBundle("file:/Users/seba/Desktop/plugins/product/repository/plugins/com.make.equo_1.0.0.201709120819.jar");
		Bundle apiBundle = context
				.installBundle("file:/Users/seba/Desktop/plugins/com.make.equo.application.api_1.0.0.jar");
		Bundle providerBundle = context.installBundle(
				"file:/Users/seba/Desktop/plugins/com.make.equo.application.provider_1.0.0.jar");

//		apiBundle.start();
		providerBundle.start();

		// ServiceReference<?> serviceReference =
		// context.getServiceReference("com.make.equo.application.api.ApplicationLauncher");
		// ApplicationLauncher app= (ApplicationLauncher)
		// context.getService(serviceReference);
		// app.launch(null);
		// Display.getDefault().syncExec(new Runnable() {
		// public void run() {
		// try {

//		ServiceReference reference = context
//				.getServiceReference(com.make.equo.application.api.ApplicationLauncher.class.getName());
//		com.make.equo.application.api.ApplicationLauncher service = (com.make.equo.application.api.ApplicationLauncher) context.getService(reference);
//		service.launch(context);

		ServiceTracker<ApplicationLauncher, Object> serviceTracker = new ApplicationLauncherServiceTracker(context);
		serviceTracker.open();
		ApplicationLauncher service2 = (ApplicationLauncher) serviceTracker.getService();
		Map<String, Object> appArguments = new HashMap<>();
		// appArguments.put("eclipse.application.default", true);
		service2.launch(context);

		// Class<? extends Object> class1 = context.getService(reference).getClass();
		// System.out.println(class1.getClassLoader());
		// apiBundle.loadClass(com.make.equo.application.api.ApplicationLauncher.class.getName());
		// System.out.println(com.make.equo.application.api.ApplicationLauncher.class.getClassLoader());
		// com.make.equo.application.api.ApplicationLauncher service =
		// (com.make.equo.application.api.ApplicationLauncher)
		// context.getService(reference);
		// service.launch(null);

		// System.out.println("class loader " +
		// serviceReference.getClass().getClassLoader());
		// System.out.println(bundle.getBundleContext().getService(serviceReference).getClass().getClassLoader());
		// System.out.println(Thread.currentThread().getContextClassLoader());
		// Thread.currentThread().setContextClassLoader(bundle.getBundleContext().getService(serviceReference).getClass().getClassLoader());
		// System.out.println(Thread.currentThread().getContextClassLoader());
		// System.out.println(Thread.currentThread().getContextClassLoader());

		// installBundle.getBundleContext().getBundle();
		//
//		 runn(context);

		// context.getBundle().start();
		// System.out.println("sisisi pasa por aca antes del run");
		//
		// System.out.println("bundles: " + context.getBundles()[1]);
		// _startApp(context);
		// context.get
		// Bundle bundle = context.installBundle(
		// "file:/Users/seba/Desktop/plugins/com.make.equo_1.0.0.201709120819.jar");
		// bundle.start();
		// for (ServiceReference<?> s : context.getBundle().getRegisteredServices()) {
		// System.out.println("el registers services es " + s.toString());
		// }
		// System.out.println("los registers services son " +
		// context.getBundle().getRegisteredServices()[0] + ",
		// context.getBundle().getRegisteredServices()[1]");
		// bundle.getBundleContext().registerService("org.osgi.service.application.ApplicationDescriptor",
		// new E4Application(), null);
		//
		// try {
		// ServiceReference<?> serviceReference =
		// bundle.getBundleContext().getAllServiceReferences(
		// "org.osgi.service.application.ApplicationDescriptor",
		// "(service.pid=com.make.equo.application)")[0];
		// ApplicationDescriptor app = (ApplicationDescriptor)
		// context.getService(serviceReference);
		// System.out.println("ejecutando app");
		// app.launch(null);
		// } catch (InvalidSyntaxException e) {
		// // TODO Auto-generated catch block
		// System.out.println("error aca en invalid syntax ex");
		// e.printStackTrace();
		// } catch (ApplicationException e) {
		// System.out.println("error aca en ApplicationException");
		// e.printStackTrace();
		// }

		// Map initProps = new HashMap ();
		//
		//// initProps.put("osgi.splashPath", "platform:/base/plugins/com.make.equo");
		// // corresponding to generated config.ini
		//
		// initProps.put("eclipse.product", "com.make.equo.product");
		//
		// initProps.put("osgi.clean", "true");
		// initProps.put("osgi.dev", "true");
		//// initProps.put("osgi.debug", "true");
		//
		//// initProps.put("osgi.install.area", this.installLocation);
		//// initProps.put("osgi.instance.area", this.workspace);
		//// initProps.put("osgi.configuration.area", this.config);
		//
		//
		// initProps.put("osgi.bundles",
		// "com.github.jnr.ffi,\n" +
		// " com.github.jnr.jffi,\n" +
		// " com.github.jnr.jffi.native,\n" +
		// " com.github.jnr.x86asm,\n" +
		// " com.ibm.icu,\n" +
		// " com.make.cef,\n" +
		// " com.make.cef.osx.x86_64,\n" +
		// " com.make.equo,\n" +
		// " javax.annotation,\n" +
		// " javax.inject,\n" +
		// " javax.xml,\n" +
		// " org.apache.batik.css,\n" +
		// " org.apache.batik.util,\n" +
		// " org.apache.batik.util.gui,\n" +
		// " org.apache.commons.jxpath,\n" +
		// " org.apache.commons.logging,\n" +
		// " org.apache.felix.gogo.command,\n" +
		// " org.apache.felix.gogo.runtime,\n" +
		// " org.apache.felix.scr,\n" +
		// " org.eclipse.core.commands,\n" +
		// " org.eclipse.core.contenttype,\n" +
		// " org.eclipse.core.databinding,\n" +
		// " org.eclipse.core.databinding.beans,\n" +
		// " org.eclipse.core.databinding.observable,\n" +
		// " org.eclipse.core.databinding.property,\n" +
		// " org.eclipse.core.expressions,\n" +
		// " org.eclipse.core.filesystem,\n" +
		// " org.eclipse.core.filesystem.macosx,\n" +
		// " org.eclipse.core.jobs,\n" +
		// " org.eclipse.core.resources,\n" +
		// " org.eclipse.core.runtime,\n" +
		// " org.eclipse.e4.core.commands,\n" +
		// " org.eclipse.e4.core.contexts,\n" +
		// " org.eclipse.e4.core.di,\n" +
		// " org.eclipse.e4.core.di.annotations,\n" +
		// " org.eclipse.e4.core.di.extensions,\n" +
		// " org.eclipse.e4.core.di.extensions.supplier,\n" +
		// " org.eclipse.e4.core.services,\n" +
		// " org.eclipse.e4.emf.xpath,\n" +
		// " org.eclipse.e4.ui.bindings,\n" +
		// " org.eclipse.e4.ui.css.core,\n" +
		// " org.eclipse.e4.ui.css.swt,\n" +
		// " org.eclipse.e4.ui.css.swt.theme,\n" +
		// " org.eclipse.e4.ui.di,\n" +
		// " org.eclipse.e4.ui.model.workbench,\n" +
		// " org.eclipse.e4.ui.services,\n" +
		// " org.eclipse.e4.ui.widgets,\n" +
		// " org.eclipse.e4.ui.workbench,\n" +
		// " org.eclipse.e4.ui.workbench.renderers.swt,\n" +
		// " org.eclipse.e4.ui.workbench.renderers.swt.cocoa,\n" +
		// " org.eclipse.e4.ui.workbench.swt,\n" +
		// " org.eclipse.e4.ui.workbench3,\n" +
		// " org.eclipse.emf.common,\n" +
		// " org.eclipse.emf.databinding,\n" +
		// " org.eclipse.emf.ecore,\n" +
		// " org.eclipse.emf.ecore.change,\n" +
		// " org.eclipse.emf.ecore.xmi,\n" +
		// " org.eclipse.equinox.app,\n" +
		// " org.eclipse.equinox.common,\n" +
		// " org.eclipse.equinox.concurrent,\n" +
		// " org.eclipse.equinox.ds@start,\n" +
		// " org.eclipse.equinox.event,\n" +
		// " org.eclipse.equinox.preferences,\n" +
		// " org.eclipse.equinox.registry,\n" +
		// " org.eclipse.equinox.util,\n" +
		// " org.eclipse.jface,\n" +
		// " org.eclipse.jface.databinding,\n" +
		// " org.eclipse.osgi,\n" +
		// " org.eclipse.osgi.compatibility.state,\n" +
		// " org.eclipse.osgi.services,\n" +
		// " org.eclipse.osgi.util,\n" +
		// " org.eclipse.swt,\n" +
		// " org.eclipse.swt.cocoa.macosx.x86_64,\n" +
		// " org.objectweb.asm,\n" +
		// " org.objectweb.asm.analysis,\n" +
		// " org.objectweb.asm.commons,\n" +
		// " org.objectweb.asm.tree,\n" +
		// " org.objectweb.asm.util,\n" +
		// " org.w3c.css.sac,\n" +
		// " org.w3c.dom.events,\n" +
		// " org.w3c.dom.smil,\n" +
		// " org.w3c.dom.svg,");
		// initProps.put("osgi.bundles.defaultStartLevel", "4");
		// // according to config.ini
		//
		//
		// initProps.put("osgi.user.area", "this.userArea");
		// initProps.put("osgi.noShutdown", "true");
		//
		//
		// EclipseStarter.setInitialProperties(initProps);
		//
		// String[] equinoxArgs = {"-console","1234","-noExit"};
		//
		// EclipseStarter.startup(equinoxArgs, null);
		// System.out.println("sisisi pasa por aca antes del run");
		//// EclipseStarter.run(equinoxArgs, null);
		System.out.println("sisisi pasa por aca despues del run");

		// ServiceLoader<FrameworkFactory> ffs =
		// ServiceLoader.load(FrameworkFactory.class);
		// FrameworkFactory ff = ffs.iterator().next();
		// Map<String, String> config = new HashMap<String,String>();
		// // add some params to config ...
		// Framework fwk = ff.newFramework(config);
		// fwk.start();
		//
		// BundleContext bc = fwk.getBundleContext();
		// bc.insta
		//
		// ServiceReference<?> serviceReference;
		// try {
		// serviceReference = bc.getAllServiceReferences(
		// "org.osgi.service.application.ApplicationDescriptor",
		// "(service.pid=com.make.equo.application)")[0];
		// ApplicationDescriptor app = (ApplicationDescriptor)
		// bc.getService(serviceReference);
		// app.launch(null);
		// } catch (InvalidSyntaxException e) {
		// // TODO Auto-generated catch block
		// System.out.println("error aca en invalid syntax ex");
		// e.printStackTrace();
		// } catch (ApplicationException e) {
		// System.out.println("error aca en ApplicationException");
		// e.printStackTrace();
		// }
		return null;
	}

	private static Object runn(BundleContext context) {
		try {
			return EclipseStarter.run(context);
		} catch (Throwable t) {
			System.out.println("vamososs");
		}
		System.out.println("booo ooo");
		return null;
	}
	
	public static void main(String args[]) throws Exception {
		start();
	}

}
