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
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.extras.SelfSignedMitmManager;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ServiceScope;

import com.make.equo.aer.api.IEquoLoggingService;
import com.make.equo.application.api.IEquoApplication;
import com.make.equo.server.api.IEquoServer;
import com.make.equo.server.contribution.EquoContribution;
import com.make.equo.server.offline.api.IEquoOfflineServer;
import com.make.equo.server.offline.api.filters.IHttpRequestFilter;
import com.make.equo.server.provider.filters.EquoHttpFiltersSourceAdapter;

@Component(scope = ServiceScope.SINGLETON)
public class EquoHttpProxyServer implements IEquoServer {

	private static final String URL_PATH = "urlPath";
	private static final String PATH_TO_STRING_REG = "PATHTOSTRING";
	private static final String URL_SCRIPT_SENTENCE = "<script src=\"urlPath\"></script>";
	private static final String LOCAL_SCRIPT_SENTENCE = "<script src=\"PATHTOSTRING\"></script>";
	private static final String URL_CSS_SENTENCE = "<link type=\"text/css\" rel=\"stylesheet\" href=\"urlPath\">";
	private static final String LOCAL_CSS_SENTENCE = "<link type=\"text/css\" rel=\"stylesheet\" href=\"PATHTOSTRING\">";

	private static final List<String> proxiedUrls = new ArrayList<String>();
	private static final Map<String, String> urlsToScripts = new HashMap<String, String>();
	private static final Map<String, String> urlsToStyles = new HashMap<String, String>();

	private static boolean enableOfflineCache = false;
	private static String limitedConnectionAppBasedPagePath;

	private static volatile HttpProxyServer proxyServer;
//	private ScheduledExecutorService internetConnectionChecker;

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
	private volatile IEquoApplication equoApplication;

	@Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.STATIC)
	private volatile IEquoOfflineServer equoOfflineServer;

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
	private volatile IEquoLoggingService equoLoggingService;

	private Map<String, EquoContribution> equoContributions = new HashMap<String, EquoContribution>();

	private List<String> contributionJsApis = new ArrayList<String>();
	private List<String> contributionStyles = new ArrayList<String>();

	@Override
	@Activate
	public void start() {
		if (proxyServer == null) {
			startServer();
		}
	}

	@Override
	public void startServer() {
		EquoHttpFiltersSourceAdapter httpFiltersSourceAdapter = new EquoHttpFiltersSourceAdapter(equoContributions,
				equoOfflineServer, isOfflineCacheSupported(), limitedConnectionAppBasedPagePath, proxiedUrls,
				contributionJsApis, contributionStyles, urlsToScripts, urlsToStyles);

//		Runnable internetConnectionRunnable = new Runnable() {
//			@Override
//			public void run() {
//				if (!isInternetReachable()) {
//					httpFiltersSourceAdapter.setConnectionLimited();
//				} else {
//					httpFiltersSourceAdapter.setConnectionUnlimited();
//				}
//			}
//		};
//		internetConnectionChecker = Executors.newSingleThreadScheduledExecutor();
//		internetConnectionChecker.scheduleAtFixedRate(internetConnectionRunnable, 0, 5, TimeUnit.SECONDS);

		proxyServer = DefaultHttpProxyServer.bootstrap().withPort(9896).withManInTheMiddle(new SelfSignedMitmManager())
				.withAllowRequestToOriginServer(true).withTransparent(false).withFiltersSource(httpFiltersSourceAdapter)
				.start();
	}

	@Deactivate
	public void stop() {
		System.out.println("Stopping proxy...");
//		if (internetConnectionChecker != null) {
//			internetConnectionChecker.shutdownNow();
//		}
//		if (proxyServer != null) {
//			proxyServer.stop();
//		}
	}

	@Override
	public void addCustomScript(String url, String scriptUrl) {
		String lowerCaseURL = url.toLowerCase();
		handleResourceAdd(lowerCaseURL, scriptUrl, URL_SCRIPT_SENTENCE, LOCAL_SCRIPT_SENTENCE, urlsToScripts);
	}

	public void addCustomStyle(String url, String styleUrl) {
		String lowerCaseURL = url.toLowerCase();
		handleResourceAdd(lowerCaseURL, styleUrl, URL_CSS_SENTENCE, LOCAL_CSS_SENTENCE, urlsToStyles);
	}

	@Override
	public void addUrl(String url) {
		if (!proxiedUrls.contains(url)) {
			proxiedUrls.add(url);
		} else {
			equoLoggingService.logWarning("The url " + url + " was already added to the Proxy server.");
		}
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

//	private boolean isInternetReachable() {
//		if (proxiedUrls.isEmpty()) {
//			return false;
//		}
//		return isAddressReachable(proxiedUrls.get(0));
//	}

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

	private void addEquoContributionJsAndCss(EquoContribution contribution) {
		List<String> javascriptFilesNames = contribution.getContributedScripts();
		if (!javascriptFilesNames.isEmpty()) {
			List<String> result = javascriptFilesNames.stream().map(input -> {
				try {
					URL url = new URL(input);
					String scriptSentence = URL_SCRIPT_SENTENCE.replaceAll(URL_PATH, url.toString());
					return scriptSentence;
				} catch (MalformedURLException e) {
					return createLocalSentence(contribution.getContributionName() + "/" + input, LOCAL_SCRIPT_SENTENCE);
				}
			}).collect(Collectors.toList());
			contributionJsApis.addAll(new ArrayList<String>(result));
		}
		List<String> cssFilesNames = contribution.getContributedStyles();
		if (!cssFilesNames.isEmpty()) {
			List<String> result = cssFilesNames.stream().map(input -> {
				try {
					URL url = new URL(input);
					String styleSentence = URL_CSS_SENTENCE.replaceAll(URL_PATH, url.toString());
					return styleSentence;
				} catch (MalformedURLException e) {
					return createLocalSentence(contribution.getContributionName() + "/" + input, LOCAL_CSS_SENTENCE);
				}
			}).collect(Collectors.toList());
			contributionStyles.addAll(new ArrayList<String>(result));
		}
	}

	private void addEquoContributionProxiedUris(EquoContribution contribution) {
		for (String url : contribution.getProxiedUris()) {
			addUrl(url);
		}
	}

	private void addEquoContributionCustomScripts(EquoContribution contribution) {
		for (Entry<String, String> entry : contribution.getPathsToScripts().entrySet()) {
			addUrl(contribution.getContributionName() + "/" + entry.getKey());
			addCustomScript(contribution.getContributionName() + "/" + entry.getKey(), entry.getValue());
		}
	}

	private void addEquoContributionCustomStyles(EquoContribution contribution) {
		for (Entry<String, String> entry : contribution.getPathsToStyles().entrySet()) {
			addUrl(contribution.getContributionName() + "/" + entry.getKey());
			addCustomStyle(entry.getKey(), entry.getValue());
		}
	}

	private void handleResourceAdd(String url, String resourceUrl, String baseSentence, String localSentence,
			Map<String, String> existingResourceMap) {
		String generatedResourceSentence = generateSentence(resourceUrl, baseSentence, localSentence);
		if (!existingResourceMap.containsKey(url.toLowerCase())) {
			existingResourceMap.put(url, generatedResourceSentence);
		} else {
			String existingResources = existingResourceMap.get(url);
			if (!existingResources.contains(generatedResourceSentence)) {
				existingResourceMap.put(url, appendToExistingOnes(url, generatedResourceSentence, existingResourceMap));
			}
		}
	}

	private String appendToExistingOnes(String url, String generatedSentence, Map<String, String> existingResources) {
		String existingResource = existingResources.get(url);
		StringBuilder result = new StringBuilder();
		result.append(existingResource);
		result.append("\n");
		result.append(generatedSentence);
		return result.toString();
	}

	private String generateSentence(String scriptPath, String baseSentence, String localSentence) {
		try {
			URL url = new URL(scriptPath);
			String scriptSentence = baseSentence.replaceAll(URL_PATH, url.toString());
			return scriptSentence;
		} catch (MalformedURLException e) {
			return createLocalSentence(scriptPath, localSentence);
		}

	}

	private String createLocalSentence(String scriptPath, String localSentence) {
		String scriptSentence = localSentence.replaceAll(PATH_TO_STRING_REG, scriptPath);
		return scriptSentence;
	}

	@Override
	public void addContribution(EquoContribution contribution) {
		equoContributions.put(contribution.getContributionName().toLowerCase(), contribution);
		addUrl(contribution.getContributionName());
		addEquoContributionJsAndCss(contribution);
		addEquoContributionProxiedUris(contribution);
		addEquoContributionCustomScripts(contribution);
		addEquoContributionCustomStyles(contribution);
		System.out.println("Equo Contribution added: " + contribution.getContributionName());
	}

	@Override
	public void addStyleToContribution(String style) {
		String processedStyle = generateSentence(style, URL_CSS_SENTENCE, LOCAL_CSS_SENTENCE);
		contributionStyles.add(processedStyle);
	}

	@Override
	public void addScriptToContribution(String script) {
		String processedScript = generateSentence(script, URL_SCRIPT_SENTENCE, LOCAL_SCRIPT_SENTENCE);
		contributionJsApis.add(processedScript);
	}

}
