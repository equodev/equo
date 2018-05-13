package com.make.equo.server.provider.resolvers;

import java.net.URL;

import com.make.equo.server.offline.api.resolvers.ILocalUrlResolver;

public class EquoProxyServerUrlResolver implements ILocalUrlResolver {

	private String equoProxyServerFilePath;

	public EquoProxyServerUrlResolver(String equoProxyServerFilePath) {
		this.equoProxyServerFilePath = equoProxyServerFilePath;
	}

	@Override
	public String getProtocol() {
		return equoProxyServerFilePath;
	}

	@Override
	public URL resolve(String pathToResolve) {
		URL resource = this.getClass().getClassLoader().getResource(pathToResolve);
		return resource;
	}

}
