package com.equo.contribution.api.resolvers;

import java.net.URI;
import java.net.URL;

import io.netty.handler.codec.http.HttpRequest;

public interface IEquoContributionUrlResolver {

	URL resolve(String pathToResolve);
	
	default boolean accepts(HttpRequest request, URI requestUri) {
		if (requestUri.getPath() != null && requestUri.getPath().matches(".*\\..+$")) {
			return true;
		}
		return false;
	}

}
