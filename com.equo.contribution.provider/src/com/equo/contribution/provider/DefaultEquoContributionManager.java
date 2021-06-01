package com.equo.contribution.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.equo.contribution.api.EquoContribution;
import com.equo.contribution.api.IEquoContributionManager;
import com.equo.contribution.api.ResolvedContribution;
import com.equo.logging.client.api.Logger;
import com.equo.logging.client.api.LoggerFactory;

/**
 * Default implementation of Contribution Manager.
 */
@Component
public class DefaultEquoContributionManager implements IEquoContributionManager {
  protected static Logger logger;

  @Reference
  private EquoContributionResolver resolver;

  private Map<String, EquoContribution> equoContributions = new HashMap<String, EquoContribution>();

  @Override
  public void addContribution(EquoContribution contribution) {
    try {
      equoContributions.put(contribution.getContributionName().toLowerCase(), contribution);
      resolve(contribution);
      if (logger == null) {
        logger = LoggerFactory.getLogger(DefaultEquoContributionManager.class);
      }
      logger.info("Equo Contribution added: " + contribution.getContributionName());
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
  public List<Runnable> getContributionStartProcedures() {
    List<Runnable> startProcedures = new ArrayList<>();
    for (EquoContribution contribution : equoContributions.values()) {
      Runnable runnable = contribution.getRunnableAtStart();
      if (runnable != null) {
        startProcedures.add(runnable);
      }
    }
    return startProcedures;
  }

}
