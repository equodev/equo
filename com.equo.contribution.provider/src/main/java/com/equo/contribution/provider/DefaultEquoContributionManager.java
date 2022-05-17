/****************************************************************************
**
** Copyright (C) 2021 Equo
**
** This file is part of the Equo SDK.
**
** Commercial License Usage
** Licensees holding valid commercial Equo licenses may use this file in
** accordance with the commercial license agreement provided with the
** Software or, alternatively, in accordance with the terms contained in
** a written agreement between you and Equo. For licensing terms
** and conditions see https://www.equo.dev/terms.
**
** GNU General Public License Usage
** Alternatively, this file may be used under the terms of the GNU
** General Public License version 3 as published by the Free Software
** Foundation. Please review the following
** information to ensure the GNU General Public License requirements will
** be met: https://www.gnu.org/licenses/gpl-3.0.html.
**
****************************************************************************/

package com.equo.contribution.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.equo.contribution.api.EquoContribution;
import com.equo.contribution.api.IEquoContributionManager;
import com.equo.contribution.api.ResolvedContribution;
import com.equo.contribution.provider.dependency.IDependencyTreeManager;
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

  private Optional<String> limitedConnectionPagePath = Optional.empty();
  private Map<String, EquoContribution> equoContributions = new HashMap<String, EquoContribution>();
  private List<String> equoContributionLoadOrder = new ArrayList<>();

  @Reference
  private IDependencyTreeManager dependencyTreeManager;

  @Override
  public synchronized void addContribution(EquoContribution contribution) {
    String contributionName = contribution.getContributionName();
    // Check if the contribution has already been uploaded.
    if (equoContributions.containsKey(contributionName)) {
      logger.info("Equo Contribution " + contributionName + " it was already loaded");
      return;
    }

    List<String> contributionDependencies = contribution.getDependencies();
    // Check if contribution dependencies are satisfied.
    for (int i = 0; i < contributionDependencies.size(); i++) {
      if (!equoContributions.containsKey(contributionDependencies.get(i))) {
        dependencyTreeManager.addContribution(contribution);
        return;
      }
    }
    try {
      equoContributions.put(contributionName.toLowerCase(), contribution);
      equoContributionLoadOrder.add(contributionName);
      resolve(contribution);
      if (logger == null) {
        logger = LoggerFactory.getLogger(DefaultEquoContributionManager.class);
      }
      logger.info("Equo Contribution added: " + contributionName);

      // Check if any pending contributions can be loaded.
      dependencyTreeManager.pullSatisfiedContributions(equoContributions)
          .forEach((equoContribution) -> {
            addContribution(equoContribution);
          });
    } catch (IllegalStateException e) {
      equoContributions.remove(contributionName.toLowerCase());
      e.printStackTrace();
    }
  }

  @Override
  public EquoContribution getContribution(String contributionName) {
    if (contributionName == null) {
      return null;
    }
    return equoContributions.get(contributionName.toLowerCase());
  }

  @Override
  public List<String> getContributions() {
    return new ArrayList<String>(equoContributionLoadOrder);
  }

  @Override
  public boolean contains(String contributionName) {
    if (contributionName == null) {
      return false;
    }
    return equoContributions.containsKey(contributionName.toLowerCase());
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

  @Override
  public Optional<String> getContributedLimitedConnectionPage() {
    return limitedConnectionPagePath;
  }

  @Override
  public void setContributedLimitedConnectionPage(Optional<String> limitedConnectionPage) {
    this.limitedConnectionPagePath = limitedConnectionPage;
  }

  @Override
  public List<EquoContribution> getPendingContributions() {
    return dependencyTreeManager.getPendingContributions();
  }

  @Override
  public void reportPendingContributions() {
    List<EquoContribution> pendingDependencies = getPendingContributions();
    if (pendingDependencies.size() > 0) {
      logger.error("The following contributions could not be loaded due to dependency conflicts: "
          + pendingDependencies);
    }
  }
}
