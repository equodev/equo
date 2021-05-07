package com.equo.ws.provider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.equo.contribution.api.EquoContributionBuilder;
import com.equo.ws.api.IEquoWebSocketService;

@Component
public class EquoWebSocketContribution {

	static final String WEBSOCKET_CONTRIBUTION_NAME = "equowebsocket";
	private static final String EQUO_WEBSOCKET_JS_API = "equoWebsockets.js";
	
	private EquoContributionBuilder builder;
	
	private IEquoWebSocketService equoWebSocketService;

	@Activate
	protected void activate() {
		builder
			.withScriptFile(EQUO_WEBSOCKET_JS_API)
			.withContributionName(WEBSOCKET_CONTRIBUTION_NAME)
			.withFiltersAdapterHandler(new EquoWebSocketFiltersAdapterHandler(equoWebSocketService))
			.build();
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
