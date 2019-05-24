package com.make.equo.ws.provider;

import org.littleshoot.proxy.HttpFiltersAdapter;

import com.make.equo.server.contribution.EquoContribution;
import com.make.equo.server.contribution.IFiltersAdapterHandler;
import com.make.equo.server.provider.filters.EquoWebsocketJsApiRequestFiltersAdapter;
import com.make.equo.ws.api.IEquoWebSocketService;

import io.netty.handler.codec.http.HttpRequest;

public class EquoWebSocketFiltersAdapterHandler implements IFiltersAdapterHandler {

	private IEquoWebSocketService equoWebSocketService;
	private EquoContribution contribution;
	
	EquoWebSocketFiltersAdapterHandler(IEquoWebSocketService equoWebSocketService, EquoContribution contribution) {
		this.equoWebSocketService = equoWebSocketService;
		this.contribution = contribution;
	}
	
	@Override
	public HttpFiltersAdapter getFiltersAdapter(HttpRequest request) {
		return new EquoWebsocketJsApiRequestFiltersAdapter(request, new EquoWebSocketURLResolver(contribution), this.equoWebSocketService.getPort());
	}

}
