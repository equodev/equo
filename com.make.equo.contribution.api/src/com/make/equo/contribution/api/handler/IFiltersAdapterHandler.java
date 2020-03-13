package com.make.equo.contribution.api.handler;

import org.littleshoot.proxy.HttpFiltersAdapter;

import io.netty.handler.codec.http.HttpRequest;

@FunctionalInterface
public interface IFiltersAdapterHandler {

	HttpFiltersAdapter getFiltersAdapter(HttpRequest request);

}
