package com.equo.ws.provider;

import org.littleshoot.proxy.HttpFiltersAdapter;

import com.equo.contribution.api.handler.IFiltersAdapterHandler;
import com.equo.contribution.api.resolvers.EquoGenericUrlResolver;
import com.equo.ws.api.IEquoWebSocketService;

import io.netty.handler.codec.http.HttpRequest;

/**
 * Adapter handler for websockets contributions.
 */
public class EquoWebSocketFiltersAdapterHandler implements IFiltersAdapterHandler {

  private IEquoWebSocketService equoWebSocketService;

  EquoWebSocketFiltersAdapterHandler(IEquoWebSocketService equoWebSocketService) {
    this.equoWebSocketService = equoWebSocketService;
  }

  @Override
  public HttpFiltersAdapter getFiltersAdapter(HttpRequest request) {
    return new EquoWebsocketJsApiRequestFiltersAdapter(request,
        new EquoGenericUrlResolver(EquoWebSocketContribution.class.getClassLoader()),
        this.equoWebSocketService.getPort());
  }

}
