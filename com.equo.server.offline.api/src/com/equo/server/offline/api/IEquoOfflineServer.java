package com.equo.server.offline.api;

import java.io.IOException;
import java.util.List;
import org.littleshoot.proxy.HttpFiltersAdapter;

import com.equo.server.offline.api.filters.IHttpRequestFilter;

import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public interface IEquoOfflineServer {

	void saveRequestResponse(HttpRequest originalRequest, HttpObject httpObject);

	HttpResponse getOfflineResponse(HttpRequest originalRequest) throws IOException;

	void addHttpRequestFilter(IHttpRequestFilter httpRequestFilter);

	void setProxiedUrls(List<String> urls);

	HttpFiltersAdapter getOfflineHttpFiltersAdapter(HttpRequest originalRequest);

}
