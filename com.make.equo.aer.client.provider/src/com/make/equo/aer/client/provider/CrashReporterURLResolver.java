package com.make.equo.aer.client.provider;

import java.net.URL;

import com.make.equo.server.contribution.resolvers.IEquoContributionUrlResolver;

public class CrashReporterURLResolver implements IEquoContributionUrlResolver {

	@Override
	public URL resolve(String pathToResolve) {
		return this.getClass().getClassLoader().getResource(pathToResolve);
	}

}
