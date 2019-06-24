package com.make.equo.ws.provider;

import java.net.URL;

import com.make.equo.server.contribution.resolvers.IEquoContributionUrlResolver;

public class EquoWebSocketURLResolver implements IEquoContributionUrlResolver {
	
	@Override
	public URL resolve(String pathToResolve) {
		return this.getClass().getClassLoader().getResource(pathToResolve);
	}

}
