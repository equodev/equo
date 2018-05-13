package com.make.equo.server.provider;

import com.make.equo.server.offline.api.IEquoOfflineServer;
import com.make.equo.server.offline.api.filters.IModifiableResponse;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

public class EquoHttpModifierFiltersAdapter extends EquoHttpFiltersAdapter {

	private String equoFrameworkJsApi;
	private String equoWebsocketsApi;
	private String customJsScripts;

	public EquoHttpModifierFiltersAdapter(HttpRequest originalRequest, String equoFrameworkJsApi,
			String equoWebsocketsApi, String customJsScripts, boolean isOfflineCacheSupported,
			IEquoOfflineServer equoOfflineServer) {
		super(originalRequest, equoOfflineServer, isOfflineCacheSupported);
		this.equoFrameworkJsApi = equoFrameworkJsApi;
		this.equoWebsocketsApi = equoWebsocketsApi;
		this.customJsScripts = customJsScripts;

	}

	@Override
	public HttpObject serverToProxyResponse(HttpObject httpObject) {
		if (httpObject instanceof FullHttpResponse
				&& ((FullHttpResponse) httpObject).getStatus().code() == HttpResponseStatus.OK.code()) {
			FullHttpResponse fullResponse = (FullHttpResponse) httpObject;
			IModifiableResponse fullHttpResponseWithTransformersScripts = new FullHttpResponseWithTransformersScripts(
					fullResponse, equoFrameworkJsApi, equoWebsocketsApi, customJsScripts);
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
