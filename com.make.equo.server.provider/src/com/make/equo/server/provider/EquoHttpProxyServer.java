package com.make.equo.server.provider;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.extras.SelfSignedMitmManager;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.osgi.framework.Bundle;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import com.make.equo.server.api.IEquoServer;
import com.make.equo.server.offline.api.IEquoOfflineServer;
import com.make.equo.server.offline.api.filters.IHttpRequestFilter;
import com.make.equo.ws.api.IEquoWebSocketService;

@Component
public class EquoHttpProxyServer implements IEquoServer {

	public static final String EQUO_WEBSOCKETS_JS_PATH = "equoWebsockets/";
	public static final String EQUO_PROXY_SERVER_PATH = "equoFramework/";
	public static final String LOCAL_SCRIPT_APP_PROTOCOL = "main_app_equo_script/";
	public static final String BUNDLE_SCRIPT_APP_PROTOCOL = "external_bundle_equo_script/";
	public static final String LOCAL_FILE_APP_PROTOCOL = "equo/";

	private static final String URL_PATH = "urlPath";
	private static final String PATH_TO_STRING_REG = "PATHTOSTRING";
	private static final String URL_SCRIPT_SENTENCE = "<script src=\"urlPath\"></script>";
	private static final String LOCAL_SCRIPT_SENTENCE = "<script src=\"PATHTOSTRING\"></script>";
	private static final String equoFrameworkJsApi = "equoFramework.js";

	private List<String> proxiedUrls = new ArrayList<>();
	private Map<String, List<String>> urlsToScripts = new HashMap<String, List<String>>();
	private boolean enableOfflineCache = false;
	private String limitedConnectionAppBasedPagePath;

	private HttpProxyServer proxyServer;
	private Bundle mainEquoAppBundle;

	@Inject
	private IEquoWebSocketService equoWebsocketServer;

	// TODO check if it works when it's null. Add cardinality to this service in
	// case it fails. Use @Reference.
	@Inject
	private IEquoOfflineServer equoOfflineServer;

	private ScheduledExecutorService internetConnectionChecker;

	@Override
	public void startServer() {
		EquoHttpFiltersSourceAdapter httpFiltersSourceAdapter = new EquoHttpFiltersSourceAdapter(equoWebsocketServer,
				equoOfflineServer, isOfflineCacheSupported(), limitedConnectionAppBasedPagePath, mainEquoAppBundle,
				proxiedUrls, getEquoFrameworkJsApi(), getEquoWebsocketsApi(), getUrlsToScriptsAsStrings());

		Runnable internetConnectionRunnable = new Runnable() {
			@Override
			public void run() {
				if (!isInternetReachable()) {
					httpFiltersSourceAdapter.setConnectionLimited();
				} else {
					httpFiltersSourceAdapter.setConnectionUnlimited();
				}
			}
		};
		internetConnectionChecker = Executors.newSingleThreadScheduledExecutor();
		internetConnectionChecker.scheduleAtFixedRate(internetConnectionRunnable, 0, 5, TimeUnit.SECONDS);

		proxyServer = DefaultHttpProxyServer
			.bootstrap()
			.withPort(9896)
			.withManInTheMiddle(new SelfSignedMitmManager())
			.withAllowRequestToOriginServer(true)
			.withTransparent(false)
			.withFiltersSource(httpFiltersSourceAdapter)
			.start();
	}

	@Deactivate
	public void stop() {
		System.out.println("Stopping proxy...");
		if (internetConnectionChecker != null) {
			internetConnectionChecker.shutdownNow();
		}
		if (proxyServer != null) {
			proxyServer.stop();
		}
	}

	@Override
	public void addCustomScript(String url, String scriptUrl) {
		if (!urlsToScripts.containsKey(url)) {
			urlsToScripts.put(url, new ArrayList<>());
		}
		urlsToScripts.get(url).add(scriptUrl);
	}

	@Override
	public void addUrl(String url) {
		proxiedUrls.add(url);
	}

	@Override
	public void setMainAppBundle(Bundle mainEquoAppBundle) {
		this.mainEquoAppBundle = mainEquoAppBundle;
	}

	@Override
	public String getLocalScriptProtocol() {
		return LOCAL_SCRIPT_APP_PROTOCOL;
	}

	@Override
	public String getBundleScriptProtocol() {
		return BUNDLE_SCRIPT_APP_PROTOCOL;
	}

	@Override
	public String getLocalFileProtocol() {
		return LOCAL_FILE_APP_PROTOCOL;
	}

	private boolean isOfflineCacheSupported() {
		return isOfflineServerSupported() && enableOfflineCache;
	}

	private boolean isOfflineServerSupported() {
		return equoOfflineServer != null;
	}

	@Override
	public void enableOfflineCache() {
		this.enableOfflineCache = true;
		if (isOfflineCacheSupported()) {
			equoOfflineServer.setProxiedUrls(proxiedUrls);
		}
	}

	@Override
	public void addOfflineSupportFilter(IHttpRequestFilter httpRequestFilter) {
		if (isOfflineCacheSupported()) {
			equoOfflineServer.addHttpRequestFilter(httpRequestFilter);
		}
	}

	private boolean isInternetReachable() {
		if (proxiedUrls.isEmpty()) {
			return false;
		}
		return isAddressReachable(proxiedUrls.get(0));
	}

	@Override
	public boolean isAddressReachable(String appUrl) {
		try (Socket socket = new Socket()) {
			URI uri = new URI(appUrl);
			String host = uri.getHost();
			socket.connect(new InetSocketAddress(host, 80));
			return true;
		} catch (IOException | URISyntaxException e) {
			return false; // Either timeout or unreachable or failed DNS lookup.
		}
	}

	@Override
	public void addLimitedConnectionPage(String limitedConnectionPagePath) {
		this.limitedConnectionAppBasedPagePath = limitedConnectionPagePath;
	}

	private String getEquoWebsocketsApi() {
		String equoWebsocketsJsResourceName = equoWebsocketServer.getEquoWebsocketsJsResourceName();
		return createLocalScriptSentence(EQUO_WEBSOCKETS_JS_PATH + equoWebsocketsJsResourceName);
	}

	private String getEquoFrameworkJsApi() {
		return createLocalScriptSentence(EQUO_PROXY_SERVER_PATH + equoFrameworkJsApi);
	}

	private Map<String, String> getUrlsToScriptsAsStrings() {
		Map<String, String> urlToScriptsAsStrings = new HashMap<>();
		for (String url : urlsToScripts.keySet()) {
			List<String> scriptsList = urlsToScripts.get(url);
			String convertedJsScriptsToString = convertJsScriptsToString(scriptsList);
			urlToScriptsAsStrings.put(url, convertedJsScriptsToString);
		}
		return urlToScriptsAsStrings;
	}

	private String convertJsScriptsToString(List<String> scriptsList) {
		if (scriptsList.isEmpty()) {
			return "";
		}
		String newLineSeparetedScripts = scriptsList.stream().map(string -> generateScriptSentence(string))
				.collect(Collectors.joining("\n"));
		return newLineSeparetedScripts;
	}

	private String generateScriptSentence(String scriptPath) {
		try {
			if (isLocalScript(scriptPath)) {
				return createLocalScriptSentence(scriptPath);
			} else {
				URL url = new URL(scriptPath);
				String scriptSentence = URL_SCRIPT_SENTENCE.replaceAll(URL_PATH, url.toString());
				return scriptSentence;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return "";
	}

	private String createLocalScriptSentence(String scriptPath) {
		String scriptSentence = LOCAL_SCRIPT_SENTENCE.replaceAll(PATH_TO_STRING_REG, scriptPath);
		return scriptSentence;
	}

	private boolean isLocalScript(String scriptPath) {
		String scriptPathLoweredCase = scriptPath.trim().toLowerCase();
		return scriptPathLoweredCase.startsWith(EquoHttpProxyServer.LOCAL_SCRIPT_APP_PROTOCOL)
				|| scriptPathLoweredCase.startsWith(EquoHttpProxyServer.BUNDLE_SCRIPT_APP_PROTOCOL);
	}

}
