package com.make.equo.contribution.api;

import java.util.List;

import com.make.equo.contribution.api.handler.ParameterizedHandler;

public interface IEquoContributionManager {

	EquoContribution getContribution(String contributionName);

	ResolvedContribution resolve(EquoContribution contribution);

	ResolvedContribution getResolvedContributions();

	List<String> getContributionProxiedUris();

	List<ParameterizedHandler> getparameterizedHandlers();

	boolean contains(String contributionName);

	void addContribution(EquoContribution contribution);

}