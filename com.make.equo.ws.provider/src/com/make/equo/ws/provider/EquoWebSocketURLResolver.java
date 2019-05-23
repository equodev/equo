package com.make.equo.ws.provider;

import java.net.URL;

import com.make.equo.server.offline.api.resolvers.ILocalUrlResolver;

public class EquoWebSocketURLResolver implements ILocalUrlResolver {

	@Override
	public URL resolve(String pathToResolve) {
		return this.getClass().getResource(pathToResolve);
	}

}
