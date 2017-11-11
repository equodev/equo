package com.make.equo.application.server;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

import org.eclipse.core.runtime.FileLocator;
import org.littleshoot.proxy.HttpFiltersAdapter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class LocalRequestFiltersAdapater extends HttpFiltersAdapter {

	public LocalRequestFiltersAdapater(HttpRequest originalRequest) {
		super(originalRequest);
	}

	@Override
	public HttpResponse clientToProxyRequest(HttpObject httpObject) {
		String uri = originalRequest.getUri();
		String localFilePath = uri.substring(uri.lastIndexOf(EquoHttpProxyServer.LOCAL_FILE_PROTOCOL));
		System.out.println("sisi local file path is " + localFilePath);
		
		try {
			URL url = new URL(localFilePath);
			File file = new File(FileLocator.resolve(url).toURI());
			String scriptContent = new String(Files.readAllBytes(file.toPath()));
			
			ByteBuf buffer = Unpooled.wrappedBuffer(scriptContent.getBytes("UTF-8"));
			return buildResponse(buffer);
		} catch (IOException | URISyntaxException e) {
			// TODO log exception
			e.printStackTrace();
			ByteBuf buffer;
			try {
				buffer = Unpooled.wrappedBuffer("".getBytes("UTF-8"));
				return buildResponse(buffer);
			} catch (UnsupportedEncodingException e1) {
				// TODO log exception
				e1.printStackTrace();
				return null;
			}
		}
	}

	private HttpResponse buildResponse(ByteBuf buffer) {
		HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buffer);
		HttpHeaders.setContentLength(response, buffer.readableBytes());
		HttpHeaders.setHeader(response, HttpHeaders.Names.CONTENT_TYPE, "application/javascript");
		return response;
	}

}
