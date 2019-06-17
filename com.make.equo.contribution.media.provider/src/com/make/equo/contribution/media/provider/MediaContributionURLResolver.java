package com.make.equo.contribution.media.provider;

import java.net.URL;

import com.make.equo.server.contribution.resolvers.IEquoContributionUrlResolver;

public class MediaContributionURLResolver implements IEquoContributionUrlResolver {

	@Override
	public URL resolve(String pathToResolve) {
		return this.getClass().getClassLoader().getResource(pathToResolve);
	}

}
