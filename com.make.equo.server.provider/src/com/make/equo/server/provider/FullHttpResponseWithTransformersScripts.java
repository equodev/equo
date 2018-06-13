package com.make.equo.server.provider;

import java.util.List;

import com.make.equo.server.offline.api.filters.IModifiableResponse;

import io.netty.handler.codec.http.FullHttpResponse;

public class FullHttpResponseWithTransformersScripts implements IModifiableResponse {

	private FullHttpResponse originalFullHttpResponse;
	private String equoFrameworkJsApi;
	private List<String> equoContributionsJsApis;
	private String customJsScripts;

	public FullHttpResponseWithTransformersScripts(FullHttpResponse originalFullHttpResponse, String equoFrameworkJsApi,
			List<String> equoContributionsJsApis, String customJsScripts) {
		this.originalFullHttpResponse = originalFullHttpResponse;
		this.equoFrameworkJsApi = equoFrameworkJsApi;
		this.equoContributionsJsApis = equoContributionsJsApis;
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
		for (String jsApi : equoContributionsJsApis) {
			customResponseWithScripts.append(jsApi);
		}
		customResponseWithScripts.append(equoFrameworkJsApi);
		customResponseWithScripts.append(customJsScripts);
		return customResponseWithScripts.toString();
	}
}
