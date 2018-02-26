package com.make.equo.server.provider;

import java.net.URL;

public class EquoFrameworkUrlResolver implements ILocalUrlResolver {

	private String equoFrameworkPath;

	public EquoFrameworkUrlResolver(String equoFrameworkPath) {
		this.equoFrameworkPath = equoFrameworkPath;
	}

	@Override
	public String getProtocol() {
		return equoFrameworkPath;
	}

	@Override
	public URL resolve(String pathToResolve) {
		URL resource = this.getClass().getClassLoader().getResource(pathToResolve);
		return resource;
	}

}
