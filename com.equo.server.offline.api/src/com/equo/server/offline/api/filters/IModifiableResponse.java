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

package com.equo.server.offline.api.filters;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.entity.ContentType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpUtil;

/**
 * Interface for handlers that makes modifiable responses.
 */
public interface IModifiableResponse {

  /**
   * Generates a modified response in which includes the contributed scripts and
   * style into the original response content.
   * @return modified response
   */
  default FullHttpResponse generateModifiedResponse() {
    FullHttpResponse originalFullHttpResponse = getOriginalFullHttpResponse();
    HttpHeaders originalHeaders = originalFullHttpResponse.headers();
    originalHeaders.remove("Content-Security-Policy");
    String contentTypeHeader = originalHeaders.get("Content-Type");
    ContentType contentType = ContentType.parse(contentTypeHeader);
    Charset charset = contentType.getCharset();

    ByteBuf content = originalFullHttpResponse.content();

    byte[] data = new byte[content.readableBytes()];
    content.readBytes(data);

    String responseToTransform = createStringFromData(data, charset);

    String transformedResponse = modifyOriginalResponse(responseToTransform);

    byte[] bytes = createDataFromString(transformedResponse, charset);
    ByteBuf transformedContent = Unpooled.buffer(bytes.length);
    transformedContent.writeBytes(bytes);

    DefaultFullHttpResponse transformedHttpResponse =
        new DefaultFullHttpResponse(originalFullHttpResponse.protocolVersion(),
            originalFullHttpResponse.status(), transformedContent);
    transformedHttpResponse.headers().set(originalHeaders);
    HttpUtil.setContentLength(transformedHttpResponse, bytes.length);

    return transformedHttpResponse;
  }

  static String createStringFromData(byte[] data, Charset charset) {
    return (charset == null) ? new String(data) : new String(data, charset);
  }

  static byte[] createDataFromString(String string, Charset charset) {
    return (charset == null) ? string.getBytes() : string.getBytes(charset);
  }

  FullHttpResponse getOriginalFullHttpResponse();

  boolean isModifiable();

  /**
   * Generates a HTML modified response in which includes the contributed scripts
   * and style into the original response content.
   * @param  responseToTransform     request response to modify.
   * @param  customJsScripts         custom js scripts for added in response.
   * @param  customStyles            custom js styles for added in response.
   * @param  equoContributionsJsApis list with contribution js scripts for added
   *                                 in response.
   * @param  equoContributionStyles  list with contribution js styles for added in
   *                                 response.
   * @return                         modified response
   */
  default String modifyOriginalResponseHtml(String responseToTransform, String customJsScripts,
      String customStyles, List<String> equoContributionsJsApis,
      List<String> equoContributionStyles) {
    String customResponse = new String(responseToTransform);

    Document html = Jsoup.parse(customResponse);

    html.head().prepend(customJsScripts);
    for (int i = equoContributionsJsApis.size() - 1; i >= 0; i--) {
      html.head().prepend(equoContributionsJsApis.get(i));
    }
    for (int i = equoContributionStyles.size() - 1; i >= 0; i--) {
      html.body().append(equoContributionStyles.get(i));
    }

    html.body().append(customStyles);
    return html.toString();
  }

  String modifyOriginalResponse(String responseToTransform);

}
