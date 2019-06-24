package com.make.equo.analytics.client.provider;

import java.net.URL;

import com.make.equo.server.contribution.resolvers.IEquoContributionUrlResolver;

public class AnalyticsURLResolver implements IEquoContributionUrlResolver {

	@Override
	public URL resolve(String pathToResolve) {
		return this.getClass().getClassLoader().getResource(pathToResolve);
	}

}
