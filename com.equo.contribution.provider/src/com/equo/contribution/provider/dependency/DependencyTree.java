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

package com.equo.contribution.provider.dependency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.service.component.annotations.Component;

import com.equo.contribution.api.EquoContribution;

/**
 * Contains a structure to organize contributions by dependencies.
 */
@Component
public class DependencyTree implements IDependencyTreeManager {

  private Map<String, List<EquoContribution>> dependencies;
  private Set<String> pendingContributions;

  /**
   * DependencyTree constructor.
   */
  public DependencyTree() {
    dependencies = new HashMap<>();
    pendingContributions = new HashSet<>();
  }

  @Override
  public void addContribution(EquoContribution equoContribution) {
    pendingContributions.add(equoContribution.getContributionName());
    equoContribution.getDependencies().forEach((dependency) -> {
      String dep = dependency.toLowerCase();
      if (dependencies.containsKey(dep)) {
        dependencies.get(dep).add(equoContribution);
      } else {
        List<EquoContribution> equoContributionList = new ArrayList<>();
        equoContributionList.add(equoContribution);
        dependencies.put(dep, equoContributionList);
      }
    });
  }

  private void removeContribution(EquoContribution equoContribution) {
    pendingContributions.remove(equoContribution.getContributionName());
  }

  private List<EquoContribution> getContributions(String dependency) {
    String dep = dependency.toLowerCase();
    if (dependencies.containsKey(dep)) {
      return dependencies.get(dep);
    }
    return new ArrayList<EquoContribution>();
  }

  @Override
  public List<EquoContribution> pullSatisfiedContributions(String dependency) {
    List<EquoContribution> satisfiedContributions = new ArrayList<>();
    for (EquoContribution equoContribution : getContributions(dependency)) {
      List<String> deps = equoContribution.getDependencies();
      deps.remove(dependency);
      boolean dependenciesSatisfied = true;
      for (String dep : deps) {
        if (dependencies.containsKey(dep)) {
          dependenciesSatisfied = false;
          break;
        }
      }
      if (dependenciesSatisfied) {
        removeContribution(equoContribution);
        satisfiedContributions.add(equoContribution);
      }
    }
    // Update tree
    removeDependency(dependency);
    return satisfiedContributions;
  }

  @Override
  public void removeDependency(String dependency) {
    String dep = dependency.toLowerCase();
    if (dependencies.containsKey(dep)) {
      dependencies.remove(dep);
    }
  }

  @Override
  public List<String> getPendingContributions() {
    return new ArrayList<String>(pendingContributions);
  }
}
