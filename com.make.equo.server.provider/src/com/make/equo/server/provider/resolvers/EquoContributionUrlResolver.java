package com.make.equo.server.provider.resolvers;

import java.net.URL;
import java.util.Map;

import com.make.equo.contribution.api.IEquoContribution;
import com.make.equo.server.offline.api.resolvers.ILocalUrlResolver;

public class EquoContributionUrlResolver implements ILocalUrlResolver {

	private String equoContributionPath;
	private Map<String, IEquoContribution> equoContributions;

	public EquoContributionUrlResolver(String equoContributionPath, Map<String, IEquoContribution> equoContributions) {
		this.equoContributionPath = equoContributionPath;
		this.equoContributions = equoContributions;
	}

	@Override
	public String getProtocol() {
		return equoContributionPath;
	}

	@Override
	public URL resolve(String filePath) {
		String[] equoContributionParts = filePath.split("/");
		if (equoContributionParts.length < 2) {
			throw new RuntimeException(
					"The equo contribution must define a \"type\" property and contribute javascript files.");
		}
		IEquoContribution equoContribution = equoContributions.get(equoContributionParts[0]);
		String equoContributionPath = filePath.replaceFirst(equoContributionParts[0] + "/", "");
		return equoContribution.getJavascriptAPIResource(equoContributionPath);
	}

}
