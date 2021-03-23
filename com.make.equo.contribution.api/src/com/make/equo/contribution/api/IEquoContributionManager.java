package com.make.equo.contribution.api;

import java.util.List;

public interface IEquoContributionManager {

	EquoContribution getContribution(String contributionName);

	ResolvedContribution resolve(EquoContribution contribution);

	ResolvedContribution getResolvedContributions();

	List<String> getContributionProxiedUris();

	List<Runnable> getContributionStartProcedures();

	boolean contains(String contributionName);

	void addContribution(EquoContribution contribution);

}