package com.make.equo.application.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.adaptor.EclipseStarter;

public enum EquoBundleManager {

	INSTANCE;

	private static final String NETTY_HTTP_REQUEST_PACKAGE = "io.netty.handler.codec.http;resolution:=optional";

	private static final String NETTY_BUFFER_PACKAGE = "io.netty.buffer;resolution:=optional";

	private static final String EQUO_APPLICATION_ID = "com.make.equo.application";

	private static final String EQUO_API_PACKAGE = EQUO_APPLICATION_ID + ".api";

	private static final String EQUO_MODEL_PACKAGE = EQUO_APPLICATION_ID + ".model";

	private static final String EQUO_OFFLINE_PACKAGE = "com.make.equo.server.offline.api.filters";

	private static final String EXPORT_PACKAGE = "Export-Package";

	private static final String IMPORT_PACKAGE = "Import-Package";

	private static final String BUNDLE_CLASS_PATH = "Bundle-ClassPath";

	private static final String BUNDLE_VERSION = "Bundle-Version";

	private static final String BUNDLE_SYMBOLIC_NAME = "Bundle-SymbolicName";

	private static final String BUNDLE_NAME = "Bundle-Name";

	private static final String BUNDLE_MANIFEST_VERSION = "Bundle-ManifestVersion";

	private static final String MANIFEST_PATH = "/META-INF/MANIFEST.MF";

	private String mainAppBundleName;

	private String getBundleName(File manifestFile) {
		return getBuildDirectory(manifestFile).getParentFile().getName();
	}

	private File getBuildDirectory(File manifestFile) {
		return manifestFile.getParentFile().getParentFile();
	}

	private String buildJarFilesList(File directory) {
		final String SEPARATOR = ", ";
		StringBuilder builder = new StringBuilder();
		if (directory != null && directory.isDirectory()) {
			for (String fileName : getJarFilesList(directory)) {
				builder.append(fileName);
				builder.append(SEPARATOR);
			}
			if (builder.length() >= SEPARATOR.length()) {
				builder.setLength(builder.length() - SEPARATOR.length());
			}
			return builder.toString();
		} else {
			return null;
		}
	}

	private List<String> getJarFilesList(File directory) {
		List<String> files = new ArrayList<String>();
		for (File file : directory.listFiles(new JarFilter())) {
			files.add(file.getName());
		}
		return files;
	}

	private String buildPackagesList(File directory, String equoAppPackage) {
		Set<String> packages = new HashSet<>();
		final String SEPARATOR = ", ";
		StringBuilder builder = new StringBuilder();
		if (directory != null && directory.isDirectory()) {
			for (File file : directory.listFiles(new JarFilter())) {
				ZipInputStream zipStream = null;
				try {
					zipStream = new ZipInputStream(new FileInputStream(file));
					ZipEntry entry;
					while ((entry = zipStream.getNextEntry()) != null) {
						if (FilenameUtils.getExtension(entry.getName()).equals("class")) {
							packages.add(FilenameUtils.getPathNoEndSeparator(entry.getName()).replace('/', '.'));
						}
					}
				} catch (IOException e) {
					// Logger.log(Logger.SEVERITY_ERROR, e);
					System.out.println("Unable to build package list.");
					e.printStackTrace();
				} finally {
					IOUtils.closeQuietly(zipStream);
				}
			}
			for (String pkg : packages) {
				builder.append(pkg);
				builder.append(SEPARATOR);
			}
			builder.append(equoAppPackage);
			builder.append(SEPARATOR);
			if (builder.length() >= SEPARATOR.length()) {
				builder.setLength(builder.length() - SEPARATOR.length());
			}
			return builder.toString();
		} else {
			return null;
		}
	}

	private String getDirectoryPath(Class<?> equoApplicationClazz) {
		String className = equoApplicationClazz.getSimpleName() + ".class";
		String classPath = equoApplicationClazz.getResource(className).toString();
		String directoryPath = null;

		if (classPath.startsWith("jar")) {
			directoryPath = classPath.substring(0, classPath.lastIndexOf("!") + 1);
		} else if (classPath.startsWith("file")) {
			String classNameFull = equoApplicationClazz.getName().replace('.', '/');
			directoryPath = classPath.substring(5, classPath.lastIndexOf(classNameFull) - 1);
		} else {
			System.out.format("Could not find manifest for %s\n", classPath);
		}
		return directoryPath;
	}

	public File convertAppToBundle(Class<?> equoApplicationClazz) {
		String parentPath = getDirectoryPath(equoApplicationClazz);
		System.out.println("The directory parent is " + parentPath);
		if (parentPath != null) {
			String manifestPath = parentPath + MANIFEST_PATH;
			File manifestFile = new Path(manifestPath).toFile();
			FileOutputStream manifestStream = null;
			try {
				FileUtils.forceMkdir(manifestFile.getParentFile());
				manifestStream = new FileOutputStream(manifestFile);
				Manifest manifest = new Manifest();
				Attributes atts = manifest.getMainAttributes();
				atts.put(Attributes.Name.MANIFEST_VERSION, "1.0");
				atts.putValue(BUNDLE_MANIFEST_VERSION, "2");
				atts.putValue(BUNDLE_NAME, "Bundled Equo Application");
				String bundleName = getBundleName(manifestFile);
				atts.putValue(BUNDLE_SYMBOLIC_NAME, bundleName);
				setMainAppBundleName(bundleName);
				atts.putValue(BUNDLE_VERSION, "1.0");
				String jarFilesList = buildJarFilesList(new File(parentPath));
				if (!jarFilesList.isEmpty()) {
					atts.putValue(BUNDLE_CLASS_PATH, jarFilesList);
				}
				atts.putValue(EXPORT_PACKAGE,
						buildPackagesList(new File(parentPath), equoApplicationClazz.getPackage().getName()));
				atts.putValue(IMPORT_PACKAGE, buildImportPackageList());
				manifest.write(manifestStream);
			} catch (IOException e) {
				System.out.println("Unable to generate MANIFEST.MF");
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(manifestStream);
			}
			// TODO for development we are returning the bin or target dir, see how to
			// handle it in
			// build time...
			return getBuildDirectory(manifestFile);
		}
		throw new RuntimeException("There is no Java Project for the " + equoApplicationClazz.getName() + " class.");
	}

	private String buildImportPackageList() {
		final String SEPARATOR = ", ";
		StringBuilder builder = new StringBuilder();
		builder.append(EQUO_API_PACKAGE);
		builder.append(SEPARATOR);
		builder.append(EQUO_MODEL_PACKAGE);
		builder.append(SEPARATOR);
		builder.append(EQUO_OFFLINE_PACKAGE);
		builder.append(SEPARATOR);
		builder.append(NETTY_HTTP_REQUEST_PACKAGE);
		builder.append(SEPARATOR);
		builder.append(NETTY_BUFFER_PACKAGE);
		return builder.toString();
	}

	public Map<String, String> initializeBundleProperties(File appBundleFile) {
		System.out.println("App bundle file is " + appBundleFile.getAbsolutePath());
		Map<String, String> bundleInitProps = new HashMap<String, String>();

		bundleInitProps.put("osgi.clean", "true");
		bundleInitProps.put("osgi.dev", "true");
		// initProps.put("osgi.debug", "true");
		// bundleInitProps.put("osgi.install.area", "this.installLocation");
		bundleInitProps.put("osgi.instance.area", "this.workspace");
		bundleInitProps.put("osgi.configuration.area", "generated");

		bundleInitProps.put("osgi.bundles", appBundleFile.getAbsolutePath() + ",\n" + " com.make.equo.ws.api,\n"
				+ " com.make.equo.ws.provider,\n" + " com.make.equo.application.provider,\n"
				+ " com.make.equo.server.api,\n" + " com.make.equo.server.provider,\n"
				+ " com.make.equo.server.offline.api,\n" + " com.make.equo.server.offline.provider,\n"
				+ " com.make.equo.analytics.client.api,\n" + " com.make.equo.analytics.client.provider,\n"
				+ " com.make.equo.analytics.internal.api,\n" + " com.make.equo.analytics.internal.provider,\n"
				+ " com.squareup.okhttp3.okhttp,\n" + " com.squareup.okhttp3.logging-interceptor,\n"
				+ " com.squareup.okio,\n" + " com.squareup.retrofit2.retrofit,\n" + "com.squareup.moshi,\n"
				+ " com.squareup.retrofit2.converter-moshi,\n" + " com.github.jnr.ffi,\n" + " com.github.jnr.jffi,\n"
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
				+ " org.objectweb.asm_5.1.0.v20160914-0701.jar,\n" + " org.objectweb.asm.analysis_5.0.3.jar,\n"
				+ " org.objectweb.asm.commons_5.0.3.jar,\n" + " org.objectweb.asm.tree_5.0.3.jar,\n"
				+ " org.objectweb.asm.util_5.0.3.jar,\n" + " org.w3c.css.sac,\n" + " org.w3c.dom.events,\n"
				+ " org.w3c.dom.smil,\n" + " org.w3c.dom.svg,\n" + " org.eclipse.update.configurator@3:start,\n"
				+ " org.eclipse.ui.workbench,\n" + " org.eclipse.ui,\n" + " org.eclipse.help,\n"
				+ " org.eclipse.e4.ui.workbench.addons.swt,\n" + " org.eclipse.jetty.server,\n"
				+ " org.eclipse.jetty.servlet,\n" + " org.eclipse.jetty.util,\n" + " javax.servlet,\n"
				+ " org.eclipse.jetty.client,\n" + " org.eclipse.jetty.http,\n" + " org.eclipse.jetty.proxy,\n"
				+ " org.eclipse.jetty.io,\n" + " org.eclipse.jetty.security,\n" + " org.eclipse.equinox.console,\n"
				+ " org.eclipse.jetty.servlets,\n" + " xyz.rogfam.littleproxy,\n" + " org.apache.commons.io,\n"
				+ " io.netty.all,\n" + " com.google.guava_20.0.0.jar,\n" + " com.barchart.udt.barchart-udt-bundle,\n"
				+ " org.apache.commons.lang3,\n" + " org.slf4j.api,\n" + " org.slf4j.impl.log4j12,\n"
				+ " org.apache.log4j,\n" + " org.apache.httpcomponents.httpcore,\n"
				+ " org.java-websocket.Java-WebSocket,\n" + " com.google.gson,\n" + " org.eclipse.ui.cocoa,\n"
				+ "org.influxdb.java");
		bundleInitProps.put("osgi.bundles.defaultStartLevel", "4");
		bundleInitProps.put("osgi.instance.area", "/Users/seba/eclipse-workspace/../runtime-equo.product7");
		bundleInitProps.put("osgi.dev",
				"file:/Users/seba/eclipse-workspace/.metadata/.plugins/org.eclipse.pde.core/equo.product/dev.properties");
		bundleInitProps.put("osgi.os", "macosx");
		bundleInitProps.put("osgi.ws", "cocoa");
		bundleInitProps.put("osgi.arch", "x86_64");
		bundleInitProps.put("eclipse.consoleLog", "true");
		bundleInitProps.put("osgi.os", "macosx");
		bundleInitProps.put("eclipse.consoleLog", "true");
		// bundleInitProps.put("eclipse.application", EQUO_APPLICATION_ID);
		// initProps.put("osgi.parentClassloader","app");

		bundleInitProps.put("osgi.noShutdown", "false");
		// initProps.put("eclipse.ignoreApp", "true");

		bundleInitProps.put("eclipse.application.launchDefault", "true");
		bundleInitProps.put("eclipse.allowAppRelaunch", "false");
		EclipseStarter.setInitialProperties(bundleInitProps);
		return bundleInitProps;
	}

	public String getMainAppBundleName() {
		return mainAppBundleName;
	}

	public void setMainAppBundleName(String mainAppBundleName) {
		this.mainAppBundleName = mainAppBundleName;
	}

}
