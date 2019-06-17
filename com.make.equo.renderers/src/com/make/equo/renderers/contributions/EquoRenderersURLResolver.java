package com.make.equo.renderers.contributions;

import java.net.URL;

import com.make.equo.server.contribution.resolvers.IEquoContributionUrlResolver;

public class EquoRenderersURLResolver implements IEquoContributionUrlResolver {
	
	@Override
	public URL resolve(String pathToResolve) {
		return this.getClass().getClassLoader().getResource(pathToResolve);
	}

}
