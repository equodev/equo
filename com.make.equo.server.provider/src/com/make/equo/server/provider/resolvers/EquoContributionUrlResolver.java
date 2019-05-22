package com.make.equo.server.provider.resolvers;

import java.net.URL;

import com.make.equo.server.contribution.ContributionDefinition;
import com.make.equo.server.offline.api.resolvers.ILocalUrlResolver;

public class EquoContributionUrlResolver implements ILocalUrlResolver {

	private ContributionDefinition contribution;

	public EquoContributionUrlResolver(ContributionDefinition contribution) {
		this.contribution = contribution;
	}

	@Override
	public String getProtocol() {
		return this.contribution.getUrlResolver().getProtocol();
	}

	@Override
	public URL resolve(String filePath) {
		return this.contribution.getUrlResolver().resolve(filePath);
	}

}
