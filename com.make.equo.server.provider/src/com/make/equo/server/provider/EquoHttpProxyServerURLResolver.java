package com.make.equo.server.provider;

import java.net.URL;

import com.make.equo.server.contribution.EquoContribution;
import com.make.equo.server.offline.api.resolvers.ILocalUrlResolver;

public class EquoHttpProxyServerURLResolver implements ILocalUrlResolver {

	private String protocol;
	
	EquoHttpProxyServerURLResolver(EquoContribution contribution) {
		this.protocol = contribution.getContributionBaseUri();
	}
	
	public EquoHttpProxyServerURLResolver(String protocol) {
		this.protocol = protocol;
	}
	
	@Override
	public String getProtocol() {
		return this.protocol;
	}

	@Override
	public URL resolve(String pathToResolve) {
		return this.getClass().getResource(pathToResolve);
	}

}
