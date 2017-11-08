package com.make.equo.application.server;

import java.util.ArrayList;
import java.util.List;

import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.extras.SelfSignedMitmManager;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

public class EquoHttpProxyServer {

	static final String LOCAL_FILE_PROTOCOL = "file:/";
	private List<String> jsScripts;

	private String appUrl;

	public EquoHttpProxyServer(String appUrl) {
		this.appUrl = appUrl;
		this.jsScripts = new ArrayList<>();
	}

	public void startProxy() {
		DefaultHttpProxyServer
			.bootstrap()
			.withPort(9896)
			.withManInTheMiddle(new SelfSignedMitmManager()).withAllowRequestToOriginServer(true)
			.withTransparent(false).withFiltersSource(new HttpFiltersSourceAdapter() {
				public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext clientCtx) {
					if (originalRequest.getUri().contains(LOCAL_FILE_PROTOCOL)) {
						return new LocalRequestFiltersAdapater(originalRequest);
					} else {
						return new EquoHttpFiltersAdapter(appUrl, originalRequest, jsScripts);
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

	public void addScripts(List<String> customScripts) {
		jsScripts.addAll(customScripts);
	}

}
