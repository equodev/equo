package com.make.equo.ws.provider;

import java.net.URL;

import com.make.equo.server.contribution.EquoContribution;
import com.make.equo.server.offline.api.resolvers.ILocalUrlResolver;

public class EquoWebSocketURLResolver implements ILocalUrlResolver {

	private EquoContribution contribution;
	
	EquoWebSocketURLResolver(EquoContribution contribution) {
		this.contribution = contribution;
	}
	
	@Override
	public String getProtocol() {
		return contribution.getContributionBaseUri();
	}
	
	@Override
	public URL resolve(String pathToResolve) {
		return this.getClass().getClassLoader().getResource(pathToResolve);
	}

}
