package com.make.equo.renderers.contributions;

import java.net.URL;

import com.make.equo.server.offline.api.resolvers.ILocalUrlResolver;

public class EquoRenderersURLResolver implements ILocalUrlResolver {

	@Override
	public String getProtocol() {
		return EquoRenderersContribution.EQUO_RENDERERS_BASE_URI;
	}
	
	@Override
	public URL resolve(String pathToResolve) {
		return this.getClass().getClassLoader().getResource(pathToResolve);
	}

}
