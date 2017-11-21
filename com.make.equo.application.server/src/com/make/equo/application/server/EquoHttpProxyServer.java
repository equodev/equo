package com.make.equo.application.server;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.extras.SelfSignedMitmManager;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpRequest;

public class EquoHttpProxyServer {

	static final String LOCAL_FILE_PROTOCOL = "file:/";

	private List<String> proxiedUrls;
	private Map<String, Object> urlsToScripts;
	
	private String equoAppBundleLocation;
	private String prefix;
	private HttpProxyServer proxyServer;

	public EquoHttpProxyServer(List<String> proxiedUrls, Map<String, Object> urlsToScripts, String equoAppBundleLocation, String prefix) {
		this.proxiedUrls = proxiedUrls;
		this.urlsToScripts = urlsToScripts;
		if (equoAppBundleLocation.endsWith("/")) {
			this.equoAppBundleLocation = equoAppBundleLocation;
		} else {
			this.equoAppBundleLocation = equoAppBundleLocation + "/";
		}
		this.prefix = prefix.toLowerCase();
	}

	public void startProxy() {
		proxyServer = DefaultHttpProxyServer
			.bootstrap()
			.withPort(9896)
			.withManInTheMiddle(new SelfSignedMitmManager())
			.withAllowRequestToOriginServer(true)
			.withTransparent(false)
			.withFiltersSource(new HttpFiltersSourceAdapter() {
				@SuppressWarnings("unchecked")
				public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext clientCtx) {
					if (originalRequest.getUri().contains(LOCAL_FILE_PROTOCOL)) {
						return new LocalScriptFileRequestFiltersAdapter(originalRequest);
					} else {
						Optional<String> url = getRequestedUrl(originalRequest);
						if (url.isPresent()) {
							if (originalRequest.getUri().contains(prefix)) {
								return new LocalFileRequestFiltersAdapter(equoAppBundleLocation, prefix,
										originalRequest);
							} else {
								String appUrl = url.get();
								return new EquoHttpFiltersAdapter(appUrl, originalRequest, (List<String>) urlsToScripts.get(appUrl));
							}
						} else {
							return new HttpFiltersAdapter(originalRequest);
						}
					}
				}

				private Optional<String> getRequestedUrl(HttpRequest originalRequest) {
					return proxiedUrls.stream()
							.filter(url -> url.contains(originalRequest.headers().get(Names.HOST)))
							.findFirst();
				}

				@Override
				public int getMaximumResponseBufferSizeInBytes() {
					return 10 * 1024 * 1024;
				}

				@Override
				public int getMaximumRequestBufferSizeInBytes() {
					return 10 * 1024 * 1024;
				}
			}).start();
	}
	
	public void stop() {
		if (proxyServer != null) {
			proxyServer.stop();
		}
	}
	
}
