package com.make.equo.server.provider.filters;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.impl.ProxyUtils;

import com.make.equo.application.api.IEquoApplication;
import com.make.equo.server.contribution.EquoContribution;
import com.make.equo.server.offline.api.IEquoOfflineServer;
import com.make.equo.server.offline.api.filters.OfflineRequestFiltersAdapter;
import com.make.equo.server.offline.api.resolvers.ILocalUrlResolver;
import com.make.equo.server.provider.EquoHttpProxyServer;
import com.make.equo.server.provider.EquoHttpProxyServerURLResolver;
import com.make.equo.server.provider.resolvers.BundleUrlResolver;
import com.make.equo.server.provider.resolvers.MainAppUrlResolver;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpRequest;

public class EquoHttpFiltersSourceAdapter extends HttpFiltersSourceAdapter {

	private static final String limitedConnectionGenericPageFilePath = "/limitedConnectionPage.html";

	private Map<String, EquoContribution> equoContributions;
	private IEquoOfflineServer equoOfflineServer;
	private IEquoApplication equoApplication;

	private boolean connectionLimited = false;
	private boolean isOfflineCacheSupported;

	private String limitedConnectionAppBasedPagePath;
	private List<String> proxiedUrls;

	private List<String> equoContributionsJsApis;
	private Set<String> localScripts;
	private Map<String, String> urlsToScriptsAsStrings;

	public EquoHttpFiltersSourceAdapter(Map<String, EquoContribution> equoContributions,
			IEquoOfflineServer equoOfflineServer, boolean isOfflineCacheSupported,
			String limitedConnectionAppBasedPagePath, List<String> proxiedUrls, List<String> equoContributionsJsApis,
			Set<String> localScripts, Map<String, String> urlsToScriptsAsStrings, IEquoApplication equoApplication) {
		this.equoContributions = equoContributions;
		this.equoOfflineServer = equoOfflineServer;
		this.isOfflineCacheSupported = isOfflineCacheSupported;
		this.limitedConnectionAppBasedPagePath = limitedConnectionAppBasedPagePath;
		this.proxiedUrls = proxiedUrls;
		this.equoContributionsJsApis = equoContributionsJsApis;
		this.localScripts = localScripts;
		this.urlsToScriptsAsStrings = urlsToScriptsAsStrings;
		this.equoApplication = equoApplication;
	}

	@Override
	public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext clientCtx) {
		if (ProxyUtils.isCONNECT(originalRequest)) {
			return new HttpFiltersAdapter(originalRequest, clientCtx);
		}

		try {
			URI uri = URI.create(originalRequest.getUri());
			String key = uri.getScheme() + "://" + uri.getAuthority();
			if (isContributionRequest(key)) {
				EquoContribution contribution = equoContributions.get(key);
				if (contribution.hasCustomFiltersAdapter()) {
					return contribution.getFiltersAdapter(originalRequest);
				} else {
					originalRequest = contribution.getFilter().applyFilter(originalRequest);

					if (isContributionLocalFileRequest(originalRequest)) {
						return new ContributionFileRequestFiltersAdapter(originalRequest, contribution.getUrlResolver(), contribution.getContributionBaseUri());
					}

					return new DefaultContributionRequestFiltersAdapter(originalRequest, contribution.getUrlResolver(),
							equoContributionsJsApis, getCustomScripts(originalRequest.getUri()),
							contribution.getContributedResourceName());
				}
			}
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
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
				return new OfflineRequestFiltersAdapter(originalRequest, new EquoHttpProxyServerURLResolver(),
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

	private boolean isContributionRequest(String key) {
		return equoContributions.containsKey(key);
	}

	private ILocalUrlResolver getUrlResolver(HttpRequest originalRequest) {
		String uri = originalRequest.getUri();
		return getUrlResolver(uri);
	}

	private ILocalUrlResolver getUrlResolver(String uri) {
		if (uri.contains(EquoHttpProxyServer.LOCAL_SCRIPT_APP_PROTOCOL)) {
			return new MainAppUrlResolver(EquoHttpProxyServer.LOCAL_SCRIPT_APP_PROTOCOL, equoApplication);
		}
		if (uri.contains(EquoHttpProxyServer.LOCAL_FILE_APP_PROTOCOL)) {
			return new MainAppUrlResolver(EquoHttpProxyServer.LOCAL_FILE_APP_PROTOCOL, equoApplication);
		}
		if (uri.contains(EquoHttpProxyServer.BUNDLE_SCRIPT_APP_PROTOCOL)) {
			return new BundleUrlResolver(EquoHttpProxyServer.BUNDLE_SCRIPT_APP_PROTOCOL);
		}
		URI realUri = URI.create(uri);
		String key = realUri.getScheme() + "://" + realUri.getAuthority();
		EquoContribution contribution = equoContributions.get(key);
		return contribution != null ? contribution.getUrlResolver() : null;
	}

	private boolean isContributionLocalFileRequest(HttpRequest originalRequest) {
		String uri = originalRequest.getUri();
		for (String script : localScripts) {
			if (uri.contains(script)) {
				return true;
			}
		}
		return false;
	}

	private boolean isLocalFileRequest(HttpRequest originalRequest) {
		String uri = originalRequest.getUri();
		return uri.contains(EquoHttpProxyServer.LOCAL_SCRIPT_APP_PROTOCOL)
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
		String key = (uri.getScheme() + "://" + uri.getAuthority() + uri.getPath()).toLowerCase();
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
