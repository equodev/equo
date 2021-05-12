package com.equo.server.offline.api.filters;

import io.netty.handler.codec.http.HttpRequest;
/**
 * Provides the call to apply the custom filter request.
 *
 */
@FunctionalInterface
public interface IHttpRequestFilter {
	
	HttpRequest applyFilter(HttpRequest httpRequest);
}
