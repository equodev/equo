package com.make.equo.renderers.contributions;

import java.net.URL;

import com.make.equo.server.offline.api.resolvers.ILocalUrlResolver;

public class EquoRenderersURLResolver implements ILocalUrlResolver {
	
	@Override
	public URL resolve(String pathToResolve) {
		return this.getClass().getClassLoader().getResource(pathToResolve);
	}

}
