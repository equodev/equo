package com.equo.server.offline.provider;

import java.io.IOException;

import org.littleshoot.proxy.HttpFiltersAdapter;

import com.equo.server.offline.api.IEquoOfflineServer;

import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public class OfflineEquoHttpFiltersAdapter extends HttpFiltersAdapter {

	private IEquoOfflineServer equoOfflineServer;

	public OfflineEquoHttpFiltersAdapter(HttpRequest originalRequest, IEquoOfflineServer equoOfflineServer) {
		super(originalRequest);
		this.equoOfflineServer = equoOfflineServer;
	}

	@Override
	public HttpResponse clientToProxyRequest(HttpObject httpObject) {
		try {
			HttpResponse offlineResponse = equoOfflineServer.getOfflineResponse(originalRequest);
			return offlineResponse;
		} catch (IOException e) {
			e.printStackTrace();
			// TODO log the exception, the not found offline request/file
		}
		return (HttpResponse) httpObject;
	}

}
