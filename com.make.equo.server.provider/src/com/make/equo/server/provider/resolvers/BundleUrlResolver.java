package com.make.equo.server.provider.resolvers;

import java.net.MalformedURLException;
import java.net.URL;

import com.make.equo.server.offline.api.resolvers.ILocalUrlResolver;

public class BundleUrlResolver implements ILocalUrlResolver {

	private String bundleScriptProtocol;

	public BundleUrlResolver(String bundleScriptProtocol) {
		this.bundleScriptProtocol = bundleScriptProtocol;
	}

	@Override
	public String getProtocol() {
		return bundleScriptProtocol;
	}

	@Override
	public URL resolve(String pathToResolve) {
		//TODO test if it works.
		try {
			return new URL(pathToResolve);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
