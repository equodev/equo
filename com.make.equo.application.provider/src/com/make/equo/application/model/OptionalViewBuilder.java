package com.make.equo.application.model;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;

import com.make.equo.server.api.IEquoServer;

public class OptionalViewBuilder extends OptionalFieldBuilder {

	@Inject
	private IEquoServer equoServer;

	private UrlMandatoryBuilder urlMandatoryBuilder;

	OptionalViewBuilder(UrlMandatoryBuilder urlMandatoryBuilder) {
		super(urlMandatoryBuilder.getEquoApplicationBuilder());
		this.urlMandatoryBuilder = urlMandatoryBuilder;
	}

	public OptionalViewBuilder addShortcut(String keySequence, Runnable runnable) {
		return addShortcut(keySequence, runnable, null);
	}

	public OptionalViewBuilder addShortcut(String keySequence, Runnable runnable, String userEvent) {
		EquoApplicationBuilder equoAppBuilder = this.urlMandatoryBuilder.getEquoApplicationBuilder();
		new GlobalShortcutBuilder(equoAppBuilder, this.urlMandatoryBuilder.getPart().getElementId(), runnable,
				userEvent).addGlobalShortcut(keySequence);
		return this;
	}

	public OptionalViewBuilder addShortcut(String keySequence, String userEvent) {
		return addShortcut(keySequence, null, userEvent);
	}

	/**
	 * Add a Javascript script that can modify the html content of the application.
	 * This script is added to the main page of the Equo application.
	 * 
	 * Uses cases a custom Javascript script include the removal, addition, and
	 * modification of existing HTML elements. An already existing application can
	 * work perfectly on the web, but it will need some changes to be adapted to the
	 * Desktop. Adding a custom js script allows to perform this kind of task.
	 * 
	 * @param scriptPath
	 *            the path to the Javascript script or a URL. Note that this
	 *            argument can be either a path which is relative to the source
	 *            folder where the script is defined or a well formed URL. For
	 *            example, if a script 'x.js' is defined inside a folder 'y' which
	 *            is defined inside a source folder 'resources', the path to the
	 *            script will be 'y/x.js'.
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
	 *             TODO add support to save scripts per url (To be defined)
	 * 
	 */
	private OptionalViewBuilder addCustomScript(String url, String scriptPath) throws IOException, URISyntaxException {
		URI scriptUri = new URI(scriptPath);
		String scriptReference;
		if (!scriptUri.isAbsolute()) {
			scriptReference = equoServer.getLocalScriptProtocol() + scriptPath;
		} else if (scriptUri.getScheme().startsWith("http")) {
			scriptReference = scriptPath;
		} else {
			scriptReference = equoServer.getBundleScriptProtocol() + scriptPath;
		}
		addCustomScriptToProxyAddon(url, scriptReference);
		return this;
	}

	private void addCustomScriptToProxyAddon(String url, String resolvedUrl) {
		equoServer.addCustomScript(url, resolvedUrl);
	}

}
