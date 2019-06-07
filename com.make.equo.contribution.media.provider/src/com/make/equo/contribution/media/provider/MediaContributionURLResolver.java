package com.make.equo.contribution.media.provider;

import java.net.URL;

import com.make.equo.server.offline.api.resolvers.ILocalUrlResolver;

public class MediaContributionURLResolver implements ILocalUrlResolver {

	@Override
	public URL resolve(String pathToResolve) {
		return this.getClass().getClassLoader().getResource(pathToResolve);
	}

}
