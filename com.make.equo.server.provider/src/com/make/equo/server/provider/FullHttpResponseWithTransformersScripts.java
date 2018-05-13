package com.make.equo.server.provider;

import com.make.equo.server.offline.api.filters.IModifiableResponse;

import io.netty.handler.codec.http.FullHttpResponse;

public class FullHttpResponseWithTransformersScripts implements IModifiableResponse {

	private FullHttpResponse originalFullHttpResponse;
	private String equoFrameworkJsApi;
	private String equoWebsocketsApi;
	private String customJsScripts;

	public FullHttpResponseWithTransformersScripts(FullHttpResponse originalFullHttpResponse, String equoFrameworkJsApi,
			String equoWebsocketsApi, String customJsScripts) {
		this.originalFullHttpResponse = originalFullHttpResponse;
		this.equoFrameworkJsApi = equoFrameworkJsApi;
		this.equoWebsocketsApi = equoWebsocketsApi;
		this.customJsScripts = customJsScripts;
	}

	@Override
	public FullHttpResponse getOriginalFullHttpResponse() {
		return originalFullHttpResponse;
	}

	@Override
	public boolean isModifiable() {
		String contentTypeHeader = originalFullHttpResponse.headers().get("Content-Type");
		return contentTypeHeader != null && contentTypeHeader.startsWith("text/");
	}

	@Override
	public String modifyOriginalResponse(String responseToTransform) {
		StringBuilder customResponseWithScripts = new StringBuilder(responseToTransform);
		customResponseWithScripts.append(equoWebsocketsApi);
		customResponseWithScripts.append(equoFrameworkJsApi);
		customResponseWithScripts.append(customJsScripts);
		return customResponseWithScripts.toString();
	}

}
