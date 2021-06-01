package com.equo.contribution.api;

import java.util.List;

/**
 * Interface for contribution manager. The manager will concentrate all the
 * added contributions and enable them to be obtained.
 */
public interface IEquoContributionManager {

  EquoContribution getContribution(String contributionName);

  ResolvedContribution resolve(EquoContribution contribution);

  ResolvedContribution getResolvedContributions();

  List<String> getContributionProxiedUris();

  List<Runnable> getContributionStartProcedures();

  boolean contains(String contributionName);

  void addContribution(EquoContribution contribution);

}