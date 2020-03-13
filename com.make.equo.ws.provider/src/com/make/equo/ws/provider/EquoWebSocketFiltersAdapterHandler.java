package com.make.equo.ws.provider;

import org.littleshoot.proxy.HttpFiltersAdapter;

import com.make.equo.contribution.api.handler.IFiltersAdapterHandler;
import com.make.equo.contribution.api.resolvers.EquoGenericURLResolver;
import com.make.equo.ws.api.IEquoWebSocketService;

import io.netty.handler.codec.http.HttpRequest;

public class EquoWebSocketFiltersAdapterHandler implements IFiltersAdapterHandler {

	private IEquoWebSocketService equoWebSocketService;
	
	EquoWebSocketFiltersAdapterHandler(IEquoWebSocketService equoWebSocketService) {
		this.equoWebSocketService = equoWebSocketService;
	}
	
	@Override
	public HttpFiltersAdapter getFiltersAdapter(HttpRequest request) {
		return new EquoWebsocketJsApiRequestFiltersAdapter(request, new EquoGenericURLResolver(EquoWebSocketContribution.class.getClassLoader()), this.equoWebSocketService.getPort());
	}

}
