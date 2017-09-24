package com.make.equo.application.client.api;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.adaptor.EclipseStarter;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import com.make.equo.application.api.ApplicationModelService;
import com.make.equo.application.client.ApplicationModelServiceTracker;

public class EquoApplication {

	private static ApplicationModelService applicationModelService;
	private final String name; // required
	private final String url; // optional if js script is provided (?)

	private EquoApplication(EquoApplicationBuilder builder) throws Exception {
		this.name = builder.name;
		this.url = builder.url;
		applicationModelService.buildModelApp();
		run();
	};

	private static class EquoApplicationBuilder {

		private String name;
		private String url;

		private final UrlMandatoryFieldBuilder urlMandatoryFieldBuilder;
		private final OptionalFieldsBuilder optionalFieldsBuilder;

		public EquoApplicationBuilder(String name) {
			this.urlMandatoryFieldBuilder = new UrlMandatoryFieldBuilder(this);
			this.optionalFieldsBuilder = new OptionalFieldsBuilder(this);
		}

		public UrlMandatoryFieldBuilder name(String name) {
			this.name = name;
			applicationModelService.initializeAppModel(name);
			return this.urlMandatoryFieldBuilder;
		}
	}

	public static class UrlMandatoryFieldBuilder {

		private EquoApplicationBuilder builder;

		private UrlMandatoryFieldBuilder(EquoApplicationBuilder equoApplicationBuilder) {
			this.builder = equoApplicationBuilder;
		}

		public OptionalFieldsBuilder withSingleView(String url) {
			builder.url = url;
			setMainWindowUrl(url);
			return builder.optionalFieldsBuilder;
		}

		private void setMainWindowUrl(String url) {
			applicationModelService.setMainWindowUrl(url);
		}
	}

	public static class OptionalFieldsBuilder {

		private EquoApplicationBuilder builder;

		private OptionalFieldsBuilder(EquoApplicationBuilder nameMandatoryFieldBuilder) {
			this.builder = nameMandatoryFieldBuilder;
		}

		public EquoApplication start() throws Exception {
			return new EquoApplication(builder);
		}
	}

	private static void initializeE4App() throws Exception {
		Map<String, String> initProps = new HashMap<String, String>();

		initProps.put("osgi.clean", "true");
		initProps.put("osgi.dev", "true");
		// initProps.put("osgi.debug", "true");
		initProps.put("osgi.install.area", "this.installLocation");
		initProps.put("osgi.instance.area", "this.workspace");
		initProps.put("osgi.configuration.area", "this.config");

		initProps.put("osgi.bundles", "com.github.jnr.ffi,\n" + " com.github.jnr.jffi,\n"
				+ " com.github.jnr.jffi.native,\n" + " com.github.jnr.x86asm,\n" + " com.ibm.icu,\n"
				+ " com.make.cef,\n" + " com.make.cef.osx.x86_64,\n" + " javax.annotation,\n" + " javax.inject,\n"
				+ " javax.xml,\n" + " org.apache.batik.css,\n" + " org.apache.batik.util,\n"
				+ " org.apache.batik.util.gui,\n" + " org.apache.commons.jxpath,\n" + " org.apache.commons.logging,\n"
				+ " org.apache.felix.gogo.command,\n" + " org.apache.felix.gogo.shell,\n"
				+ " org.apache.felix.gogo.runtime,\n" + " org.apache.felix.scr,\n" + " org.eclipse.core.commands,\n"
				+ " org.eclipse.core.contenttype,\n" + " org.eclipse.core.databinding,\n"
				+ " org.eclipse.core.databinding.beans,\n" + " org.eclipse.core.databinding.observable,\n"
				+ " org.eclipse.core.databinding.property,\n" + " org.eclipse.core.expressions,\n"
				+ " org.eclipse.core.filesystem,\n" + " org.eclipse.core.filesystem.macosx,\n"
				+ " org.eclipse.core.jobs,\n" + " org.eclipse.core.resources,\n"
				+ " org.eclipse.core.runtime@1:start,\n" + " org.eclipse.e4.core.commands,\n"
				+ " org.eclipse.e4.core.contexts,\n" + " org.eclipse.e4.core.di,\n"
				+ " org.eclipse.e4.core.di.annotations,\n" + " org.eclipse.e4.core.di.extensions,\n"
				+ " org.eclipse.e4.core.di.extensions.supplier,\n" + " org.eclipse.e4.core.services,\n"
				+ " org.eclipse.e4.emf.xpath,\n" + " org.eclipse.e4.ui.bindings,\n" + " org.eclipse.e4.ui.css.core,\n"
				+ " org.eclipse.e4.ui.css.swt,\n" + " org.eclipse.e4.ui.css.swt.theme,\n" + " org.eclipse.e4.ui.di,\n"
				+ " org.eclipse.e4.ui.model.workbench,\n" + " org.eclipse.e4.ui.services,\n"
				+ " org.eclipse.e4.ui.widgets,\n" + " org.eclipse.e4.ui.workbench,\n"
				+ " org.eclipse.e4.ui.workbench.renderers.swt,\n"
				+ " org.eclipse.e4.ui.workbench.renderers.swt.cocoa,\n" + " org.eclipse.e4.ui.workbench.swt,\n"
				+ " org.eclipse.e4.ui.workbench3,\n" + " org.eclipse.emf.common,\n" + " org.eclipse.emf.databinding,\n"
				+ " org.eclipse.emf.ecore,\n" + " org.eclipse.emf.ecore.change,\n" + " org.eclipse.emf.ecore.xmi,\n"
				+ " org.eclipse.equinox.app,\n" + " org.eclipse.equinox.common@2:start,\n"
				+ " org.eclipse.equinox.concurrent,\n" + " org.eclipse.equinox.ds@2:start,\n"
				+ " org.eclipse.equinox.event,\n" + " org.eclipse.equinox.preferences,\n"
				+ " org.eclipse.equinox.registry,\n" + " org.eclipse.equinox.util,\n" + " org.eclipse.jface,\n"
				+ " org.eclipse.jface.databinding,\n" + " org.eclipse.osgi,\n"
				+ " org.eclipse.osgi.compatibility.state,\n" + " org.eclipse.osgi.services,\n"
				+ " org.eclipse.osgi.util,\n" + " org.eclipse.swt,\n" + " org.eclipse.swt.cocoa.macosx.x86_64,\n"
				+ " org.objectweb.asm,\n" + " org.objectweb.asm.analysis,\n" + " org.objectweb.asm.commons,\n"
				+ " org.objectweb.asm.tree,\n" + " org.objectweb.asm.util,\n" + " org.w3c.css.sac,\n"
				+ " org.w3c.dom.events,\n" + " org.w3c.dom.smil,\n" + " org.w3c.dom.svg,\n"
				+ " org.eclipse.update.configurator@3:start,\n" + " org.eclipse.ui.workbench,\n" + " org.eclipse.ui,\n"
				+ " org.eclipse.help,\n" + " org.eclipse.e4.ui.workbench.addons.swt,\n"
				+ " org.eclipse.equinox.console,\n" + " org.eclipse.ui.cocoa");
		initProps.put("osgi.bundles.defaultStartLevel", "4");
		initProps.put("osgi.instance.area", "/Users/seba/eclipse-workspace/../runtime-equo.product5");
		initProps.put("osgi.dev",
				"file:/Users/seba/eclipse-workspace/.metadata/.plugins/org.eclipse.pde.core/equo.product/dev.properties");
		initProps.put("osgi.os", "macosx");
		initProps.put("osgi.ws", "cocoa");
		initProps.put("osgi.arch", "x86_64");
		initProps.put("eclipse.consoleLog", "true");
		initProps.put("osgi.os", "macosx");
		initProps.put("eclipse.consoleLog", "true");
		initProps.put("eclipse.application", "com.make.equo.application");
		// initProps.put("osgi.parentClassloader","app");

		initProps.put("osgi.noShutdown", "false");
		// initProps.put("eclipse.ignoreApp", "true");

		initProps.put("eclipse.application.launchDefault", "true");
		initProps.put("eclipse.allowAppRelaunch", "true");
		EclipseStarter.setInitialProperties(initProps);

		String[] equinoxArgs = { "-data", "/Users/seba/eclipse-workspace/../runtime-equo.product6", "-dev",
				"file:/Users/seba/eclipse-workspace/.metadata/.plugins/org.eclipse.pde.core/equo.product/dev.properties",
				"-os", "macosx", "-ws", "cocoa", "-arch", "x86_64", "-console", "-noexit", "-consoleLog",
				"-clearPersistedState", "-application", "com.make.equo.application" };

		BundleContext context = EclipseStarter.startup(equinoxArgs, null);
		Bundle apiBundle = context
				.installBundle("file:/Users/seba/Desktop/plugins/com.make.equo.application.api_1.0.0.jar");
		Bundle providerBundle = context
				.installBundle("file:/Users/seba/Desktop/plugins/com.make.equo.application.provider_1.0.0.jar");
		apiBundle.start();
		providerBundle.start();

		loadAppModelService(context);
	}

	private static void loadAppModelService(BundleContext eclipseContext) {
		ServiceTracker<ApplicationModelService, Object> serviceTracker = new ApplicationModelServiceTracker(
				eclipseContext);
		serviceTracker.open();
		applicationModelService = (ApplicationModelService) serviceTracker.getService();
	}

	public static UrlMandatoryFieldBuilder name(String name) {
		EquoApplicationBuilder equoApplicationBuilder = new EquoApplicationBuilder(name);
		try {
			initializeE4App();
			return equoApplicationBuilder.name(name);
		} catch (Exception e) {
			System.out.println("Should never reach this state");
		}
		return null;
	}

	private static void run() throws Exception {
		String[] args = { "-appName", "com.make.equo.application", "-application",
				"org.eclipse.e4.ui.workbench.swt.E4Application", "-" + IWorkbench.XMI_URI_ARG,
				"com.make.equo.application.provider/Application.e4xmi", "-clearPersistedState",
				"-" + IWorkbench.LIFE_CYCLE_URI_ARG,
				"bundleclass://com.make.equo.application.provider/com.make.equo.application.LifeCycleManager",
				"-XstartOnFirstThread" };
		EclipseStarter.run(args);
	}
}
