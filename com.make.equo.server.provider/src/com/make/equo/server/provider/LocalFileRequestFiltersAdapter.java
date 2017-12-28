package com.make.equo.server.provider;

import java.io.File;

import javax.activation.MimetypesFileTypeMap;

import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public class LocalFileRequestFiltersAdapter extends LocalRequestFiltersAdapter {

	private String basePath;
	private String prefix;

	public LocalFileRequestFiltersAdapter(String basePath, String prefix, HttpRequest originalRequest) {
		super(originalRequest);
		if (basePath.endsWith("/")) {
			this.basePath = basePath;
		} else {
			this.basePath = basePath + "/";
		}
		this.prefix = prefix;
	}

	@Override
	public HttpResponse clientToProxyRequest(HttpObject httpObject) {
		String uri = originalRequest.getUri();
		String originalFileName = uri.substring(uri.lastIndexOf(prefix));
		String absoluteFilePath = basePath + originalFileName.replace(prefix + "/", "");
		String urlAsString = EquoHttpProxyServer.LOCAL_FILE_PROTOCOL + absoluteFilePath.substring(1);
		return buildHttpResponse(urlAsString);
	}

	@Override
	protected String getMimeType(File localFile) {
		final MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
		String contentType = fileTypeMap.getContentType(localFile);
		return contentType;
	}

}
