package com.make.equo.server.provider;

import org.littleshoot.proxy.HttpFiltersAdapter;

import com.make.equo.server.offline.api.IEquoOfflineServer;

import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;

public class EquoHttpFiltersAdapter extends HttpFiltersAdapter {

	private boolean isOfflineTraficSupported;
	private IEquoOfflineServer equoOfflineServer;

	public EquoHttpFiltersAdapter(HttpRequest originalRequest, IEquoOfflineServer equoOfflineServer,
			boolean isOfflineTraficSupported) {
		super(originalRequest);
		this.equoOfflineServer = equoOfflineServer;
		this.isOfflineTraficSupported = isOfflineTraficSupported;
	}

	@Override
	public HttpObject serverToProxyResponse(HttpObject httpObject) {
		saveRequestResponseIfPossible(originalRequest, httpObject);
		return super.serverToProxyResponse(httpObject);
	}

	private boolean isOfflineTrafficSupported() {
		return isOfflineTraficSupported;
	}

	public IEquoOfflineServer getEquoOfflineServer() {
		return equoOfflineServer;
	}

	public void saveRequestResponseIfPossible(HttpRequest originalRequest, HttpObject httpObject) {
		if (isOfflineTrafficSupported()) {
			getEquoOfflineServer().saveRequestResponse(originalRequest, httpObject);
		}
	}
}
