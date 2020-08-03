package com.make.equo.application.model;

import java.io.IOException;
import java.net.URISyntaxException;

import org.eclipse.e4.ui.model.application.ui.menu.MMenu;

import com.make.equo.analytics.internal.api.AnalyticsService;
import com.make.equo.application.api.IEquoApplication;
import com.make.equo.application.impl.EnterFullScreenModeRunnable;
import com.make.equo.contribution.api.EquoContributionBuilder;
import com.make.equo.server.api.IEquoServer;
import com.make.equo.server.offline.api.filters.IHttpRequestFilter;

public class OptionalViewBuilder {

	private IEquoServer equoServer;
	private ViewBuilder viewBuilder;
	private EquoApplicationBuilder equoApplicationBuilder;

	private AnalyticsService analyticsService;
	private MMenu mainMenu;
	private EquoContributionBuilder mainAppBuilder;
	private EquoContributionBuilder offlineSupportBuilder;

	OptionalViewBuilder(ViewBuilder viewBuilder, IEquoServer equoServer, AnalyticsService analyticsService,
			EquoContributionBuilder mainAppBuilder, EquoContributionBuilder offlineSupportBuilder, IEquoApplication equoApp) {
		this.viewBuilder = viewBuilder;
		this.equoServer = equoServer;
		this.analyticsService = analyticsService;
		this.mainAppBuilder = mainAppBuilder;
		this.offlineSupportBuilder = offlineSupportBuilder;
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

	public OptionalViewBuilder removeShortcut(String keySequence) {
		EquoApplicationBuilder equoAppBuilder = this.viewBuilder.getEquoApplicationBuilder();
		new GlobalShortcutBuilder(equoAppBuilder, this.viewBuilder.getPart().getElementId(), null, null)
				.removeShortcut(keySequence);
		return this;
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
		mainAppBuilder.withScriptFile(scriptPath);
		return this;
	}


	public OptionalViewBuilder withCustomStyle(String stylePath) throws IOException, URISyntaxException {
		mainAppBuilder.withStyleFile(stylePath);
		return this;
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
		offlineSupportBuilder.withBaseHtmlResource(limitedConnectionPagePath);
		return this;
	}

	public EquoApplicationBuilder start() {
		return this.viewBuilder.start();
	}

	public MenuBuilder withMainMenu(String menuLabel) {
		inicMainMenu();
		return new MenuBuilder(this).addMenu(menuLabel);
	}

	void inicMainMenu() {
		mainMenu = equoApplicationBuilder.getmWindow().getMainMenu();
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
		mainAppBuilder.withBaseHtmlResource(baseHtmlFile);
		return this;
	}

	public ToolbarBuilder withToolbar() {
		return new ToolbarBuilder(this,equoApplicationBuilder.getmWindow()).addToolbar();
	}	

}
