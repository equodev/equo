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

package com.equo.server.provider.filters;

import org.littleshoot.proxy.HttpFiltersAdapter;

import com.equo.server.offline.api.IEquoOfflineServer;
import com.equo.server.provider.EquoHttpProxyServer;

import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;

/**
 * Simple adapter that caches requests.
 */
public class EquoHttpFiltersAdapter extends HttpFiltersAdapter {

  protected IEquoOfflineServer equoOfflineServer;

  public EquoHttpFiltersAdapter(HttpRequest originalRequest, IEquoOfflineServer equoOfflineServer) {
    super(originalRequest);
    this.equoOfflineServer = equoOfflineServer;
  }

  @Override
  public HttpObject serverToProxyResponse(HttpObject httpObject) {
    saveRequestResponseIfPossible(originalRequest, httpObject);
    return super.serverToProxyResponse(httpObject);
  }

  public IEquoOfflineServer getEquoOfflineServer() {
    return equoOfflineServer;
  }

  /**
   * Save the request to be used when entering offline mode.
   */
  public void saveRequestResponseIfPossible(HttpRequest originalRequest, HttpObject httpObject) {
    if (EquoHttpProxyServer.isOfflineCacheSupported()) {
      getEquoOfflineServer().saveRequestResponse(originalRequest, httpObject);
    }
  }
}
