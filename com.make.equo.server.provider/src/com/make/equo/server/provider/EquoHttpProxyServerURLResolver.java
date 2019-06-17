package com.make.equo.server.provider;

import java.net.URL;

import com.make.equo.server.contribution.resolvers.IEquoContributionUrlResolver;

public class EquoHttpProxyServerURLResolver implements IEquoContributionUrlResolver {

	@Override
	public URL resolve(String pathToResolve) {
		return this.getClass().getClassLoader().getResource(pathToResolve);
	}

}
