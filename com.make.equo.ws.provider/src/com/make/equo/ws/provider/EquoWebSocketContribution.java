package com.make.equo.ws.provider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.make.equo.server.api.IEquoServer;
import com.make.equo.server.contribution.EquoContribution;
import com.make.equo.server.contribution.EquoContributionBuilder;
import com.make.equo.ws.api.IEquoWebSocketService;

@Component
public class EquoWebSocketContribution {

	private static final String equoWebsocketsJsApi = "equoWebsockets.js";
	private EquoContribution contribution;
	
	private IEquoWebSocketService equoWebSocketService;
	private IEquoServer server;

	@Activate
	protected void activate() {
		contribution = EquoContributionBuilder.createContribution()
				.withScriptFile(equoWebsocketsJsApi)
				.withServer(server)
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
	void setEquoServer(IEquoServer server) {
		this.server = server;
	}

	void unsetEquoServer(IEquoServer server) {
		this.server = null;
	}

}
