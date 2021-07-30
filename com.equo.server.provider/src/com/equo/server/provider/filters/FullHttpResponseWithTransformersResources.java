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

import com.equo.server.offline.api.filters.IModifiableResponse;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;

/**
 * Modified HTTP response.
 */
public class FullHttpResponseWithTransformersResources implements IModifiableResponse {

  private FullHttpResponse originalFullHttpResponse;
  private List<String> equoContributionsJsApis;
  private List<String> equoContributionStyles;
  private String customJsScripts;
  private String customStyles;

  /**
   * Parameterized constructor.
   */
  public FullHttpResponseWithTransformersResources(FullHttpResponse originalFullHttpResponse,
      List<String> equoContributionsJsApis, List<String> equoContributionStyles,
      String customJsScripts, String customStyles) {
    this.originalFullHttpResponse = originalFullHttpResponse;
    this.equoContributionsJsApis = equoContributionsJsApis;
    this.equoContributionStyles = equoContributionStyles;
    this.customJsScripts = customJsScripts;
    this.customStyles = customStyles;
  }

  @Override
  public FullHttpResponse getOriginalFullHttpResponse() {
    return originalFullHttpResponse;
  }

  @Override
  public boolean isModifiable() {
    final HttpHeaders headers = originalFullHttpResponse.headers();
    if (isAnAttachment(headers)) {
      return false;
    }
    return isAnHtmlFile(headers);
  }

  private boolean isAnHtmlFile(HttpHeaders headers) {
    String contentTypeHeader = headers.get("Content-Type");
    return contentTypeHeader != null && contentTypeHeader.startsWith("text/html");
  }

  private boolean isAnAttachment(HttpHeaders headers) {
    final String contentDispositionheader = headers.get("Content-Disposition");
    return contentDispositionheader != null && contentDispositionheader.contains("attachment");
  }

  @Override
  public String modifyOriginalResponse(String responseToTransform) {
    StringBuilder customResponse = new StringBuilder(responseToTransform);
    for (String jsApi : equoContributionsJsApis) {
      customResponse.append(jsApi);
    }
    for (String style : equoContributionStyles) {
      customResponse.append(style);
    }
    customResponse.append(customJsScripts);
    customResponse.append(customStyles);
    return customResponse.toString();
  }
}
