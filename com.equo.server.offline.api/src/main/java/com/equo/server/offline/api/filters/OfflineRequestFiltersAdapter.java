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

package com.equo.server.offline.api.filters;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

import javax.activation.MimetypesFileTypeMap;

import org.littleshoot.proxy.HttpFiltersAdapter;

import com.equo.server.offline.api.resolvers.ILocalUrlResolver;
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
 * Adapter for requests in offline mode.
 */
public class OfflineRequestFiltersAdapter extends HttpFiltersAdapter {

  private static final MimetypesFileTypeMap FILE_TYPE_MAP;

  static {
    FILE_TYPE_MAP = new MimetypesFileTypeMap();
    FILE_TYPE_MAP.addMimeTypes("text/css css");
    FILE_TYPE_MAP.addMimeTypes("application/javascript js");
    FILE_TYPE_MAP.addMimeTypes("application/json json");
    FILE_TYPE_MAP.addMimeTypes("text/xml xml");
  }

  protected ILocalUrlResolver urlResolver;
  private String localFilePathWithProtocol;

  protected OfflineRequestFiltersAdapter(HttpRequest originalRequest) {
    super(originalRequest);
  }

  public OfflineRequestFiltersAdapter(HttpRequest originalRequest, ILocalUrlResolver urlResolver) {
    super(originalRequest);
    this.urlResolver = urlResolver;
  }

  public OfflineRequestFiltersAdapter(HttpRequest originalRequest, ILocalUrlResolver urlResolver,
      String localFilePathWithProtocol) {
    this(originalRequest, urlResolver);
    this.localFilePathWithProtocol = localFilePathWithProtocol;
  }

  @Override
  public HttpResponse clientToProxyRequest(HttpObject httpObject) {
    String protocol = urlResolver.getProtocol().toLowerCase();
    String originalFileName =
        localFilePathWithProtocol.substring(localFilePathWithProtocol.lastIndexOf(protocol));
    String fileWithoutProtocol = originalFileName.replace(protocol, "");
    URL resolvedUrl = urlResolver.resolve(fileWithoutProtocol);
    return buildHttpResponse(resolvedUrl);
  }

  protected HttpResponse buildHttpResponse(URL resolvedUrl) {
    if (resolvedUrl == null) {
      return null;
    }
    try {
      final URLConnection connection = resolvedUrl.openConnection();
      InputStream inputStream = connection.getInputStream();
      byte[] bytes = ByteStreams.toByteArray(inputStream);
      ByteBuf buffer = Unpooled.wrappedBuffer(bytes);
      String fileName = resolvedUrl.getFile().substring(1);
      String contentType = FILE_TYPE_MAP.getContentType(fileName);
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

  protected HttpResponse buildResponse(ByteBuf buffer, String contentType) {
    HttpResponse response =
        new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buffer);
    HttpUtil.setContentLength(response, buffer.readableBytes());
    response.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType);
    return response;
  }

  protected void setLocalFilePathWithProtocol(String localFilePathWithProtocol) {
    this.localFilePathWithProtocol = localFilePathWithProtocol;
  }
}
