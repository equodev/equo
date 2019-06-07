package com.make.equo.ws.provider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.make.equo.server.contribution.EquoContribution;
import com.make.equo.server.contribution.EquoContributionBuilder;
import com.make.equo.ws.api.IEquoWebSocketService;

@Component
public class EquoWebSocketContribution {

	static final String WEBSOCKET_BASE_URI = "http://equowebsocket/";
	private static final String EQUO_WEBSOCKET_JS_API = "equoWebsockets.js";
	
	private EquoContributionBuilder builder;
	
	private EquoContribution contribution;
	private IEquoWebSocketService equoWebSocketService;

	@Activate
	protected void activate() {
		contribution = builder.withScriptFile(EQUO_WEBSOCKET_JS_API)
				.withContributionBaseUri(WEBSOCKET_BASE_URI)
				.withFiltersAdapterHandler(new EquoWebSocketFiltersAdapterHandler(equoWebSocketService))
				.build();
		contribution.startContributing();
	}
	
	@Reference
	void setEquoWebSocketService(IEquoWebSocketService equoWebSocketService) {
		this.equoWebSocketService = equoWebSocketService;
	}

	void unsetEquoWebSocketService(IEquoWebSocketService equoWebSocketService) {
		this.equoWebSocketService = null;
	}

	@Reference
	void setEquoBuilder(EquoContributionBuilder builder) {
		this.builder = builder;
	}

	void unsetEquoBuilder(EquoContributionBuilder builder) {
		this.builder = null;
	}

}
