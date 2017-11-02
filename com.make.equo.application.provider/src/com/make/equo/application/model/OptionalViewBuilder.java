package com.make.equo.application.model;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import org.eclipse.core.runtime.FileLocator;

import com.make.equo.application.util.FrameworkUtils;
import com.make.equo.application.util.ScriptHandler;

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
	 * Add a js script that can modify the html content of the application.
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
	 */
	public OptionalViewBuilder addCustomScript(String scriptPath) throws IOException, URISyntaxException {
		URI scriptUri = new URI(scriptPath);
		URL scriptUrl;
		if (!scriptUri.isAbsolute()) {
			scriptUrl = FrameworkUtils.INSTANCE.getEntry(scriptPath);
		} else {
			scriptUrl = new URL(scriptPath);
		}
		URL resolvedUrl = FileLocator.resolve(scriptUrl);
		new ScriptHandler(urlMandatoryBuilder.getPart()).saveScript(resolvedUrl.toString());
		return this;
	}

	/**
	 * @throws IOException
	 * @see OptionalViewBuilder#addCustomScript(String)
	 */
	public OptionalViewBuilder addCustomScript(URL scriptUrl) throws IOException {
		if (scriptUrl == null) {
			throw new IOException("The script URL does not exist.");
		}
		URL url = new URL(scriptUrl.toString());
		URLConnection conn = url.openConnection();
		conn.connect();
		new ScriptHandler(urlMandatoryBuilder.getPart()).saveScript(scriptUrl.toString());
		return this;
	}

}