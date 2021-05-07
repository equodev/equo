package com.equo.server.offline.api.filters;

import io.netty.handler.codec.http.HttpRequest;

@FunctionalInterface
public interface IHttpRequestFilter {
	
	HttpRequest applyFilter(HttpRequest httpRequest);
}
