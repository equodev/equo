package com.make.equo.ws.provider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.make.equo.server.contribution.EquoContribution;
import com.make.equo.server.contribution.EquoContributionBuilder;
import com.make.equo.ws.api.IEquoWebSocketService;

@Component
public class EquoWebSocketContribution {

	private static final String equoWebsocketsJsApi = "equoWebsockets.js";
	
	private EquoContributionBuilder builder;
	
	private EquoContribution contribution;
	private IEquoWebSocketService equoWebSocketService;

	@Activate
	protected void activate() {
		contribution = builder.withScriptFile(equoWebsocketsJsApi)
				.build();
		contribution.setFiltersAdapterHandler(new EquoWebSocketFiltersAdapterHandler(equoWebSocketService, contribution));
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

	void unsetEquoServer(EquoContributionBuilder builder) {
		this.builder = null;
	}

}
