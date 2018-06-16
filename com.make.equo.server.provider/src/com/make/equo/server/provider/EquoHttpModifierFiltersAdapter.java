package com.make.equo.server.provider;

import java.util.List;

import com.make.equo.server.offline.api.IEquoOfflineServer;
import com.make.equo.server.offline.api.filters.IModifiableResponse;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

public class EquoHttpModifierFiltersAdapter extends EquoHttpFiltersAdapter {

	private List<String> equoFrameworkJsApis;
	private List<String> equoContributionsJsApis;
	private String customJsScripts;

	public EquoHttpModifierFiltersAdapter(HttpRequest originalRequest, List<String> equoFrameworkJsApis,
			List<String> equoContributionsJsApis, String customJsScripts, boolean isOfflineCacheSupported,
			IEquoOfflineServer equoOfflineServer) {
		super(originalRequest, equoOfflineServer, isOfflineCacheSupported);
		this.equoFrameworkJsApis = equoFrameworkJsApis;
		this.equoContributionsJsApis = equoContributionsJsApis;
		this.customJsScripts = customJsScripts;

	}

	@Override
	public HttpObject serverToProxyResponse(HttpObject httpObject) {
		if (httpObject instanceof FullHttpResponse
				&& ((FullHttpResponse) httpObject).getStatus().code() == HttpResponseStatus.OK.code()) {
			FullHttpResponse fullResponse = (FullHttpResponse) httpObject;
			IModifiableResponse fullHttpResponseWithTransformersScripts = new FullHttpResponseWithTransformersScripts(
					fullResponse, equoFrameworkJsApis, equoContributionsJsApis, customJsScripts);
			if (fullHttpResponseWithTransformersScripts.isModifiable()) {
				FullHttpResponse generatedModifiedResponse = fullHttpResponseWithTransformersScripts
						.generateModifiedResponse();
				saveRequestResponseIfPossible(originalRequest, generatedModifiedResponse);
				return generatedModifiedResponse;
			}
		}
		return super.serverToProxyResponse(httpObject);
	}

}
