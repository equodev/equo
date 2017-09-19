package com.make.equo.application.client.api;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.adaptor.EclipseStarter;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public class EquoApplication {

	private final String name; // required
	private final String url; // optional if js script is provided (?)

	private EquoApplication(EquoApplicationBuilder builder) throws Exception {
		this.name = builder.name;
		this.url = builder.url;
		this.start();
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
			return builder.optionalFieldsBuilder;
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

	public Object start() throws Exception {
		Map<String, String> initProps = new HashMap<String, String>();

		initProps.put("osgi.clean", "true");
		initProps.put("osgi.dev", "true");
		// initProps.put("osgi.debug", "true");
		initProps.put("osgi.install.area", "this.installLocation");
		initProps.put("osgi.instance.area", "this.workspace");
		initProps.put("osgi.configuration.area", "this.config");

		initProps.put("osgi.bundles",
				"org.eclipse.e4.ui.workbench.swt_0.14.100.v20170519-1601.jar,org.apache.felix.gogo.runtime_0.10.0.v201209301036.jar,org.eclipse.jface.databinding_1.8.100.v20170503-1507.jar,org.eclipse.jface_3.13.0.v20170503-1507.jar,org.apache.felix.gogo.command_0.10.0.v201209301215.jar,org.eclipse.core.jobs_3.9.0.v20170322-0013.jar,org.apache.batik.util_1.8.0.v20170214-1941.jar,org.eclipse.core.filesystem.macosx_1.3.0.v20140124-1940.jar,org.eclipse.e4.ui.workbench_1.5.0.v20170412-0908.jar,org.eclipse.equinox.ds_1.5.0.v20170307-1429.jar@1:start,org.eclipse.e4.core.commands_0.12.100.v20170513-0428.jar,org.eclipse.ant.core_3.5.0.v20170509-2149.jar,org.eclipse.emf.ecore.change_2.11.0.v20170609-0707.jar,org.w3c.dom.svg_1.1.0.v201011041433.jar,org.eclipse.e4.core.di.annotations_1.6.0.v20170119-2002.jar,org.eclipse.e4.ui.css.swt_0.13.0.v20170516-1617.jar,org.eclipse.e4.ui.workbench.renderers.swt_0.14.100.v20170612-1255.jar,org.eclipse.equinox.app_1.3.400.v20150715-1528.jar,org.eclipse.e4.ui.workbench.addons.swt_1.3.1.v20170319-1442.jar,org.eclipse.e4.ui.workbench3_0.14.0.v20160630-0740.jar,org.eclipse.e4.ui.bindings_0.12.0.v20170312-2302.jar,org.eclipse.e4.ui.widgets_1.2.0.v20160630-0736.jar,org.eclipse.equinox.preferences_3.7.0.v20170126-2132.jar,org.eclipse.e4.ui.model.workbench_2.0.0.v20170228-1842.jar,javax.annotation_1.2.0.v201602091430.jar,org.w3c.dom.smil_1.0.1.v200903091627.jar,org.eclipse.core.databinding.property_1.6.100.v20170515-1119.jar,org.eclipse.ui.trace_1.1.0.v20170201-2036.jar,org.eclipse.core.variables_3.4.0.v20170113-2056.jar,org.eclipse.emf.ecore.xmi_2.13.0.v20170609-0707.jar,org.eclipse.e4.core.di.extensions_0.15.0.v20170228-1728.jar,org.eclipse.e4.emf.xpath_0.2.0.v20160630-0728.jar,org.eclipse.swt.cocoa.macosx.x86_64_3.106.0.v20170608-0516.jar,org.eclipse.core.databinding.observable_1.6.100.v20170515-1119.jar,org.apache.batik.css_1.8.0.v20170214-1941.jar,org.apache.commons.jxpath_1.3.0.v200911051830.jar,org.eclipse.core.commands_3.9.0.v20170530-1048.jar,org.eclipse.osgi.util_3.4.0.v20170111-1608.jar,org.eclipse.core.runtime_3.13.0.v20170207-1030.jar@start,org.eclipse.e4.core.services_2.1.0.v20170407-0928.jar,org.eclipse.core.resources_3.12.0.v20170417-1558.jar,org.eclipse.e4.core.di.extensions.supplier_0.15.0.v20170407-0928.jar,org.eclipse.core.expressions_3.6.0.v20170207-1037.jar,org.eclipse.e4.ui.workbench.renderers.swt.cocoa_0.11.300.v20160330-1418.jar,org.eclipse.ui.cocoa_1.1.100.v20151202-1450.jar,org.eclipse.core.databinding_1.6.100.v20170515-1119.jar,org.eclipse.e4.core.di_1.6.100.v20170421-1418.jar,org.eclipse.e4.ui.css.core_0.12.100.v20170526-1635.jar,org.eclipse.emf.ecore_2.13.0.v20170609-0707.jar,org.eclipse.team.core_3.8.100.v20170516-0820.jar,javax.servlet_3.1.0.v201410161800.jar,org.eclipse.equinox.event_1.4.0.v20170105-1446.jar,org.eclipse.osgi.services_3.6.0.v20170228-1906.jar,org.w3c.css.sac_1.3.1.v200903091627.jar,org.eclipse.equinox.registry_3.7.0.v20170222-1344.jar,javax.xml_1.3.4.v201005080400.jar,org.eclipse.e4.ui.services_1.3.0.v20170307-2032.jar,org.eclipse.e4.ui.css.swt.theme_0.11.0.v20170312-2302.jar,org.eclipse.swt_3.106.0.v20170608-0516.jar,com.ibm.icu_58.2.0.v20170418-1837.jar,org.eclipse.core.filesystem_1.7.0.v20170406-1337.jar,org.eclipse.equinox.bidi_1.1.0.v20160728-1031.jar,org.eclipse.compare.core_3.6.100.v20170516-0820.jar,org.eclipse.core.contenttype_3.6.0.v20170207-1037.jar,org.eclipse.e4.core.contexts_1.6.0.v20170322-1144.jar,org.eclipse.ui_3.109.0.v20170411-1742.jar,org.eclipse.e4.ui.di_1.2.100.v20170414-1137.jar,org.eclipse.update.configurator_3.3.400.v20160506-0750.jar@3:start,org.apache.felix.scr_2.0.10.v20170501-2007.jar,javax.inject_1.0.0.v20091030.jar,org.eclipse.osgi.compatibility.state_1.1.0.v20170516-1513.jar,org.eclipse.equinox.common_3.9.0.v20170207-1454.jar@2:start,org.eclipse.ui.workbench_3.110.0.v20170612-1255.jar,org.w3c.dom.events_3.0.0.draft20060413_v201105210656.jar,org.eclipse.emf.common_2.13.0.v20170609-0707.jar,org.eclipse.help_3.8.0.v20160823-1530.jar");
		initProps.put("osgi.bundles.defaultStartLevel", "4");
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
		// initProps.put("osgi.parentClassloader","app");

		initProps.put("osgi.noShutdown", "false");
		// initProps.put("eclipse.ignoreApp", "true");

		initProps.put("eclipse.application.launchDefault", "true");
		initProps.put("eclipse.allowAppRelaunch", "true");
		EclipseStarter.setInitialProperties(initProps);

		String[] equinoxArgs = { "-data", "/Users/seba/eclipse-workspace/../runtime-equo.product5", "-dev",
				"file:/Users/seba/eclipse-workspace/.metadata/.plugins/org.eclipse.pde.core/equo.product/dev.properties",
				"-os", "macosx", "-ws", "cocoa", "-arch", "x86_64", "-consoleLog", "-clearPersistedState",
				"-application", "com.make.equo.application" };

		BundleContext context = EclipseStarter.startup(equinoxArgs, null);
		context.installBundle("file:/Users/seba/Desktop/plugins/com.make.equo.application.api_1.0.0.jar");
		Bundle providerBundle = context
				.installBundle("file:/Users/seba/Desktop/plugins/com.make.equo.application.provider_1.0.0.jar");
		providerBundle.start();

		String[] args = { "-appName", "com.make.equo.application", "-application",
				"org.eclipse.e4.ui.workbench.swt.E4Application", "-" + IWorkbench.XMI_URI_ARG,
				"com.make.equo.application.provider/Application.e4xmi", "-clearPersistedState",
				// "-" + IWorkbench.LIFE_CYCLE_URI_ARG,
				// "bundleclass://com.make.equo.application.provider/com.make.equo.application.LifeCycleManager",
				"-XstartOnFirstThread" };
		return EclipseStarter.run(args);
	}

	public static UrlMandatoryFieldBuilder name(String name) {
		EquoApplicationBuilder equoApplicationBuilder = new EquoApplicationBuilder(name);
		return equoApplicationBuilder.name(name);
	}
}
