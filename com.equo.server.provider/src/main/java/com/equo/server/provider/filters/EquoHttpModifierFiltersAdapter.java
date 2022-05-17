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

package com.equo.server.provider.filters;

import java.util.List;

import com.equo.contribution.api.ResolvedContribution;
import com.equo.server.offline.api.IEquoOfflineServer;
import com.equo.server.offline.api.filters.IModifiableResponse;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * Adapter that makes modified responses.
 */
public class EquoHttpModifierFiltersAdapter extends EquoHttpFiltersAdapter {

  private List<String> proxiedUrls;
  private List<String> equoContributionsJsApis;
  private List<String> equoContributionStyles;
  private String customJsScripts;
  private String customStyles;

  /**
   * Parameterized constructor.
   */
  public EquoHttpModifierFiltersAdapter(HttpRequest originalRequest,
      ResolvedContribution globalContribution, IEquoOfflineServer equoOfflineServer,
      List<String> proxiedUrls) {
    super(originalRequest, equoOfflineServer);
    this.equoContributionsJsApis = globalContribution.getScripts();
    this.equoContributionStyles = globalContribution.getStyles();
    this.customJsScripts = globalContribution.getCustomScripts(originalRequest.uri());
    this.customStyles = globalContribution.getCustomStyles(originalRequest.uri());
    this.proxiedUrls = proxiedUrls;
  }

  @Override
  public HttpObject serverToProxyResponse(HttpObject httpObject) {
    if (httpObject instanceof FullHttpResponse) {
      FullHttpResponse fullObject = ((FullHttpResponse) httpObject);
      int code = fullObject.status().code();
      if (code == HttpResponseStatus.OK.code()) {
        FullHttpResponse fullResponse = (FullHttpResponse) httpObject;
        IModifiableResponse fullHttpResponseWithTransformersResources =
            new FullHttpResponseWithTransformersResources(fullResponse, equoContributionsJsApis,
                equoContributionStyles, customJsScripts, customStyles);
        if (fullHttpResponseWithTransformersResources.isModifiable()) {
          FullHttpResponse generatedModifiedResponse =
              fullHttpResponseWithTransformersResources.generateModifiedResponse();
          saveRequestResponseIfPossible(originalRequest, generatedModifiedResponse);
          return generatedModifiedResponse;
        }
      } else if (code == HttpResponseStatus.MOVED_PERMANENTLY.code()
          || code == HttpResponseStatus.PERMANENT_REDIRECT.code()
          || code == HttpResponseStatus.TEMPORARY_REDIRECT.code()) {
        String location = fullObject.headers().get("location");
        proxiedUrls.add(location);
      }
    }
    return super.serverToProxyResponse(httpObject);
  }
}
