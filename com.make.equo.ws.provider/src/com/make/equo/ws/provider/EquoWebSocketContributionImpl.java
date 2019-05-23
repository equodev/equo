package com.make.equo.ws.provider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

import com.make.equo.server.api.IEquoServer;
import com.make.equo.server.contribution.EquoContribution;
import com.make.equo.server.contribution.EquoContributionBuilder;
import com.make.equo.server.provider.filters.EquoWebsocketJsApiRequestFiltersAdapter;
import com.make.equo.ws.api.IEquoWebSocketService;

@Component
public class EquoWebSocketContributionImpl {

	private static final String equoWebsocketsJsApi = "equoWebsockets.js";
	private IEquoWebSocketService equoWebSocketService;
	private IEquoServer server;

	@Activate
	@Modified
	protected void activate() {
		EquoContribution contribution = EquoContributionBuilder.createContribution()
				.withScriptFile(equoWebsocketsJsApi)
				.withServer(server)
				.withFiltersAdapter(new EquoWebsocketJsApiRequestFiltersAdapter(null, new EquoWebSocketURLResolver(), this.equoWebSocketService.getPort()))
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
	void setEquoServer(IEquoServer server) {
		this.server = server;
	}

	void unsetEquoServer(IEquoServer server) {
		this.server = null;
	}

}
