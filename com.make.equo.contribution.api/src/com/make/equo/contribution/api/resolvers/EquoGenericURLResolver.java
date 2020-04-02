package com.make.equo.contribution.api.resolvers;

import java.net.URL;

public class EquoGenericURLResolver implements IEquoContributionUrlResolver {

	private ClassLoader classLoader;

	public EquoGenericURLResolver(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	@Override
	public URL resolve(String pathToResolve) {
		return classLoader.getResource(pathToResolve);
	}
}