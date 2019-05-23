package com.make.equo.server.offline.api.filters;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

import javax.activation.MimetypesFileTypeMap;

import org.littleshoot.proxy.HttpFiltersAdapter;

import com.google.common.io.ByteStreams;
import com.make.equo.server.offline.api.resolvers.ILocalUrlResolver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class OfflineRequestFiltersAdapter extends HttpFiltersAdapter {

	protected ILocalUrlResolver urlResolver;
	private String localFilePathWithProtocol;

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
		String originalFileName = localFilePathWithProtocol.substring(localFilePathWithProtocol.lastIndexOf(protocol));
		String fileWithoutProtocol = originalFileName.replace(protocol, "");
		URL resolvedUrl = urlResolver.resolve(fileWithoutProtocol);
		return buildHttpResponse(resolvedUrl);
	}

	protected HttpResponse buildHttpResponse(URL resolvedUrl) {
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

	protected HttpResponse buildResponse(ByteBuf buffer, String contentType) {
		HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buffer);
		HttpHeaders.setContentLength(response, buffer.readableBytes());
		HttpHeaders.setHeader(response, HttpHeaders.Names.CONTENT_TYPE, contentType);
		return response;
	}

	protected void setLocalFilePathWithProtocol(String localFilePathWithProtocol) {
		this.localFilePathWithProtocol = localFilePathWithProtocol;
	}
}
