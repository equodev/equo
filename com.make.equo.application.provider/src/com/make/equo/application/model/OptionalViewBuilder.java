package com.make.equo.application.model;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimBar;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuFactoryImpl;

import com.make.equo.analytics.internal.api.AnalyticsService;
import com.make.equo.application.impl.EnterFullScreenModeRunnable;
import com.make.equo.server.api.IEquoServer;
import com.make.equo.server.offline.api.filters.IHttpRequestFilter;

public class OptionalViewBuilder {

	private IEquoServer equoServer;

	private ViewBuilder viewBuilder;

	private EquoApplicationBuilder equoApplicationBuilder;

	private AnalyticsService analyticsService;

	private MMenu mainMenu;

	OptionalViewBuilder(ViewBuilder viewBuilder, IEquoServer equoServer, AnalyticsService analyticsService) {
		this.viewBuilder = viewBuilder;
		this.equoServer = equoServer;
		this.analyticsService = analyticsService;
		this.equoApplicationBuilder = viewBuilder.getEquoApplicationBuilder();
	}

	public OptionalViewBuilder addShortcut(String keySequence, Runnable runnable) {
		return addShortcut(keySequence, runnable, null);
	}

	public OptionalViewBuilder addShortcut(String keySequence, Runnable runnable, String userEvent) {
		EquoApplicationBuilder equoAppBuilder = this.viewBuilder.getEquoApplicationBuilder();
		new GlobalShortcutBuilder(equoAppBuilder, this.viewBuilder.getPart().getElementId(), runnable, userEvent)
				.addGlobalShortcut(keySequence);
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
	 * @param scriptPath the path to the Javascript script or a URL. Note that this
	 *                   argument can be either a path which is relative to the
	 *                   source folder where the script is defined or a well formed
	 *                   URL. For example, if a script 'x.js' is defined inside a
	 *                   folder 'y' which is defined inside a source folder
	 *                   'resources', the path to the script will be 'y/x.js'.
	 * 
	 * @return this builder
	 * @throws IOException
	 * @throws URISyntaxException
	 * 
	 */
	public OptionalViewBuilder withCustomScript(String scriptPath) throws IOException, URISyntaxException {
		String url = viewBuilder.getUrl();
		return withCustomScript(url, scriptPath);
	}

	/**
	 * Add a Javascript script that can modify the html content of the given url.
	 * 
	 * Uses cases a custom Javascript script include the removal, addition, and
	 * modification of existing HTML elements. An already existing application can
	 * work perfectly on the web, but it will need some changes to be adapted to the
	 * Desktop. Adding a custom js script allows to perform this kind of task.
	 * 
	 * @param scriptPath the path to the Javascript script or a URL. Note that this
	 *                   argument can be a path which is relative to the source
	 *                   folder where the script is defined or a well defined URL.
	 *                   For example, if a script 'x.js' is defined inside a folder
	 *                   'y' which is defined inside a source folder 'resources',
	 *                   the path to the script will be 'y/x.js'.
	 * @return this builder
	 * @throws IOException
	 * @throws URISyntaxException
	 * 
	 *                            TODO add support to save scripts per url (To be
	 *                            defined)
	 * 
	 */
	private OptionalViewBuilder withCustomScript(String url, String scriptPath) throws URISyntaxException {
		URI scriptUri = new URI(scriptPath);
		String scriptReference;
		if (!scriptUri.isAbsolute()) {
			scriptReference = equoServer.getLocalScriptProtocol() + scriptPath;
		} else if (scriptUri.getScheme().startsWith("http")) {
			scriptReference = scriptPath;
		} else {
			scriptReference = equoServer.getBundleScriptProtocol() + scriptPath;
		}
		addCustomScriptToProxyServer(url, scriptReference);
		return this;
	}

	private void addCustomScriptToProxyServer(String url, String resolvedUrl) {
		equoServer.addCustomScript(url, resolvedUrl);
	}

	/**
	 * Enable an offline cache which will be used when there is no internet
	 * connection or a limited one. This functionality will only work if and only if
	 * the application was run at least once with a working internet connection.
	 * 
	 * @return this optional builder
	 */
	public OptionalViewBuilder enableOfflineSupport() {
		equoServer.enableOfflineCache();
		return this;
	}

	public OptionalViewBuilder addOfflineSupportFilter(IHttpRequestFilter httpRequestFilter) {
		equoServer.addOfflineSupportFilter(httpRequestFilter);
		return this;
	}

	/**
	 * Add a limited or no connection custom page for the case that there is no
	 * internet connection or a limited one. If an offline cache is enabled, see
	 * {@link #enableOfflineSupport()}, then this method has no effect.
	 * 
	 * @param limitedConnectionPagePath
	 * @return this optional builder
	 * @throws URISyntaxException
	 */
	public OptionalViewBuilder addLimitedConnectionPage(String limitedConnectionPagePath) throws URISyntaxException {
		URI scriptUri = new URI(limitedConnectionPagePath);
		String limitedConnectionPagePathWithPrefix;
		if (!scriptUri.isAbsolute()) {
			limitedConnectionPagePathWithPrefix = equoServer.getLocalFileProtocol() + limitedConnectionPagePath;
		} else {
			limitedConnectionPagePathWithPrefix = equoServer.getBundleScriptProtocol() + limitedConnectionPagePath;
		}
		equoServer.addLimitedConnectionPage(limitedConnectionPagePathWithPrefix);
		return this;
	}

	public IApplicationBuilder start() {
		return this.viewBuilder.start();
	}

	public MenuBuilder withMainMenu(String menuLabel) {
		mainMenu = equoApplicationBuilder.getmWindow().getMainMenu();
		return new MenuBuilder(this).addMenu(menuLabel);
	}

	EquoApplicationBuilder getEquoApplicationBuilder() {
		return equoApplicationBuilder;
	}

	MMenu getMainMenu() {
		return mainMenu;
	}

	public OptionalViewBuilder addFullScreenModeShortcut(String keySequence) {
		return addShortcut(keySequence, EnterFullScreenModeRunnable.instance);
	}

	public OptionalViewBuilder enableAnalytics() {
		analyticsService.enableAnalytics();
		return this;
	}

	public OptionalViewBuilder withBaseHtml(String baseHtmlFile) throws URISyntaxException {
		equoServer.withBaseHtml(baseHtmlFile);
		return this;
	}

	public ToolbarBuilder withToolbar() {
		
		return new ToolbarBuilder(this,equoApplicationBuilder.getmWindow()).addToolbar();
	}	

}
