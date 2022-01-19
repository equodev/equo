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

import org.littleshoot.proxy.HttpFiltersAdapter;

import com.equo.comm.api.IEquoCommService;
import com.equo.contribution.api.handler.IFiltersAdapterHandler;
import com.equo.contribution.api.resolvers.EquoGenericUrlResolver;

import io.netty.handler.codec.http.HttpRequest;

/**
 * Adapter handler for websockets contributions.
 */
public class EquoWebSocketFiltersAdapterHandler implements IFiltersAdapterHandler {

  private IEquoCommService equoWebSocketService;

  EquoWebSocketFiltersAdapterHandler(IEquoCommService equoWebSocketService) {
    this.equoWebSocketService = equoWebSocketService;
  }

  @Override
  public HttpFiltersAdapter getFiltersAdapter(HttpRequest request) {
    return new EquoWebsocketJsApiRequestFiltersAdapter(request,
        new EquoGenericUrlResolver(EquoWebSocketContribution.class.getClassLoader()),
        this.equoWebSocketService.getPort());
  }

}
