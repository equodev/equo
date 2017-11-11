package com.make.equo.application.server;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.core.runtime.FileLocator;
import org.littleshoot.proxy.HttpFiltersAdapter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public abstract class LocalRequestFiltersAdapter extends HttpFiltersAdapter {

	public LocalRequestFiltersAdapter(HttpRequest originalRequest) {
		super(originalRequest);
	}

	private HttpResponse buildResponse(ByteBuf buffer, String contentType) {
		HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buffer);
		HttpHeaders.setContentLength(response, buffer.readableBytes());
		HttpHeaders.setHeader(response, HttpHeaders.Names.CONTENT_TYPE, contentType);
		return response;
	}

	protected HttpResponse buildHttpResponse(String fileUrl) {
		try {
			URL url = new URL(fileUrl);
			File localFile = new File(FileLocator.resolve(url).toURI());
			Path filePath = localFile.toPath();
			String mimeType = getMimeType(localFile);
			ByteBuf buffer = Unpooled.wrappedBuffer(Files.readAllBytes(filePath));
			return buildResponse(buffer, mimeType);
		} catch (IOException | URISyntaxException e) {
			// TODO log exception
			e.printStackTrace();
			ByteBuf buffer;
			try {
				buffer = Unpooled.wrappedBuffer("".getBytes("UTF-8"));
				return buildResponse(buffer, "text/html");
			} catch (UnsupportedEncodingException e1) {
				// TODO log exception
				e1.printStackTrace();
				return null;
			}
		}
	}
	
	protected abstract String getMimeType(File localFile);
	
}
