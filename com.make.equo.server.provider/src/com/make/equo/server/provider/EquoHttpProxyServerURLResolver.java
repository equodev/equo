package com.make.equo.server.provider;

import java.net.URL;

import com.make.equo.server.offline.api.resolvers.ILocalUrlResolver;

public class EquoHttpProxyServerURLResolver implements ILocalUrlResolver {

	@Override
	public URL resolve(String pathToResolve) {
		return this.getClass().getClassLoader().getResource(pathToResolve);
	}

}
