package com.equo.contribution.api.handler;

import org.littleshoot.proxy.HttpFiltersAdapter;

import io.netty.handler.codec.http.HttpRequest;

/**
 * Interface for adapter handlers.
 */
@FunctionalInterface
public interface IFiltersAdapterHandler {

  HttpFiltersAdapter getFiltersAdapter(HttpRequest request);

}
