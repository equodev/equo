package com.make.equo.server.offline.api;

import io.netty.handler.codec.http.HttpRequest;

@FunctionalInterface
public interface IHttpRequestFilter {
	
	HttpRequest applyFilter(HttpRequest httpRequest);
}
