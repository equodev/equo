package com.make.equo.server.api.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

import org.eclipse.core.runtime.adaptor.EclipseStarter;
import org.eclipse.core.runtime.internal.adaptor.EclipseAppLauncher;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;
import org.osgi.service.application.ApplicationDescriptor;
import org.osgi.util.tracker.ServiceTracker;

import com.make.equo.application.api.ApplicationLauncher;
import com.make.equo.server.api.ApplicationLauncherServiceTracker;
import com.make.equo.server.api.IEquoApplication;

public class EquoApplication implements IEquoApplication {

	private static final String SAMPLE_APP_NAME = "Hello Equo!";

	private String applicationName;
	
//	MApplication application;
//	@Inject
//	EModelService modelService;
//
//	private MPart mainPagePart;
//
//	private EPartService windowPartService;
//
//	@Inject
//	private IEclipseContext iEclipseContext;
	
	public EquoApplication() {
		
	}
	
	public EquoApplication(String applicationName) {
		this.applicationName = applicationName;
	}

	@Override
	public IEquoApplication name(String applicationName) {
		this.applicationName = applicationName;
		return this;
	}

	@Override
	public IEquoApplication withSingleView(String url) {
		// TODO Auto-generated method stub
		// MApplication application =
		// modelService.createModelElement(MApplication.class);
		
//		CreateAppHandler man = new CreateAppHandler();
		
		//inject the context into an object
		//IEclipseContext iEclipseContext was injected into this class
//		ContextInjectionFactory.inject(man,iEclipseContext);
//		
//		man.execute();
//		MBasicFactory.INSTANCE.create
//		modelService = application.getContext().get(EModelService.class);
//		MTrimmedWindow trimmedWindow = modelService.createModelElement(MTrimmedWindow.class);
//
//		application.getChildren().add(trimmedWindow);

		// EModelService windowModelService =
		// trimmedWindow.getContext().get(EModelService.class);
//		windowPartService = trimmedWindow.getContext().get(EPartService.class);
//		mainPagePart = windowPartService.findPart("com.make.equo.trimmedwindow.mainpage");
//		if (mainPagePart == null) {
//			// create if not exists
//			mainPagePart = windowPartService.createPart("com.make.equo.trimmedwindow.mainpage");
//		}
//
//		return this;

		// MPart mainPagePart = MBasicFactory.INSTANCE.createPart();
		// mainPagePart.setLabel("New Part");
		// part.setContributionURI("platform:/plugin/de.vogella.e4.modelservice/de.vogella.e4.modelservice.part.NewPart");

		// trimmedWindow.getChildren().add(mainPagePart);

		// List<MPartStack> stacks = modelService.findElements(application, null,
		// MPartStack.class, null);
		// stacks.get(0).getChildren().add(part);
		// partService.showPart(part, PartState.ACTIVATE);

		// application.
		return null;
	}

	@Override
	public void show() {
//		mainPagePart.setVisible(true);
//		windowPartService.showPart(mainPagePart, PartState.VISIBLE);
//		EquoApplicationStarter.start();
	}
	
	@SuppressWarnings("restriction")
	public static void main(String args[]) throws Exception {
//		 String[] equinoxArgs = {"-console","1234","-noExit"};
//		 BundleContext context = EclipseStarter.startup(equinoxArgs,null);
		 
		 Map<String, String> initProps = new HashMap<String, String> ();
		 
////		initProps.put("osgi.splashPath", "platform:/base/plugins/com.make.equo"); 
//		// corresponding to generated config.ini 
//		
//		initProps.put("eclipse.product", "equo.product"); 
//		
		initProps.put("osgi.clean", "true"); 
		initProps.put("osgi.dev", "true"); 
//		initProps.put("osgi.debug", "true"); 
////		
		initProps.put("osgi.install.area", "this.installLocation"); 
		initProps.put("osgi.instance.area", "this.workspace"); 
		initProps.put("osgi.configuration.area", "this.config");	
		
//		
		initProps.put("osgi.bundles", 
				"org.eclipse.swt.examples.addressbook,\n"+
		"com.github.jnr.ffi,\n" + 
		"      com.github.jnr.jffi,\n" + 
		"      com.github.jnr.jffi.native,\n" + 
		"      com.github.jnr.x86asm,\n" + 
		"      com.ibm.icu,\n" + 
		"      com.make.cef,\n" + 
		"      com.make.cef.osx.x86_64,\n" + 
//		"      com.make.equo,\n" + 
		"      javax.annotation,\n" + 
		"      javax.inject,\n" + 
		"      javax.xml,\n" + 
		"      org.apache.batik.css,\n" + 
		"      org.apache.batik.util,\n" + 
		"      org.apache.batik.util.gui,\n" + 
		"      org.apache.commons.jxpath,\n" + 
		"      org.apache.commons.logging,\n" + 
		"      org.apache.felix.gogo.command,\n" + 
		"      org.apache.felix.gogo.runtime,\n" + 
		"      org.apache.felix.scr,\n" + 
		"      org.eclipse.core.commands,\n" + 
		"      org.eclipse.core.contenttype,\n" + 
		"      org.eclipse.core.databinding,\n" + 
		"      org.eclipse.core.databinding.beans,\n" + 
		"      org.eclipse.core.databinding.observable,\n" + 
		"      org.eclipse.core.databinding.property,\n" + 
		"      org.eclipse.core.expressions,\n" + 
		"      org.eclipse.core.filesystem,\n" + 
		"      org.eclipse.core.filesystem.macosx,\n" + 
		"      org.eclipse.core.jobs,\n" + 
		"      org.eclipse.core.resources,\n" + 
		"      org.eclipse.core.runtime@start,\n" + 
		"      org.eclipse.e4.core.commands,\n" + 
		"      org.eclipse.e4.core.contexts,\n" + 
		"      org.eclipse.e4.core.di,\n" + 
		"      org.eclipse.e4.core.di.annotations,\n" + 
		"      org.eclipse.e4.core.di.extensions,\n" + 
		"      org.eclipse.e4.core.di.extensions.supplier,\n" + 
		"      org.eclipse.e4.core.services,\n" + 
		"      org.eclipse.e4.emf.xpath,\n" + 
		"      org.eclipse.e4.ui.bindings,\n" + 
		"      org.eclipse.e4.ui.css.core,\n" + 
		"      org.eclipse.e4.ui.css.swt,\n" + 
		"      org.eclipse.e4.ui.css.swt.theme,\n" + 
		"      org.eclipse.e4.ui.di,\n" + 
		"      org.eclipse.e4.ui.model.workbench,\n" + 
		"      org.eclipse.e4.ui.services,\n" + 
		"      org.eclipse.e4.ui.widgets,\n" + 
		"      org.eclipse.e4.ui.workbench,\n" + 
		"      org.eclipse.e4.ui.workbench.renderers.swt,\n" + 
		"      org.eclipse.e4.ui.workbench.renderers.swt.cocoa,\n" + 
		"      org.eclipse.e4.ui.workbench.swt,\n" + 
		"      org.eclipse.e4.ui.workbench3,\n" + 
		"      org.eclipse.emf.common,\n" + 
		"      org.eclipse.emf.databinding,\n" + 
		"      org.eclipse.emf.ecore,\n" + 
		"      org.eclipse.emf.ecore.change,\n" + 
		"      org.eclipse.emf.ecore.xmi,\n" + 
		"      org.eclipse.equinox.app,\n" + 
		"      org.eclipse.equinox.common,\n" + 
		"      org.eclipse.equinox.concurrent,\n" + 
		"      org.eclipse.equinox.ds@start,\n" +
//		"      org.eclipse.equinox.p2.reconciler.dropins@start,\n" + 
		"      org.eclipse.equinox.event@start,\n" + 
		"      org.eclipse.equinox.preferences,\n" + 
		"      org.eclipse.equinox.registry,\n" + 
		"      org.eclipse.equinox.util,\n" + 
		"      org.eclipse.jface,\n" + 
		"      org.eclipse.jface.databinding,\n" + 
		"      org.eclipse.osgi,\n" + 
		"      org.eclipse.osgi.compatibility.state,\n" + 
		"      org.eclipse.osgi.services,\n" + 
		"      org.eclipse.osgi.util,\n" + 
		"      org.eclipse.swt,\n" + 
		"      org.eclipse.swt.cocoa.macosx.x86_64,\n" + 
		"      org.objectweb.asm,\n" + 
		"      org.objectweb.asm.analysis,\n" + 
		"      org.objectweb.asm.commons,\n" + 
		"      org.objectweb.asm.tree,\n" + 
		"      org.objectweb.asm.util,\n" + 
		"      org.w3c.css.sac,\n" + 
		"      org.w3c.dom.events,\n" + 
		"      org.w3c.dom.smil,\n" + 
		"      org.w3c.dom.svg,");
		initProps.put("osgi.bundles.defaultStartLevel", "1"); 
		// according to config.ini 
		initProps.put("osgi.instance.area","/Users/seba/eclipse-workspace/../runtime-equo.product");
		initProps.put("osgi.dev","file:/Users/seba/eclipse-workspace/.metadata/.plugins/org.eclipse.pde.core/equo.product/dev.properties");
		initProps.put("osgi.os","macosx");
		initProps.put("osgi.ws", "cocoa");
		initProps.put("osgi.arch","x86_64");
		initProps.put("eclipse.consoleLog","true");
		initProps.put("osgi.os","macosx");
		initProps.put("eclipse.consoleLog","true");
		initProps.put("eclipse.application","com.make.equo.application");
//		initProps.put("osgi.parentClassloader","app");
		
		
//		initProps.put("osgi.user.area", "this.userArea"); 
//		initProps.put("osgi.noShutdown", "false"); 
		
//		initProps.put("eclipse.ignoreApp", "true"); 
		initProps.put("eclipse.application.launchDefault", "true");
		initProps.put("eclipse.allowAppRelaunch", "true");
		
//		osgiWayRun(initProps);
		EclipseStarter.setInitialProperties(initProps); 
		
		
//		 "-consoleLog", "-clearPersistedState", "-application", "com.make.equo.application"};
//		String[] equinoxArgs = {"-product", "com.make.equo.product", "-data", "/Users/seba/eclipse-workspace/../runtime-equo.product", "-dev", "file:/Users/seba/eclipse-workspace/.metadata/.plugins/org.eclipse.pde.core/equo.product/dev.properties", "-os", "macosx", "-ws", "cocoa", "-arch", "x86_64", "-consoleLog", "-clearPersistedState", "-application", "org.eclipse.e4.ui.workbench.swt.E4Application"};
		String[] equinoxArgs = {"-data", "/Users/seba/eclipse-workspace/../runtime-equo.product", "-dev", "file:/Users/seba/eclipse-workspace/.metadata/.plugins/org.eclipse.pde.core/equo.product/dev.properties", "-os", "macosx", "-ws", "cocoa", "-arch", "x86_64", "-consoleLog", "-clearPersistedState", "-application", "com.make.equo.application"};
//		EclipseStarter.run(equinoxArgs, null); 
//		
		BundleContext context = EclipseStarter.startup(equinoxArgs, null);
//		
//		Bundle installBundle = context.installBundle("file:/Users/seba/Desktop/plugins/product/repository/plugins/com.make.equo_1.0.0.201709120819.jar");
		Bundle apiBundle = context.installBundle("file:/Users/seba/Desktop/plugins/com.make.equo.application.api_1.0.0.jar");
		Bundle providerBundle = context.installBundle("file:/Users/seba/Desktop/plugins/com.make.equo.application.provider_1.0.0.201709152344.jar");
//		Bundle providerBundle = context.installBundle("file:/Users/seba/Desktop/plugins/main_thread/com.make.equo.application.provider_1.0.0.201709152100.jar");
		
		
//		apiBundle.start();
		providerBundle.start();
		
		
		
//		ServiceReference reference = context
//                .getServiceReference(com.make.equo.application.api.ApplicationLauncher.class.getName());
////        Class<? extends Object> class1 = context.getService(reference).getClass();
//		System.out.println(context.getService(reference).getClass().getClassLoader());
////		apiBundle.loadClass(com.make.equo.application.api.ApplicationLauncher.class.getName());
//        System.out.println(com.make.equo.application.api.ApplicationLauncher.class.getClassLoader());
//        System.out.println("running the app");
//        
//        ServiceTracker<ApplicationLauncher, Object> serviceTracker= new ApplicationLauncherServiceTracker(context);
//        serviceTracker.open();
//        ApplicationLauncher service2 = (ApplicationLauncher) serviceTracker.getService();
//        Map<String, Object> appArguments = new HashMap<>();
////        appArguments.put("eclipse.application.default", true);
//        service2.launch(null);
        
//        com.make.equo.application.api.ApplicationLauncher service = (com.make.equo.application.api.ApplicationLauncher) context.getService(reference);
//        service.launch(null);
//        
        
//		System.out.println("class loader " + serviceReference.getClass().getClassLoader());
//		System.out.println(bundle.getBundleContext().getService(serviceReference).getClass().getClassLoader());
//		System.out.println(Thread.currentThread().getContextClassLoader());
//		Thread.currentThread().setContextClassLoader(bundle.getBundleContext().getService(serviceReference).getClass().getClassLoader());
//		System.out.println(Thread.currentThread().getContextClassLoader());
//		System.out.println(Thread.currentThread().getContextClassLoader());
		
//		installBundle.getBundleContext().getBundle();
//		
		runn(context);
		
//		context.getBundle().start();
//		System.out.println("sisisi pasa por aca antes del run");
//		 
//		System.out.println("bundles: " + context.getBundles()[1]);
//		_startApp(context);
//		context.get
//		 Bundle bundle = context.installBundle(
//		 "file:/Users/seba/Desktop/plugins/com.make.equo_1.0.0.201709120819.jar");
//		 bundle.start();
//		for (ServiceReference<?> s : context.getBundle().getRegisteredServices()) {
//			System.out.println("el registers services es " + s.toString());
//		}
//		 System.out.println("los registers services son " + context.getBundle().getRegisteredServices()[0] + ", context.getBundle().getRegisteredServices()[1]");
//		 bundle.getBundleContext().registerService("org.osgi.service.application.ApplicationDescriptor", new E4Application(), null);
//		 
//		 try {
//				ServiceReference<?> serviceReference = bundle.getBundleContext().getAllServiceReferences(
//						"org.osgi.service.application.ApplicationDescriptor", "(service.pid=com.make.equo.application)")[0];
//				ApplicationDescriptor app = (ApplicationDescriptor) context.getService(serviceReference);
//				System.out.println("ejecutando app");
//				app.launch(null);
//			} catch (InvalidSyntaxException e) {
//				// TODO Auto-generated catch block
//				System.out.println("error aca en invalid syntax ex");
//				e.printStackTrace();
//			} catch (ApplicationException e) {
//				System.out.println("error aca en ApplicationException");
//				e.printStackTrace();
//			}

//		Map initProps = new HashMap (); 
//		
////		initProps.put("osgi.splashPath", "platform:/base/plugins/com.make.equo"); 
//		// corresponding to generated config.ini 
//		
//		initProps.put("eclipse.product", "com.make.equo.product"); 
//		
//		initProps.put("osgi.clean", "true"); 
//		initProps.put("osgi.dev", "true"); 
////		initProps.put("osgi.debug", "true"); 
//		
////		initProps.put("osgi.install.area", this.installLocation); 
////		initProps.put("osgi.instance.area", this.workspace); 
////		initProps.put("osgi.configuration.area", this.config);	
//		
//		
//		initProps.put("osgi.bundles", 
//		"com.github.jnr.ffi,\n" + 
//		"      com.github.jnr.jffi,\n" + 
//		"      com.github.jnr.jffi.native,\n" + 
//		"      com.github.jnr.x86asm,\n" + 
//		"      com.ibm.icu,\n" + 
//		"      com.make.cef,\n" + 
//		"      com.make.cef.osx.x86_64,\n" + 
//		"      com.make.equo,\n" + 
//		"      javax.annotation,\n" + 
//		"      javax.inject,\n" + 
//		"      javax.xml,\n" + 
//		"      org.apache.batik.css,\n" + 
//		"      org.apache.batik.util,\n" + 
//		"      org.apache.batik.util.gui,\n" + 
//		"      org.apache.commons.jxpath,\n" + 
//		"      org.apache.commons.logging,\n" + 
//		"      org.apache.felix.gogo.command,\n" + 
//		"      org.apache.felix.gogo.runtime,\n" + 
//		"      org.apache.felix.scr,\n" + 
//		"      org.eclipse.core.commands,\n" + 
//		"      org.eclipse.core.contenttype,\n" + 
//		"      org.eclipse.core.databinding,\n" + 
//		"      org.eclipse.core.databinding.beans,\n" + 
//		"      org.eclipse.core.databinding.observable,\n" + 
//		"      org.eclipse.core.databinding.property,\n" + 
//		"      org.eclipse.core.expressions,\n" + 
//		"      org.eclipse.core.filesystem,\n" + 
//		"      org.eclipse.core.filesystem.macosx,\n" + 
//		"      org.eclipse.core.jobs,\n" + 
//		"      org.eclipse.core.resources,\n" + 
//		"      org.eclipse.core.runtime,\n" + 
//		"      org.eclipse.e4.core.commands,\n" + 
//		"      org.eclipse.e4.core.contexts,\n" + 
//		"      org.eclipse.e4.core.di,\n" + 
//		"      org.eclipse.e4.core.di.annotations,\n" + 
//		"      org.eclipse.e4.core.di.extensions,\n" + 
//		"      org.eclipse.e4.core.di.extensions.supplier,\n" + 
//		"      org.eclipse.e4.core.services,\n" + 
//		"      org.eclipse.e4.emf.xpath,\n" + 
//		"      org.eclipse.e4.ui.bindings,\n" + 
//		"      org.eclipse.e4.ui.css.core,\n" + 
//		"      org.eclipse.e4.ui.css.swt,\n" + 
//		"      org.eclipse.e4.ui.css.swt.theme,\n" + 
//		"      org.eclipse.e4.ui.di,\n" + 
//		"      org.eclipse.e4.ui.model.workbench,\n" + 
//		"      org.eclipse.e4.ui.services,\n" + 
//		"      org.eclipse.e4.ui.widgets,\n" + 
//		"      org.eclipse.e4.ui.workbench,\n" + 
//		"      org.eclipse.e4.ui.workbench.renderers.swt,\n" + 
//		"      org.eclipse.e4.ui.workbench.renderers.swt.cocoa,\n" + 
//		"      org.eclipse.e4.ui.workbench.swt,\n" + 
//		"      org.eclipse.e4.ui.workbench3,\n" + 
//		"      org.eclipse.emf.common,\n" + 
//		"      org.eclipse.emf.databinding,\n" + 
//		"      org.eclipse.emf.ecore,\n" + 
//		"      org.eclipse.emf.ecore.change,\n" + 
//		"      org.eclipse.emf.ecore.xmi,\n" + 
//		"      org.eclipse.equinox.app,\n" + 
//		"      org.eclipse.equinox.common,\n" + 
//		"      org.eclipse.equinox.concurrent,\n" + 
//		"      org.eclipse.equinox.ds@start,\n" + 
//		"      org.eclipse.equinox.event,\n" + 
//		"      org.eclipse.equinox.preferences,\n" + 
//		"      org.eclipse.equinox.registry,\n" + 
//		"      org.eclipse.equinox.util,\n" + 
//		"      org.eclipse.jface,\n" + 
//		"      org.eclipse.jface.databinding,\n" + 
//		"      org.eclipse.osgi,\n" + 
//		"      org.eclipse.osgi.compatibility.state,\n" + 
//		"      org.eclipse.osgi.services,\n" + 
//		"      org.eclipse.osgi.util,\n" + 
//		"      org.eclipse.swt,\n" + 
//		"      org.eclipse.swt.cocoa.macosx.x86_64,\n" + 
//		"      org.objectweb.asm,\n" + 
//		"      org.objectweb.asm.analysis,\n" + 
//		"      org.objectweb.asm.commons,\n" + 
//		"      org.objectweb.asm.tree,\n" + 
//		"      org.objectweb.asm.util,\n" + 
//		"      org.w3c.css.sac,\n" + 
//		"      org.w3c.dom.events,\n" + 
//		"      org.w3c.dom.smil,\n" + 
//		"      org.w3c.dom.svg,");
//		initProps.put("osgi.bundles.defaultStartLevel", "4"); 
//		// according to config.ini 
//		
//		
//		initProps.put("osgi.user.area", "this.userArea"); 
//		initProps.put("osgi.noShutdown", "true"); 
//		
//		
//		EclipseStarter.setInitialProperties(initProps); 
//		
//		String[] equinoxArgs = {"-console","1234","-noExit"}; 
//		
//		EclipseStarter.startup(equinoxArgs, null); 
//		System.out.println("sisisi pasa por aca antes del run");
////		EclipseStarter.run(equinoxArgs, null); 
		System.out.println("sisisi pasa por aca despues del run");
		
//		ServiceLoader<FrameworkFactory> ffs = ServiceLoader.load(FrameworkFactory.class);
//		FrameworkFactory ff = ffs.iterator().next();
//		Map<String, String> config = new HashMap<String,String>();
//		// add some params to config ...
//		Framework fwk = ff.newFramework(config);
//		fwk.start();
//		
//		BundleContext bc = fwk.getBundleContext();
//		bc.insta
//		
//		ServiceReference<?> serviceReference;
//		try {
//			serviceReference = bc.getAllServiceReferences(
//					"org.osgi.service.application.ApplicationDescriptor", "(service.pid=com.make.equo.application)")[0];
//			ApplicationDescriptor app = (ApplicationDescriptor) bc.getService(serviceReference);
//			app.launch(null);
//		} catch (InvalidSyntaxException e) {
//			// TODO Auto-generated catch block
//			System.out.println("error aca en invalid syntax ex");
//			e.printStackTrace();
//		} catch (ApplicationException e) {
//			System.out.println("error aca en ApplicationException");
//			e.printStackTrace();
//		}
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
	
	public static void osgiWayRun(Map<String, String> config) {
		ServiceLoader<FrameworkFactory> ffs = ServiceLoader.load(FrameworkFactory.class);
		FrameworkFactory ff = ffs.iterator().next();
//		Map<String,String> config = new HashMap<String,String>();
		// add some params to config ...
		Framework fwk = ff.newFramework(config);
		try {
			fwk.start();
			
			BundleContext bc = fwk.getBundleContext();
//			bc.installBundle("file:/path/to/bundle.jar");
			EclipseAppLauncher appLauncher = new EclipseAppLauncher(bc, false, false, null, null);
//			bc.registerService(ApplicationLauncher.class.getName(), appLauncher, null);
			
			
//			boolean launchDefault = Boolean.valueOf(getProperty(PROP_APPLICATION_LAUNCHDEFAULT, "true")).booleanValue(); //$NON-NLS-1$
			// create the ApplicationLauncher and register it as a service
//			appLauncher = new EclipseAppLauncher(context, Boolean.valueOf(getProperty(PROP_ALLOW_APPRELAUNCH)).booleanValue(), launchDefault, log, equinoxConfig);
//			appLauncherRegistration = context.registerService(ApplicationLauncher.class.getName(), appLauncher, null);
			// must start the launcher AFTER service restration because this method 
			// blocks and runs the application on the current thread.  This method 
			// will return only after the application has stopped.
			appLauncher.start(null);
			
		} catch (BundleException e) {
			System.out.println("bundle exxxcept sisi");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	public static Object anotherRun(Object argument) throws Exception {
//		if (!running)
//			throw new IllegalStateException(Msg.ECLIPSE_STARTUP_NOT_RUNNING);
//		// if we are just initializing, do not run the application just return.
//		if (initialize)
//			return Integer.valueOf(0);
//		try {
//			if (appLauncher == null) {
//				boolean launchDefault = Boolean.valueOf(getProperty(PROP_APPLICATION_LAUNCHDEFAULT, "true")).booleanValue(); //$NON-NLS-1$
//				// create the ApplicationLauncher and register it as a service
//				appLauncher = new EclipseAppLauncher(context, Boolean.valueOf(getProperty(PROP_ALLOW_APPRELAUNCH)).booleanValue(), launchDefault, log, equinoxConfig);
//				appLauncherRegistration = context.registerService(ApplicationLauncher.class.getName(), appLauncher, null);
//				// must start the launcher AFTER service restration because this method 
//				// blocks and runs the application on the current thread.  This method 
//				// will return only after the application has stopped.
//				return appLauncher.start(argument);
//			}
//			return appLauncher.reStart(argument);
//		} catch (Exception e) {
//			if (log != null && context != null) { // context can be null if OSGi failed to launch (bug 151413)
//				ResolutionReport report = context.getBundle().adapt(Module.class).getContainer().resolve(null, false);
//				for (Resource unresolved : report.getEntries().keySet()) {
//					String bsn = ((ModuleRevision) unresolved).getSymbolicName();
//					FrameworkLogEntry logEntry = new FrameworkLogEntry(bsn != null ? bsn : EquinoxContainer.NAME, FrameworkLogEntry.WARNING, 0, Msg.Module_ResolveError + report.getResolutionReportMessage(unresolved), 1, null, null);
//					log.log(logEntry);
//				}
//			}
//			throw e;
//		}
//	}
	
	public static void _startApp(BundleContext context) throws Exception {
//		Bundle bundle = context.getBundle(8);
//		System.out.println(bundle);
		
//		String appId = intp.nextArgument();
		ServiceTracker applicationDescriptors = new ServiceTracker(context, ApplicationDescriptor.class.getName(), null);
		ServiceReference<?> serviceReference = context.getAllServiceReferences("org.osgi.service.application.ApplicationDescriptor", "(service.pid=com.make.equo.application)")[0];
//		System.out.println("class loader " + serviceReference.getClass().getClassLoader());
//		System.out.println(bundle.getBundleContext().getService(serviceReference).getClass().getClassLoader());
//		System.out.println(Thread.currentThread().getContextClassLoader());
//		Thread.currentThread().setContextClassLoader(bundle.getBundleContext().getService(serviceReference).getClass().getClassLoader());
//		System.out.println(Thread.currentThread().getContextClassLoader());
		ApplicationDescriptor application = (ApplicationDescriptor) context.getService(serviceReference);
		application.launch(null);
//		System.out.println(Thread.currentThread().getContextClassLoader());
//		ServiceReference application = getApplication(applicationDescriptors.getServiceReferences(), appId, ApplicationDescriptor.APPLICATION_PID, false);
//		if (application == null)
////			intp.println("\"" + appId + "\" does not exist or is ambigous."); //$NON-NLS-1$ //$NON-NLS-2$
//			System.out.println("app es null sisi");
//		else {
//			ArrayList argList = new ArrayList();
//			String arg = null;
////			while ((arg = intp.nextArgument()) != null)
////				argList.add(arg);
//			String[] args = argList.size() == 0 ? null : (String[]) argList.toArray(new String[argList.size()]);
//			try {
//				HashMap launchArgs = new HashMap(1);
//				if (args != null)
//					launchArgs.put(IApplicationContext.APPLICATION_ARGS, args);
//				ApplicationDescriptor appDesc = ((EclipseAppDescriptor) context.getService(serviceReference));
//				ApplicationHandle handle = appDesc.launch(launchArgs);
////				intp.println("Launched application instance: " + handle.getInstanceId()); //$NON-NLS-1$
//			} finally {
//				context.ungetService(serviceReference);
//			}
//			return;
//		}
		
//		String appId = intp.nextArgument();
//		ServiceReference application = getApplication(applicationDescriptors.getServiceReferences(), "com.make.equo.application", ApplicationDescriptor.APPLICATION_PID, false);
//		if (application == null)
//			System.out.println("app es null sisi");
//		else {
//			ArrayList argList = new ArrayList();
//			String arg = null;
////			while ((arg = intp.nextArgument()) != null)
////				argList.add(arg);
//			String[] args = argList.size() == 0 ? null : (String[]) argList.toArray(new String[argList.size()]);
//			try {
//				HashMap launchArgs = new HashMap(1);
//				if (args != null)
//					launchArgs.put(IApplicationContext.APPLICATION_ARGS, args);
//				ApplicationDescriptor appDesc = ((ApplicationDescriptor) context.getService(application));
//				ApplicationHandle handle = appDesc.launch(launchArgs);
//				System.out.println("la app esta corriendo");
////				intp.println("Launched application instance: " + handle.getInstanceId()); //$NON-NLS-1$
//			} finally {
//				context.ungetService(application);
//			}
//			return;
//		}
	}
	
	private static ServiceReference getApplication(ServiceReference[] apps, String targetId, String idKey, boolean perfectMatch) {
		if (apps == null || targetId == null)
			return null;

		ServiceReference result = null;
		boolean ambigous = false;
		for (int i = 0; i < apps.length; i++) {
			String id = (String) apps[i].getProperty(idKey);
			if (targetId.equals(id))
				return apps[i]; // always return a perfect match
			if (perfectMatch)
				continue;
			if (id.indexOf(targetId) >= 0) {
				if (result != null)
					ambigous = true;
				result = apps[i];
			}
		}
		return ambigous ? null : result;
	}
	
}
