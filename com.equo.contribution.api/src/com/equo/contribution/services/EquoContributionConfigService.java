/****************************************************************************
**
** Copyright (C) 2021 Equo
**
** This file is part of Equo Framework.
**
** Commercial License Usage
** Licensees holding valid commercial Equo licenses may use this file in
** accordance with the commercial license agreement provided with the
** Software or, alternatively, in accordance with the terms contained in
** a written agreement between you and Equo. For licensing terms
** and conditions see https://www.equoplatform.com/terms.
**
** GNU General Public License Usage
** Alternatively, this file may be used under the terms of the GNU
** General Public License version 3 as published by the Free Software
** Foundation. Please review the following
** information to ensure the GNU General Public License requirements will
** be met: https://www.gnu.org/licenses/gpl-3.0.html.
**
****************************************************************************/

package com.equo.contribution.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.equo.contribution.api.EquoContribution;
import com.equo.contribution.api.EquoContributionBuilder;
import com.equo.contribution.api.IEquoContributionManager;
import com.equo.contribution.api.resolvers.EquoGenericUrlResolver;
import com.equo.contribution.services.pojo.ConfigContribution;
import com.equo.contribution.services.pojo.ContributionSet;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * A service to define contributions through JSON files.
 */
@Component
public class EquoContributionConfigService implements IContributionConfigService {

  @Reference
  private IEquoContributionManager manager;

  /**
   * Defines a new contribution.
   */
  public List<EquoContribution> defineContributions(JsonObject configJson, Bundle bundle) {
    ArrayList<EquoContribution> contributions = new ArrayList<EquoContribution>();
    Gson parser = new Gson();
    ContributionSet configSet = parser.fromJson(configJson, ContributionSet.class);
    manager.setContributedLimitedConnectionPage(
        Optional.ofNullable(configSet.getLimitedConnectionPagePath()));
    for (ConfigContribution configCont : configSet.getContributions()) {
      contributions.add(parseContributionJsonConfig(configCont, bundle));
    }
    return contributions;
  }

  /**
   * Parses a contribution POJO and creates an Equo contribution with its values.
   */
  public EquoContribution parseContributionJsonConfig(ConfigContribution config, Bundle bundle) {
    if (config.isEmpty()) {
      throw new RuntimeException(
          "A Contribution Config request must be at least one field in the Json config declared.");
    }

    EquoContributionBuilder builder = new EquoContributionBuilder();

    String contributionName = config.getContributionName();
    String contributionHtmlName = config.getContributionHtmlName();
    List<String> proxiedUris = config.getProxiedUris();

    if (contributionName != null) {
      builder.withContributionName(contributionName);
    }
    if (contributionHtmlName != null) {
      builder.withBaseHtmlResource(contributionHtmlName);
    }
    if (proxiedUris != null) {
      for (String uri : proxiedUris) {
        builder.withProxiedUri(uri);
      }
    }

    List<String> scripts = config.getContributedScripts();
    Map<String, String> pathsWithScripts = config.getPathsWithScripts();

    if (scripts != null) {
      builder.withScriptFiles(scripts);
    }
    if (pathsWithScripts != null) {
      for (String path : pathsWithScripts.keySet()) {
        builder.withPathWithScript(path, pathsWithScripts.get(path));
      }
    }
    return builder.withManager(manager).withUrlResolver(
        new EquoGenericUrlResolver(bundle.adapt(BundleWiring.class).getClassLoader())).build();
  }

}
