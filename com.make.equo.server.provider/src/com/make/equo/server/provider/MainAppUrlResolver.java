package com.make.equo.server.provider;

import java.net.URL;

import org.osgi.framework.Bundle;

public class MainAppUrlResolver implements ILocalUrlResolver {

	private String localScriptProtocol;
	private Bundle mainEquoAppBundle;

	public MainAppUrlResolver(String localScriptProtocol, Bundle mainEquoAppBundle) {
		this.localScriptProtocol = localScriptProtocol;
		this.mainEquoAppBundle = mainEquoAppBundle;
	}

	@Override
	public String getProtocol() {
		return localScriptProtocol;
	}

	@Override
	public URL resolve(String urlToResolve) {
		URL resource = mainEquoAppBundle.getResource(urlToResolve);
		return resource;
	}

}
