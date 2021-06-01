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

  private List<String> equoContributionsJsApis;
  private List<String> equoContributionStyles;
  private String customJsScripts;
  private String customStyles;

  /**
   * Parameterized constructor.
   */
  public EquoHttpModifierFiltersAdapter(HttpRequest originalRequest,
      ResolvedContribution globalContribution, IEquoOfflineServer equoOfflineServer) {
    super(originalRequest, equoOfflineServer);
    this.equoContributionsJsApis = globalContribution.getScripts();
    this.equoContributionStyles = globalContribution.getStyles();
    this.customJsScripts = globalContribution.getCustomScripts(originalRequest.getUri());
    this.customStyles = globalContribution.getCustomStyles(originalRequest.getUri());
  }

  @Override
  public HttpObject serverToProxyResponse(HttpObject httpObject) {
    if (httpObject instanceof FullHttpResponse
        && ((FullHttpResponse) httpObject).getStatus().code() == HttpResponseStatus.OK.code()) {
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
    }
    return super.serverToProxyResponse(httpObject);
  }
}
