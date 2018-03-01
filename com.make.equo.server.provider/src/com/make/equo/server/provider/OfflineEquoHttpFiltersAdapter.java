package com.make.equo.server.provider;

import java.io.IOException;

import org.littleshoot.proxy.HttpFiltersAdapter;

import com.make.equo.server.offline.api.IEquoOfflineServer;

import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public class OfflineEquoHttpFiltersAdapter extends HttpFiltersAdapter {

	private boolean isOfflineTrafficSupported;
	private IEquoOfflineServer equoOfflineServer;

	public OfflineEquoHttpFiltersAdapter(HttpRequest originalRequest, boolean isOfflineTrafficSupported,
			IEquoOfflineServer equoOfflineServer) {
		super(originalRequest);
		this.isOfflineTrafficSupported = isOfflineTrafficSupported;
		this.equoOfflineServer = equoOfflineServer;
	}

	@Override
	public HttpResponse clientToProxyRequest(HttpObject httpObject) {
		if (isOfflineTrafficSupported) {
			String newUri = originalRequest.getUri().replaceFirst(":443$", "");
			newUri = newUri.replace("https://", "http");
			originalRequest.setUri(newUri);
			try {
				HttpResponse offlineResponse = equoOfflineServer.getOfflineResponse(originalRequest);
				return offlineResponse;
			} catch (IOException e) {
				// TODO log the exception, the not found offline request/file
			}
		} else {
			// TODO return offline page (equo or app based offline page)
		}
		return (HttpResponse) httpObject;
	}

}
