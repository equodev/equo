package com.make.equo.server.contribution.configservice;

import java.net.URL;

import org.osgi.framework.Bundle;

import com.make.equo.server.contribution.resolvers.IEquoContributionUrlResolver;

public class CustomContributionURLResolver implements IEquoContributionUrlResolver{
	
	private Bundle bundle;
	
	
	
	public CustomContributionURLResolver(Bundle bundle) {
		super();
		this.bundle = bundle;
	}

	@Override
	public URL resolve(String pathToResolve) {
		return bundle.getClass().getClassLoader().getResource(pathToResolve); //Consultar
	}

}
