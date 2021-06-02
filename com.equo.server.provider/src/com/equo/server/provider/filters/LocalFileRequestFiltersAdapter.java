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

import com.equo.server.offline.api.filters.OfflineRequestFiltersAdapter;
import com.equo.server.offline.api.resolvers.ILocalUrlResolver;

import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

/**
 * Adapter to response a local file for the requests.
 */
public class LocalFileRequestFiltersAdapter extends OfflineRequestFiltersAdapter {

  public LocalFileRequestFiltersAdapter(HttpRequest originalRequest,
      ILocalUrlResolver urlResolver) {
    super(originalRequest, urlResolver);
  }

  @Override
  public HttpResponse clientToProxyRequest(HttpObject httpObject) {
    String uri = originalRequest.getUri();
    this.setLocalFilePathWithProtocol(uri);
    return super.clientToProxyRequest(httpObject);
  }

}
