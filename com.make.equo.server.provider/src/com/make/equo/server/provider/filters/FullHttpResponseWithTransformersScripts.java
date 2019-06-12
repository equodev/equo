package com.make.equo.server.provider.filters;

import java.util.List;

import com.make.equo.server.offline.api.filters.IModifiableResponse;

import io.netty.handler.codec.http.FullHttpResponse;

public class FullHttpResponseWithTransformersScripts implements IModifiableResponse {

	private FullHttpResponse originalFullHttpResponse;
	private List<String> equoContributionsJsApis;
	private List<String> equoContributionStyles;
	private String customJsScripts;
	private String customStyles;

	public FullHttpResponseWithTransformersScripts(FullHttpResponse originalFullHttpResponse,
			List<String> equoContributionsJsApis, List<String> equoContributionStyles, String customJsScripts, String customStyles) {
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
		String contentTypeHeader = originalFullHttpResponse.headers().get("Content-Type");
		return contentTypeHeader != null && contentTypeHeader.startsWith("text/");
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
