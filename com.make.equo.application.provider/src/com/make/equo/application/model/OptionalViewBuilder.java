package com.make.equo.application.model;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;

import com.make.equo.application.util.FrameworkUtils;

public class OptionalViewBuilder extends OptionalFieldBuilder {

	private UrlMandatoryBuilder urlMandatoryBuilder;

	OptionalViewBuilder(UrlMandatoryBuilder urlMandatoryBuilder) {
		super(urlMandatoryBuilder.getEquoApplicationBuilder());
		this.urlMandatoryBuilder = urlMandatoryBuilder;
	}

	public OptionalViewBuilder addShortcut(String keySequence, Runnable runnable) {
		EquoApplicationBuilder equoAppBuilder = this.urlMandatoryBuilder.getEquoApplicationBuilder();
		new GlobalShortcutBuilder(equoAppBuilder, this.urlMandatoryBuilder.getPart().getElementId(), runnable)
				.addGlobalShortcut(keySequence);
		return this;
	}

	/**
	 * Add a Javascript script that can modify the html content of the application. This
	 * script is added to the main page of the Equo application.
	 * 
	 * Uses cases a custom Javascript script include the removal, addition, and
	 * modification of existing HTML elements. An already existing application can
	 * work perfectly on the web, but it will need some changes to be adapted to the
	 * Desktop. Adding a custom js script allows to perform this kind of task.
	 * 
	 * @param scriptPath
	 *            the path to the Javascript script or a URL. Note that this
	 *            argument can be either a path which is relative to the source folder
	 *            where the script is defined or a well formed URL. For example, if
	 *            a script 'x.js' is defined inside a folder 'y' which is defined
	 *            inside a source folder 'resources', the path to the script will be
	 *            'y/x.js'.
	 *            
	 * @return this builder
	 * @throws IOException
	 * @throws URISyntaxException
	 * 
	 */
	public OptionalViewBuilder addCustomScript(String scriptPath) throws IOException, URISyntaxException {
		String url = urlMandatoryBuilder.getUrl();
		return addCustomScript(url, scriptPath);
	}

	/**
	 * Add a Javascript script that can modify the html content of the given url.
	 * 
	 * Uses cases a custom Javascript script include the removal, addition, and
	 * modification of existing HTML elements. An already existing application can
	 * work perfectly on the web, but it will need some changes to be adapted to the
	 * Desktop. Adding a custom js script allows to perform this kind of task.
	 * 
	 * @param scriptPath
	 *            the path to the Javascript script or a URL. Note that this
	 *            argument can be a path which is relative to the source folder
	 *            where the script is defined or a well defined URL. For example, if
	 *            a script 'x.js' is defined inside a folder 'y' which is defined
	 *            inside a source folder 'resources', the path to the script will be
	 *            'y/x.js'.
	 * @return this builder
	 * @throws IOException
	 * @throws URISyntaxException
	 * 
	 * TODO add support to save scripts per url (To be defined)
	 * 
	 */
	private OptionalViewBuilder addCustomScript(String url, String scriptPath) throws IOException, URISyntaxException {
		URI scriptUri = new URI(scriptPath);
		URL scriptUrl;
		if (!scriptUri.isAbsolute()) {
			scriptUrl = FrameworkUtils.INSTANCE.getEntry(scriptPath);
		} else {
			scriptUrl = new URL(scriptPath);
		}
		URL resolvedUrl = FileLocator.resolve(scriptUrl);
		addCustomScriptToProxyAddon(url, resolvedUrl);
		return this;
	}

	private void addCustomScriptToProxyAddon(String url, URL resolvedUrl) {
		Map<String, Object> transientData = getEquoApplicationBuilder().getEquoProxyServerAddon().getTransientData();
		@SuppressWarnings("unchecked")
		List<String> scripts = (List<String>) transientData.get(url);
		if (scripts == null) {
			scripts = new ArrayList<>();
		}
		scripts.add(resolvedUrl.toString());
		transientData.put(url, scripts);
	}

}
