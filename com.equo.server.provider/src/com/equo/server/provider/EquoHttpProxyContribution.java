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

package com.equo.server.provider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.equo.contribution.api.EquoContributionBuilder;
import com.equo.contribution.api.resolvers.EquoGenericUrlResolver;

/**
 * Server contribution. Injects JQuery and the Framework API.
 */
@Component
public class EquoHttpProxyContribution {

  public static final String PROXY_CONTRIBUTION_NAME = "equoproxy";
  private static final String JQUERY_JS_API =
      "https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js";
  private static final String EQUO_FRAMEWORK_JS_API = "equoFramework.js";
  private static final String DOM_MODIFIER_JS_API = "domModifier.js";

  private EquoContributionBuilder builder;

  @Activate
  protected void activate() {
    String value = System.getProperty("change_original_html");
    if (value == null || (value != null && Boolean.parseBoolean(value))) {
      builder.withContributionName(PROXY_CONTRIBUTION_NAME).withScriptFile(EQUO_FRAMEWORK_JS_API)
          .withScriptFile(JQUERY_JS_API).withScriptFile(DOM_MODIFIER_JS_API).withUrlResolver(
              new EquoGenericUrlResolver(EquoHttpProxyContribution.class.getClassLoader()))
          .build();
    } else {
      builder.withContributionName(PROXY_CONTRIBUTION_NAME).withScriptFile(EQUO_FRAMEWORK_JS_API)
          .withScriptFile(JQUERY_JS_API).withUrlResolver(
              new EquoGenericUrlResolver(EquoHttpProxyContribution.class.getClassLoader()))
          .build();
    }
  }

  @Reference
  void setEquoBuilder(EquoContributionBuilder builder) {
    this.builder = builder;
  }

  void unsetEquoBuilder(EquoContributionBuilder builder) {
    this.builder = null;
  }
}
