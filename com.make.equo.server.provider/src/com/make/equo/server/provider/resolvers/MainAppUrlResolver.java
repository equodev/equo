package com.make.equo.server.provider.resolvers;

import java.net.URL;

import com.make.equo.application.api.IEquoApplication;
import com.make.equo.server.offline.api.resolvers.ILocalUrlResolver;

public class MainAppUrlResolver implements ILocalUrlResolver {

	private String localScriptProtocol;
	private IEquoApplication equoApplication;

	public MainAppUrlResolver(String localScriptProtocol, IEquoApplication equoApplication) {
		this.localScriptProtocol = localScriptProtocol;
		this.equoApplication = equoApplication;
	}

	@Override
	public String getProtocol() {
		return localScriptProtocol;
	}

	@Override
	public URL resolve(String urlToResolve) {
		URL resource = equoApplication.getClass().getClassLoader().getResource(urlToResolve);
		return resource;
	}

}
