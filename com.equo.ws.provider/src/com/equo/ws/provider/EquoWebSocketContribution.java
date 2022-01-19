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

package com.equo.ws.provider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.equo.comm.api.EquoCommContribution;
import com.equo.comm.api.IEquoCommService;
import com.equo.contribution.api.EquoContributionBuilder;

/**
 * Websocket contribution, adding websocket javascript API into the app.
 */
@Component
public class EquoWebSocketContribution extends EquoCommContribution {
  private static final String EQUO_WEBSOCKET_JS_API = "equoComm.js";

  private EquoContributionBuilder builder;

  private IEquoCommService equoWebSocketService;

  @Activate
  protected void activate() {
    builder.withScriptFile(EQUO_WEBSOCKET_JS_API).withContributionName(COMM_CONTRIBUTION_NAME)
        .withFiltersAdapterHandler(new EquoWebSocketFiltersAdapterHandler(equoWebSocketService))
        .build();
  }

  @Reference
  void setEquoWebSocketService(IEquoCommService equoWebSocketService) {
    this.equoWebSocketService = equoWebSocketService;
  }

  void unsetEquoWebSocketService(IEquoCommService equoWebSocketService) {
    this.equoWebSocketService = null;
  }

  @Reference
  void setEquoBuilder(EquoContributionBuilder builder) {
    this.builder = builder;
  }

  void unsetEquoBuilder(EquoContributionBuilder builder) {
    this.builder = null;
  }

}
