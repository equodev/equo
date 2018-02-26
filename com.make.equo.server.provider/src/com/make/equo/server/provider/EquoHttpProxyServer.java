package com.make.equo.server.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.extras.SelfSignedMitmManager;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.osgi.framework.Bundle;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import com.make.equo.server.api.IEquoServer;
import com.make.equo.ws.api.IEquoWebSocketService;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpRequest;

@Component
public class EquoHttpProxyServer implements IEquoServer {

	public static final String LOCAL_SCRIPT_PROTOCOL = "main_app_equo_script/";
	public static final String BUNDLE_SCRIPT_PROTOCOL = "external_bundle_equo_script/";
	public static final String LOCAL_FILE_PROTOCOL = "equo/";
	public static final String EQUO_FRAMEWORK_PATH = "equoFramework/";
	public static final String EQUO_WEBSOCKETS_JS_PATH = "equoWebsockets/";

	private List<String> proxiedUrls = new ArrayList<>();
	private Map<String, List<String>> urlsToScripts = new HashMap<String, List<String>>();
	
	private HttpProxyServer proxyServer;
	private Bundle mainEquoAppBundle;

	@Inject
	private IEquoWebSocketService equoWebsocketServer;

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
					if (isEquoWebsocketJsApi(originalRequest)) {
						return new EquoWebsocketJsApiRequestFiltersAdapter(originalRequest, new EquoWebsocketsUrlResolver(EQUO_WEBSOCKETS_JS_PATH, equoWebsocketServer), equoWebsocketServer.getPort());
					}
					if (isLocalFileRequest(originalRequest)) {
						return new LocalFileRequestFiltersAdapter(originalRequest, getUrlResolver(originalRequest));
					} else {
						Optional<String> url = getRequestedUrl(originalRequest);
						if (url.isPresent()) {
							String appUrl = url.get();
							return new EquoHttpFiltersAdapter(appUrl, originalRequest, getCustomScripts(appUrl), equoWebsocketServer);
						} else {
							return new HttpFiltersAdapter(originalRequest);
						}
					}
				}

				private boolean isEquoWebsocketJsApi(HttpRequest originalRequest) {
					String uri = originalRequest.getUri();
					return uri.contains(EQUO_WEBSOCKETS_JS_PATH);
				}

				private ILocalUrlResolver getUrlResolver(HttpRequest originalRequest) {
					String uri = originalRequest.getUri();
					if (uri.contains(LOCAL_SCRIPT_PROTOCOL)) {
						return new MainAppUrlResolver(LOCAL_SCRIPT_PROTOCOL, mainEquoAppBundle);
					}
					if (uri.contains(LOCAL_FILE_PROTOCOL)) {
						return new MainAppUrlResolver(LOCAL_FILE_PROTOCOL, mainEquoAppBundle);
					}
					if (uri.contains(BUNDLE_SCRIPT_PROTOCOL)) {
						return new BundleUrlResolver(BUNDLE_SCRIPT_PROTOCOL);
					}
					if (uri.contains(EQUO_FRAMEWORK_PATH)) {
						return new EquoFrameworkUrlResolver(EQUO_FRAMEWORK_PATH);
					}
					return null;
				}

				private boolean isLocalFileRequest(HttpRequest originalRequest) {
					String uri = originalRequest.getUri();
					return uri.contains(LOCAL_SCRIPT_PROTOCOL) || uri.contains(LOCAL_FILE_PROTOCOL) || uri.contains(BUNDLE_SCRIPT_PROTOCOL) || uri.contains(EQUO_FRAMEWORK_PATH);
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
	
	private List<String> getCustomScripts(String url) {
		if (!urlsToScripts.containsKey(url)) {
			return Collections.emptyList();
		}
		return urlsToScripts.get(url);
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
	public void setMainAppBundle(Bundle mainEquoAppBundle) {
		this.mainEquoAppBundle = mainEquoAppBundle;
	}

	@Override
	public String getLocalScriptProtocol() {
		return LOCAL_SCRIPT_PROTOCOL;
	}

	@Override
	public String getBundleScriptProtocol() {
		return BUNDLE_SCRIPT_PROTOCOL;
	}

}
