package com.make.equo.contribution.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.make.equo.contribution.api.EquoContribution;
import com.make.equo.contribution.api.IEquoContributionManager;
import com.make.equo.contribution.api.ResolvedContribution;
import com.make.equo.contribution.api.handler.ParameterizedHandler;

@Component
public class DefaultEquoContributionManager implements IEquoContributionManager {

	@Reference
	private EquoContributionResolver resolver;

	private Map<String, EquoContribution> equoContributions = new HashMap<String, EquoContribution>();

	@Override
	public void addContribution(EquoContribution contribution) {
		try {
			equoContributions.put(contribution.getContributionName().toLowerCase(), contribution);
			resolve(contribution);
			System.out.println("Equo Contribution added: " + contribution.getContributionName());
		} catch (IllegalStateException e) {
			equoContributions.remove(contribution.getContributionName().toLowerCase());
			e.printStackTrace();
		}
	}

	@Override
	public EquoContribution getContribution(String contributionName) {
		return equoContributions.get(contributionName);
	}

	@Override
	public boolean contains(String contributionName) {
		return equoContributions.containsKey(contributionName);
	}

	@Override
	public ResolvedContribution resolve(EquoContribution contribution) {
		return resolver.resolve(contribution);
	}

	@Override
	public ResolvedContribution getResolvedContributions() {
		return resolver.getResolvedContributions();
	}

	public List<String> getContributionProxiedUris() {
		return resolver.getContributionProxiedUris();
	}

	@Override
	public List<ParameterizedHandler> getparameterizedHandlers() {
		List<ParameterizedHandler> result = new ArrayList<>();
		for (Entry<String, EquoContribution> entry : equoContributions.entrySet()) {
			result.addAll(entry.getValue().getParameterizedHandlers());
		}
		return result;
	}

}
