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
    URL resolvedUrl = getResolvedUrl();
    return super.buildHttpResponse(resolvedUrl);
  }

  /**
   * Returns the resolved URL for the requested file or null if it's not found.
   */
  public URL getResolvedUrl() {
    String requestUri = originalRequest.uri();
    String uriPath = getPathFromUri(requestUri);
    URL resolvedUrl = getResolvedUrlFromPath(uriPath);
    if (resolvedUrl == null) {
      URI requestUriAsUri = URI.create(requestUri);
      final String host = requestUriAsUri.getHost();
      if (host != null && host.contains(contributionName)) {
        uriPath = getPathFromUri(uriPath);
        resolvedUrl = getResolvedUrlFromPath(uriPath);
      }
    }
    return resolvedUrl;
  }

  private String getPathFromUri(String uri) {
    int indexWhenContributionNameEnds = uri.indexOf(contributionName) + contributionName.length();
    int endIndex = uri.length();
    return uri.substring(indexWhenContributionNameEnds, endIndex);
  }

  private URL getResolvedUrlFromPath(String path) {
    URL resolvedUrl = urlResolver.resolve(path);
    if (resolvedUrl == null) {
      final String pathWithoutQueryParams = path.split("\\?")[0];
      resolvedUrl = urlResolver.resolve(pathWithoutQueryParams);
    }
    return resolvedUrl;
  }

  @Override
  public FullHttpResponse getOriginalFullHttpResponse() {
    return null;
  }

  @Override
  public boolean isModifiable() {
    final URL resolvedUrl = getResolvedUrl();
    if (resolvedUrl == null) {
      return false;
    }
    return resolvedUrl.toString().endsWith(".html");
  }

  @Override
  public String modifyOriginalResponse(String responseToTransform) {
    return null;
  }
}
