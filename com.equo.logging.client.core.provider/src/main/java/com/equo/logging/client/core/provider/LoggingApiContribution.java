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

package com.equo.logging.client.core.provider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.equo.contribution.api.EquoContributionBuilder;
import com.equo.contribution.api.resolvers.EquoGenericUrlResolver;

/**
 * Contribution for the Logging js API.
 */
@Component
public class LoggingApiContribution {

  private static final String LOGGING_CONTRIBUTION_NAME = "equologging";
  private static final String LOGGING_JS_API = "loggingApi.js";

  @Reference
  private EquoContributionBuilder builder;

  @Activate
  protected void activate() {
    builder.withContributionName(LOGGING_CONTRIBUTION_NAME).withScriptFile(LOGGING_JS_API)
        .withUrlResolver(new EquoGenericUrlResolver(LoggingApiContribution.class.getClassLoader()))
        .build();
  }
}
