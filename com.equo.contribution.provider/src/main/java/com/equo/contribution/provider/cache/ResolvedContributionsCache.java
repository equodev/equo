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

package com.equo.contribution.provider.cache;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;

import com.equo.contribution.api.EquoContribution;
import com.equo.contribution.api.ResolvedContribution;

/**
 * Cache of resolved contributions.
 */
@Component(service = ResolvedContributionsCache.class)
public class ResolvedContributionsCache {

  private Map<EquoContribution, ResolvedContribution> resolvedContributions;

  public ResolvedContributionsCache() {
    this.resolvedContributions = new HashMap<EquoContribution, ResolvedContribution>();
  }

  public ResolvedContribution getCached(EquoContribution contribution) {
    return resolvedContributions.get(contribution);
  }

  public ResolvedContribution put(EquoContribution contribution,
      ResolvedContribution resolvedContribution) {
    return resolvedContributions.put(contribution, resolvedContribution);
  }

}
