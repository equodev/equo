package com.make.equo.server.contribution;

import org.littleshoot.proxy.HttpFiltersAdapter;

import io.netty.handler.codec.http.HttpRequest;

@FunctionalInterface
public interface IFiltersAdapterHandler {

	HttpFiltersAdapter getFiltersAdapter(HttpRequest request);
	
}
