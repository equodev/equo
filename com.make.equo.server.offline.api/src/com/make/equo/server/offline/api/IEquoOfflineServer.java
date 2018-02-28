package com.make.equo.server.offline.api;

import java.io.IOException;
import java.util.List;

import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public interface IEquoOfflineServer {

	void saveRequestResponse(HttpRequest originalRequest, HttpObject httpObject);

	HttpResponse getOfflineResponse(HttpRequest originalRequest) throws IOException;

	void addHttpRequestFilter(IHttpRequestFilter httpRequestFilter);

	void setProxiedUrls(List<String> urls);

}
