package com.make.equo.server.provider;

import java.io.File;

import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public class LocalScriptFileRequestFiltersAdapter extends LocalRequestFiltersAdapter {

	public LocalScriptFileRequestFiltersAdapter(HttpRequest originalRequest) {
		super(originalRequest);
	}

	@Override
	public HttpResponse clientToProxyRequest(HttpObject httpObject) {
		String uri = originalRequest.getUri();
		String localFileUrl = uri.substring(uri.lastIndexOf(EquoHttpProxyServer.LOCAL_FILE_PROTOCOL));
		return buildHttpResponse(localFileUrl);
	}

	@Override
	protected String getMimeType(File localFile) {
		return "application/javascript";
	}

}
