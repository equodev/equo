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

package com.equo.comm.ws.provider;

import static com.equo.comm.api.EquoCommContribution.COMM_CONTRIBUTION_NAME;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import javax.activation.MimetypesFileTypeMap;

import org.littleshoot.proxy.HttpFiltersAdapter;

import com.equo.contribution.api.resolvers.IEquoContributionUrlResolver;
import com.google.common.io.ByteStreams;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;

/**
 * Adapter handler for websocket api contribution. It is responsible for
 * modifying the contributed script to resolve a ws port placeholder with the
 * actual value.
 */
public class EquoWebsocketJsApiRequestFiltersAdapter extends HttpFiltersAdapter {

  private int portNumber;
  private IEquoContributionUrlResolver urlResolver;

  /**
   * Parameterized constructor.
   */
  public EquoWebsocketJsApiRequestFiltersAdapter(HttpRequest originalRequest,
      IEquoContributionUrlResolver urlResolver, int portNumber) {
    super(originalRequest);
    this.urlResolver = urlResolver;
    this.portNumber = portNumber;
  }

  /**
   * Handles the request from the client to the proxy.
   */
  public HttpResponse clientToProxyRequest(HttpObject httpObject) {
    String requestUri = originalRequest.uri();
    String fileName = requestUri.substring(
        requestUri.indexOf(COMM_CONTRIBUTION_NAME) + COMM_CONTRIBUTION_NAME.length(),
        requestUri.length());
    URL resolvedUrl = urlResolver.resolve(fileName);
    return buildHttpResponse(resolvedUrl);
  }

  private HttpResponse buildHttpResponse(URL resolvedUrl) {
    try {
      final URLConnection connection = resolvedUrl.openConnection();
      InputStream inputStream = connection.getInputStream();
      byte[] bytes = ByteStreams.toByteArray(inputStream);
      ByteBuf buffer = Unpooled.wrappedBuffer(bytes);
      final MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
      String fileName = resolvedUrl.getFile().substring(1);
      String contentType = fileTypeMap.getContentType(fileName);
      inputStream.close();
      return buildResponse(buffer, contentType);
    } catch (IOException e) {
      // TODO log exception
      ByteBuf buffer;
      try {
        buffer = Unpooled.wrappedBuffer("".getBytes("UTF-8"));
        return buildResponse(buffer, "text/html");
      } catch (UnsupportedEncodingException e1) {
        // TODO log exception
        return null;
      }
    }
  }

  private HttpResponse buildResponse(ByteBuf buffer, String contentType) {
    String contentFile = buffer.toString(Charset.defaultCharset());
    String responseAsString = String.format(contentFile, portNumber);
    byte[] bytes = responseAsString.getBytes();
    ByteBuf newBuffer = Unpooled.wrappedBuffer(bytes);
    HttpResponse response =
        new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, newBuffer);
    HttpUtil.setContentLength(response, newBuffer.readableBytes());
    response.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType);
    return response;
  }

}
