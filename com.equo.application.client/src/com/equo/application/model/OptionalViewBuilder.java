package com.equo.application.model;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;

import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.workbench.modeling.IWindowCloseHandler;

import com.equo.analytics.internal.api.AnalyticsService;
import com.equo.application.api.IEquoApplication;
import com.equo.application.handlers.AppStartupCompleteEventHandler;
import com.equo.application.impl.EnterFullScreenModeRunnable;
import com.equo.contribution.api.EquoContributionBuilder;
import com.equo.server.api.IEquoServer;
import com.equo.server.offline.api.filters.IHttpRequestFilter;

public class OptionalViewBuilder {

	private IEquoServer equoServer;
	private ViewBuilder viewBuilder;
	private EquoApplicationBuilder equoApplicationBuilder;

	private AnalyticsService analyticsService;
	private MMenu mainMenu;
	private EquoContributionBuilder mainAppBuilder;
	private EquoContributionBuilder offlineSupportBuilder;

	OptionalViewBuilder(ViewBuilder viewBuilder, IEquoServer equoServer, AnalyticsService analyticsService,
			EquoContributionBuilder mainAppBuilder, EquoContributionBuilder offlineSupportBuilder,
			IEquoApplication equoApp) {
		this.viewBuilder = viewBuilder;
		this.equoServer = equoServer;
		this.analyticsService = analyticsService;
		this.mainAppBuilder = mainAppBuilder;
		this.offlineSupportBuilder = offlineSupportBuilder;
		this.equoApplicationBuilder = viewBuilder.getEquoApplicationBuilder();
	}

	/**
	 * Add the shortcut with a custom runnable.
	 * 
	 * @param keySequence the shortcut command.
	 * @param runnable    the runnable.
	 * @return this.
	 */
	public OptionalViewBuilder addShortcut(String keySequence, Runnable runnable) {
		return addShortcut(keySequence, runnable, null);
	}

	/**
	 * Adds the shortcut for the user event and custom runnable.
	 * 
	 * @param keySequence the shortcut command.
	 * @param runnable    the runnable.
	 * @param userEvent   the user event.
	 * @return this.
	 */
	public OptionalViewBuilder addShortcut(String keySequence, Runnable runnable, String userEvent) {
		EquoApplicationBuilder equoAppBuilder = this.viewBuilder.getEquoApplicationBuilder();
		new GlobalShortcutBuilder(equoAppBuilder, this.viewBuilder.getPart().getElementId(), runnable, userEvent)
				.addGlobalShortcut(keySequence);
		return this;
	}

	/**
	 * Adds the shortcut for the user event.
	 * 
	 * @param keySequence the shortcut command.
	 * @param userEvent   the user event.
	 * @return this.
	 */
	public OptionalViewBuilder addShortcut(String keySequence, String userEvent) {
		return addShortcut(keySequence, null, userEvent);
	}

	/**
	 * Removes the shortcut command.
	 * 
	 * @param keySequence the shortcut command.
	 * @return this.
	 */
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

	/**
	 * Starts Equo application.
	 * 
	 * @return the EquoApplicationBuilder instance.
	 */
	public EquoApplicationBuilder start() {
		return this.viewBuilder.start();
	}

	/**
	 * Creates a root menu, visible in the bar.
	 * 
	 * @param menuLabel the menu title.
	 * @return the MenuBuilder instance.
	 */
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

	/**
	 * Adds new menu item with full screen mode.
	 * 
	 * @param label the menu title.
	 * @return this.
	 */
	public OptionalViewBuilder addFullScreenModeShortcut(String keySequence) {
		return addShortcut(keySequence, EnterFullScreenModeRunnable.instance);
	}

	public OptionalViewBuilder enableAnalytics() {
		if (analyticsService != null) {
			analyticsService.enableAnalytics();
		}
		return this;
	}

	public OptionalViewBuilder trustAnySSLCert(boolean trustAllServers) {
		return this.viewBuilder.setSSLTrust(trustAllServers);
	}

	/**
	 * Introduce some action before application closes and decide if continue
	 * exiting or not.
	 * 
	 * @param beforeExitCallable The action to perform. Return 'true' to exit,
	 *                           'false' to cancel
	 * @return
	 */
	public OptionalViewBuilder beforeExit(Callable<Boolean> beforeExitCallable) {
		IWindowCloseHandler handler = new IWindowCloseHandler() {
			@Override
			public boolean close(MWindow window) {
				try {
					return beforeExitCallable.call();
				} catch (Exception e) {
					return true;
				}
			}

		};
		AppStartupCompleteEventHandler.getInstance().setRunnable(
				() -> equoApplicationBuilder.getmWindow().getContext().set(IWindowCloseHandler.class, handler));
		return this;
	}

	OptionalViewBuilder withBaseHtml(String baseHtmlFile) throws URISyntaxException {
		mainAppBuilder.withBaseHtmlResource(baseHtmlFile);
		return this;
	}

	/**
	 * Adds a toolbar to current builder instance.
	 * 
	 * @return the ToolbarBuilder instance.
	 */
	public ToolbarBuilder withToolbar() {
		return new ToolbarBuilder(this, equoApplicationBuilder.getmWindow()).addToolbar();
	}

}
