package com.make.equo.server.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.extras.SelfSignedMitmManager;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import com.make.equo.server.api.IEquoServer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpRequest;

@Component
public class EquoHttpProxyServer implements IEquoServer {

	static final String LOCAL_FILE_PROTOCOL = "file:/";
	private static final String prefix = "equo";

	private List<String> proxiedUrls = new ArrayList<>();
	private Map<String, List<String>> urlsToScripts = new HashMap<String, List<String>>();
	
	private String equoAppBundleLocation;
	private HttpProxyServer proxyServer;

	@Override
	public void startServer() {
		proxyServer = DefaultHttpProxyServer
			.bootstrap()
			.withPort(9896)
			.withManInTheMiddle(new SelfSignedMitmManager())
			.withAllowRequestToOriginServer(true)
			.withTransparent(false)
			.withFiltersSource(new HttpFiltersSourceAdapter() {
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
								return new EquoHttpFiltersAdapter(appUrl, originalRequest, urlsToScripts.get(appUrl));
							}
						} else {
							return new HttpFiltersAdapter(originalRequest);
						}
					}
				}

				private Optional<String> getRequestedUrl(HttpRequest originalRequest) {
					return proxiedUrls.stream()
							.filter(url -> containsHeader(url, originalRequest))
							.findFirst();
				}

				private boolean containsHeader(String url, HttpRequest originalRequest) {
					String host = originalRequest.headers().get(Names.HOST);
					if (host.indexOf(":") != -1) {
						return url.contains(host.substring(0, host.indexOf(":"))); 
					} else {
						return url.contains(originalRequest.headers().get(Names.HOST));
					}
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
	
	@Deactivate
	public void stop() {
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
	public void setAppBundlePath(String appBundlePath) {
		if (appBundlePath.endsWith("/")) {
			this.equoAppBundleLocation = appBundlePath;
		} else {
			this.equoAppBundleLocation = appBundlePath + "/";
		}
	}
	
}
