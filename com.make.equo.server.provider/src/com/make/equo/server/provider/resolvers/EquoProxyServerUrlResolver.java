package com.make.equo.server.provider.resolvers;

import java.net.URL;

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
