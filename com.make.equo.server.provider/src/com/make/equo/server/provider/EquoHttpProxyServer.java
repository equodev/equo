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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.extras.SelfSignedMitmManager;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.make.equo.aer.api.IEquoLoggingService;
import com.make.equo.application.api.IEquoApplication;
import com.make.equo.contribution.api.IEquoContribution;
import com.make.equo.server.api.IEquoServer;
import com.make.equo.server.offline.api.IEquoOfflineServer;
import com.make.equo.server.offline.api.filters.IHttpRequestFilter;

@Component
public class EquoHttpProxyServer implements IEquoServer {

	static final String EQUO_CONTRIBUTION_PATH = "equoContribution/";

	public static final String LOCAL_SCRIPT_APP_PROTOCOL = "main_app_equo_script/";
	public static final String BUNDLE_SCRIPT_APP_PROTOCOL = "external_bundle_equo_script/";
	public static final String LOCAL_FILE_APP_PROTOCOL = "equo/";

	protected static final String WEBSOCKET_CONTRIBUTION_TYPE = "websocketContribution";
	protected static final String RENDERERS_CONTRIBUTION_TYPE = "renderersContribution";

	private static final String URL_PATH = "urlPath";
	private static final String PATH_TO_STRING_REG = "PATHTOSTRING";
	private static final String URL_SCRIPT_SENTENCE = "<script src=\"urlPath\"></script>";
	private static final String LOCAL_SCRIPT_SENTENCE = "<script src=\"PATHTOSTRING\"></script>";

	private static final List<String> proxiedUrls = new ArrayList<>();
	private static final Map<String, String> urlsToScripts = new HashMap<String, String>();
	private static boolean enableOfflineCache = false;
	private static String limitedConnectionAppBasedPagePath;

	private static volatile HttpProxyServer proxyServer;
	private ScheduledExecutorService internetConnectionChecker;
	private int websocketPort;

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
	private volatile IEquoApplication equoApplication;

	@Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.STATIC)
	private volatile IEquoOfflineServer equoOfflineServer;

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
	private volatile IEquoLoggingService equoLoggingService;

	private static final Map<String, IEquoContribution> equoContributions = new LinkedHashMap<>();

	@Override
	@Activate
	public void start() {
		if (proxyServer == null && !proxiedUrls.isEmpty()) {
			startServer();
		}
	}

	@Override
	public void startServer() {
		EquoHttpFiltersSourceAdapter httpFiltersSourceAdapter = new EquoHttpFiltersSourceAdapter(equoContributions,
				equoOfflineServer, isOfflineCacheSupported(), limitedConnectionAppBasedPagePath, proxiedUrls,
				getEquoContributionsJsApis(), urlsToScripts, websocketPort, equoApplication);

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

		proxyServer = DefaultHttpProxyServer.bootstrap().withPort(9896).withManInTheMiddle(new SelfSignedMitmManager())
				.withAllowRequestToOriginServer(true).withTransparent(false).withFiltersSource(httpFiltersSourceAdapter)
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

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.STATIC)
	void addEquoContribution(IEquoContribution equoContribution, Map<String, String> props) {
		String contributionKey = props.get("type");
		System.out.println("Equo Contribution added: " + contributionKey);
		equoContributions.put(contributionKey, equoContribution);
		if (contributionKey.equals(WEBSOCKET_CONTRIBUTION_TYPE)) {
			Map<String, Object> properties = equoContribution.getProperties();
			if (!properties.containsKey("websocketPort")) {
				throw new RuntimeException(
						"The websocketPort property must be defined in the Equo websocket contribution.");
			}
			this.websocketPort = (Integer) properties.get("websocketPort");
		}
	}

	void removeEquoContribution(Map<String, Object> props) {
		equoContributions.remove(props.get("type"));
	}

	@Override
	public void addCustomScript(String url, String scriptUrl) {
		String generatedScriptSentence = generateScriptSentence(scriptUrl);
		if (!urlsToScripts.containsKey(url)) {
			urlsToScripts.put(url, generatedScriptSentence);
		} else {
			String existingScripts = urlsToScripts.get(url);
			if (!existingScripts.contains(generatedScriptSentence)) {
				urlsToScripts.put(url, appendScriptToExistingOnes(url, generatedScriptSentence));
			}
		}
	}
	
	@Override
	public void addUrl(String url) {
		if (!proxiedUrls.contains(url)) {
			proxiedUrls.add(url);
		} else {
			equoLoggingService.logWarning("The url " + url + " was already added to the Proxy server.");
		}
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
		EquoHttpProxyServer.enableOfflineCache = true;
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
		EquoHttpProxyServer.limitedConnectionAppBasedPagePath = limitedConnectionPagePath;
	}

	private List<String> getEquoContributionsJsApis() {
		List<String> javascriptApis = new ArrayList<>();
		for (String contributionType : equoContributions.keySet()) {
			IEquoContribution equoContribution = equoContributions.get(contributionType);
			List<String> javascriptFilesNames = equoContribution.getJavascriptFileNames();
			if (!javascriptFilesNames.isEmpty()) {
				Function<String, String> function = new Function<String, String>() {
					@Override
					public String apply(String input) {
						try {
							URL url = new URL(input);
							String scriptSentence = URL_SCRIPT_SENTENCE.replaceAll(URL_PATH, url.toString());
							return scriptSentence;
						} catch (MalformedURLException e) {
							return createLocalScriptSentence(EQUO_CONTRIBUTION_PATH + contributionType + "/" + input);
						}

					}
				};
				Iterable<String> result = Iterables.transform(javascriptFilesNames, function);
				javascriptApis.addAll(Lists.newArrayList(result));
			}
		}
		return javascriptApis;
	}

	private String appendScriptToExistingOnes(String url, String generatedScriptSentence) {
		String existingCustomJsScripts = urlsToScripts.get(url);
		StringBuilder result = new StringBuilder();
		result.append(existingCustomJsScripts);
		result.append("\n");
		result.append(generatedScriptSentence);
		return result.toString();
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
				|| scriptPathLoweredCase.startsWith(EquoHttpProxyServer.BUNDLE_SCRIPT_APP_PROTOCOL)
				|| scriptPathLoweredCase.startsWith(EquoHttpProxyServer.EQUO_CONTRIBUTION_PATH.toLowerCase());
	}

	@Override
	public String getEquoContributionPath() {
		return EQUO_CONTRIBUTION_PATH;
	}

}
