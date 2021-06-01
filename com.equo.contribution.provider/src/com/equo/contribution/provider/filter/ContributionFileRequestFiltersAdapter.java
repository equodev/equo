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

package com.equo.contribution.provider.filter;

import java.net.URI;
import java.net.URL;

import com.equo.contribution.api.resolvers.IEquoContributionUrlResolver;
import com.equo.server.offline.api.filters.IModifiableResponse;
import com.equo.server.offline.api.filters.OfflineRequestFiltersAdapter;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

/**
 * Filter adapter to resolve request with a local file.
 */
public class ContributionFileRequestFiltersAdapter extends OfflineRequestFiltersAdapter
    implements IModifiableResponse {

  private String contributionName;
  protected IEquoContributionUrlResolver urlResolver;

  /**
   * Parameterized constructor.
   */
  public ContributionFileRequestFiltersAdapter(HttpRequest originalRequest,
      IEquoContributionUrlResolver urlResolver, String contributionName) {
    super(originalRequest);
    this.urlResolver = urlResolver;
    this.contributionName = contributionName;
  }

  @Override
  public HttpResponse clientToProxyRequest(HttpObject httpObject) {
    String requestUri = originalRequest.getUri();
    String fileName = requestUri.substring(
        requestUri.indexOf(contributionName) + contributionName.length(), requestUri.length());
    URL resolvedUrl = urlResolver.resolve(fileName);
    if (resolvedUrl == null) {
      URI requestUriAsUri = URI.create(requestUri);
      final String host = requestUriAsUri.getHost();
      if (host != null && host.contains(contributionName)) {
        fileName = fileName.substring(
            fileName.indexOf(contributionName) + contributionName.length(), fileName.length());
        resolvedUrl = urlResolver.resolve(fileName);
      }
    }
    return super.buildHttpResponse(resolvedUrl);
  }

  @Override
  public FullHttpResponse getOriginalFullHttpResponse() {
    return null;
  }

  @Override
  public boolean isModifiable() {
    return true;
  }

  @Override
  public String modifyOriginalResponse(String responseToTransform) {
    return null;
  }
}
