package com.make.equo.contribution.provider.cache;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

import com.make.equo.contribution.api.EquoContribution;
import com.make.equo.contribution.api.ResolvedContribution;

@Component(service = ResolvedContributionsCache.class)
public class ResolvedContributionsCache {

	private Map<EquoContribution, ResolvedContribution> resolvedContributions;

	public ResolvedContributionsCache() {
		this.resolvedContributions = new HashMap<EquoContribution, ResolvedContribution>();
	}

	public ResolvedContribution getCached(EquoContribution contribution) {
		return resolvedContributions.get(contribution);
	}

	public ResolvedContribution put(EquoContribution contribution, ResolvedContribution resolvedContribution) {
		return resolvedContributions.put(contribution, resolvedContribution);
	}

}
