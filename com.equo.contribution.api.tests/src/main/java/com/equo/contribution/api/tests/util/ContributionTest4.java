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

package com.equo.contribution.api.tests.util;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.equo.contribution.api.EquoContributionBuilder;
import com.equo.contribution.api.resolvers.EquoGenericUrlResolver;

@Component
public class ContributionTest4 implements IContributionTest4 {
  @Reference
  private EquoContributionBuilder builder;

  @Override
  public void load() {
    builder //
        .withContributionName("contributionTest4") //
        .withBaseHtmlResource("index.html") //
        .withPathWithScript("", "index.bundle.js") //
        .withDependencies("contributiontest5")
        .withUrlResolver(new EquoGenericUrlResolver(ContributionTest4.class.getClassLoader())) //
        .build();
  }
}
