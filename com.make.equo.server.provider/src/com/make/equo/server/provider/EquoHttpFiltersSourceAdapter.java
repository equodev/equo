package com.make.equo.server.provider;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.impl.ProxyUtils;

import com.make.equo.application.api.IEquoApplication;
import com.make.equo.contribution.api.IEquoContribution;
import com.make.equo.server.offline.api.IEquoOfflineServer;
import com.make.equo.server.offline.api.filters.OfflineRequestFiltersAdapter;
import com.make.equo.server.offline.api.resolvers.ILocalUrlResolver;
import com.make.equo.server.provider.resolvers.BundleUrlResolver;
import com.make.equo.server.provider.resolvers.EquoContributionUrlResolver;
import com.make.equo.server.provider.resolvers.MainAppUrlResolver;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpRequest;

public class EquoHttpFiltersSourceAdapter extends HttpFiltersSourceAdapter {

	private static final String limitedConnectionGenericPageFilePath = EquoHttpProxyServer.EQUO_CONTRIBUTION_PATH
			+ "limitedConnectionPage.html";

	private static final String RENDERERS_CONTRIBUTION_TYPE = "renderersContribution";

	private static final String baseRendererPath = EquoHttpProxyServer.EQUO_CONTRIBUTION_PATH
			+ RENDERERS_CONTRIBUTION_TYPE + "/baseRenderer.html";

	private static final String EQUO_RENDERERS_SUBFIX = "equo_renderers";

	private Map<String, IEquoContribution> equoContributions;
	private IEquoOfflineServer equoOfflineServer;
	private IEquoApplication equoApplication;

	private boolean connectionLimited = false;
	private boolean isOfflineCacheSupported;

	private String limitedConnectionAppBasedPagePath;
	private List<String> proxiedUrls;

	private List<String> equoContributionsJsApis;
	private Map<String, String> urlsToScriptsAsStrings;

	private int websocketPort;

	public EquoHttpFiltersSourceAdapter(Map<String, IEquoContribution> equoContributions,
			IEquoOfflineServer equoOfflineServer, boolean isOfflineCacheSupported,
			String limitedConnectionAppBasedPagePath, List<String> proxiedUrls, List<String> equoContributionsJsApis,
			Map<String, String> urlsToScriptsAsStrings, int websocketPort, IEquoApplication equoApplication) {
		this.equoContributions = equoContributions;
		this.equoOfflineServer = equoOfflineServer;
		this.isOfflineCacheSupported = isOfflineCacheSupported;
		this.limitedConnectionAppBasedPagePath = limitedConnectionAppBasedPagePath;
		this.proxiedUrls = proxiedUrls;
		this.equoContributionsJsApis = equoContributionsJsApis;
		this.urlsToScriptsAsStrings = urlsToScriptsAsStrings;
		this.websocketPort = websocketPort;
		this.equoApplication = equoApplication;
	}

	@Override
	public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext clientCtx) {
		if (ProxyUtils.isCONNECT(originalRequest)) {
			return new HttpFiltersAdapter(originalRequest, clientCtx);
		}
		// It's necessary to distinguish between the websocket contribution and the
		// other Equo contributions in order to parse and generate the port dinamically
		if (isEquoWebsocketJsApi(originalRequest)) {
			return new EquoWebsocketJsApiRequestFiltersAdapter(originalRequest,
					new EquoContributionUrlResolver(EquoHttpProxyServer.EQUO_CONTRIBUTION_PATH, equoContributions),
					websocketPort);
		}

		if (isEquoRendererRequest(originalRequest)) {
			return new RenderersRequestFiltersAdapter(originalRequest,
					new EquoContributionUrlResolver(EquoHttpProxyServer.EQUO_CONTRIBUTION_PATH, equoContributions),
					equoContributionsJsApis, getCustomScripts(originalRequest.getUri()), baseRendererPath);
		}

		if (isLocalFileRequest(originalRequest)) {
			return new LocalFileRequestFiltersAdapter(originalRequest, getUrlResolver(originalRequest));
		}

		if (isConnectionLimited()) {
			if (isOfflineCacheSupported) {
				return equoOfflineServer.getOfflineHttpFiltersAdapter(originalRequest);
			} else if (limitedConnectionAppBasedPagePath != null) {
				return new OfflineRequestFiltersAdapter(originalRequest,
						getUrlResolver(limitedConnectionAppBasedPagePath), limitedConnectionAppBasedPagePath);
			} else {
				return new OfflineRequestFiltersAdapter(originalRequest,
						new EquoContributionUrlResolver(EquoHttpProxyServer.EQUO_CONTRIBUTION_PATH, equoContributions),
						limitedConnectionGenericPageFilePath);
			}
		} else {
			Optional<String> url = getRequestedUrl(originalRequest);
			if (url.isPresent() && !isFilteredOutFromProxy(originalRequest)) {
				String appUrl = url.get();
				return new EquoHttpModifierFiltersAdapter(originalRequest, equoContributionsJsApis,
						getCustomScripts(appUrl), isOfflineCacheSupported, equoOfflineServer);
			} else {
				return new EquoHttpFiltersAdapter(originalRequest, equoOfflineServer, isOfflineCacheSupported);
			}
		}
	}

	private boolean isFilteredOutFromProxy(HttpRequest originalRequest) {
		String uri = originalRequest.getUri();
		return uri.contains("/shared") || uri.contains("/static");
	}

	private boolean isEquoRendererRequest(HttpRequest originalRequest) {
		String uri = originalRequest.getUri();
		String renderersUri = EQUO_RENDERERS_SUBFIX + "/" + EquoHttpProxyServer.EQUO_CONTRIBUTION_PATH
				+ RENDERERS_CONTRIBUTION_TYPE;
		return uri.contains(renderersUri);
	}

	private boolean isEquoWebsocketJsApi(HttpRequest originalRequest) {
		String uri = originalRequest.getUri();
		return uri.contains(EquoHttpProxyServer.WEBSOCKET_CONTRIBUTION_TYPE);
	}

	private ILocalUrlResolver getUrlResolver(HttpRequest originalRequest) {
		String uri = originalRequest.getUri();
		return getUrlResolver(uri);
	}

	private ILocalUrlResolver getUrlResolver(String uri) {
		if (uri.contains(EquoHttpProxyServer.EQUO_CONTRIBUTION_PATH)) {
			return new EquoContributionUrlResolver(EquoHttpProxyServer.EQUO_CONTRIBUTION_PATH, equoContributions);
		}
		if (uri.contains(EquoHttpProxyServer.LOCAL_SCRIPT_APP_PROTOCOL)) {
			return new MainAppUrlResolver(EquoHttpProxyServer.LOCAL_SCRIPT_APP_PROTOCOL, equoApplication);
		}
		if (uri.contains(EquoHttpProxyServer.LOCAL_FILE_APP_PROTOCOL)) {
			return new MainAppUrlResolver(EquoHttpProxyServer.LOCAL_FILE_APP_PROTOCOL, equoApplication);
		}
		if (uri.contains(EquoHttpProxyServer.BUNDLE_SCRIPT_APP_PROTOCOL)) {
			return new BundleUrlResolver(EquoHttpProxyServer.BUNDLE_SCRIPT_APP_PROTOCOL);
		}
		return null;
	}

	private boolean isLocalFileRequest(HttpRequest originalRequest) {
		String uri = originalRequest.getUri();
		return uri.contains(EquoHttpProxyServer.EQUO_CONTRIBUTION_PATH)
				|| uri.contains(EquoHttpProxyServer.LOCAL_SCRIPT_APP_PROTOCOL)
				|| uri.contains(EquoHttpProxyServer.LOCAL_FILE_APP_PROTOCOL)
				|| uri.contains(EquoHttpProxyServer.BUNDLE_SCRIPT_APP_PROTOCOL);
	}

	private Optional<String> getRequestedUrl(HttpRequest originalRequest) {
		return proxiedUrls.stream().filter(url -> containsHeader(url, originalRequest)).findFirst();
	}

	private boolean containsHeader(String url, HttpRequest originalRequest) {
		String host = originalRequest.headers().get(Names.HOST);
		if (host.indexOf(":") != -1) {
			return url.contains(host.substring(0, host.indexOf(":")));
		} else {
			return url.contains(host);
		}
	}

	private String getCustomScripts(String url) {
		if (urlsToScriptsAsStrings.containsKey(url)) {
			return urlsToScriptsAsStrings.get(url);
		}
		if (urlsToScriptsAsStrings.containsKey(url + "/")) {
			return urlsToScriptsAsStrings.get(url + "/");
		}
		URI uri = URI.create(url);
		String key = uri.getScheme() + "://" + uri.getAuthority();
		if (!urlsToScriptsAsStrings.containsKey(key)) {
			return "";
		}
		return urlsToScriptsAsStrings.get(key);
	}

	@Override
	public int getMaximumResponseBufferSizeInBytes() {
		return 10 * 1024 * 1024;
	}

	@Override
	public int getMaximumRequestBufferSizeInBytes() {
		return 10 * 1024 * 1024;
	}

	private boolean isConnectionLimited() {
		return connectionLimited;
	}

	public void setConnectionLimited() {
		connectionLimited = true;
	}

	public void setConnectionUnlimited() {
		connectionLimited = false;
	}

}
