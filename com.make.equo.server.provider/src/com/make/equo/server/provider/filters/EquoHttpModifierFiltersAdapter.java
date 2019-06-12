package com.make.equo.server.provider.filters;

import java.util.List;

import com.make.equo.server.offline.api.IEquoOfflineServer;
import com.make.equo.server.offline.api.filters.IModifiableResponse;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

public class EquoHttpModifierFiltersAdapter extends EquoHttpFiltersAdapter {

	private List<String> equoContributionsJsApis;
	private List<String> equoContributionStyles;
	private String customJsScripts;
	private String customStyles;

	public EquoHttpModifierFiltersAdapter(HttpRequest originalRequest, List<String> equoContributionsJsApis, List<String> equoContributionStyles,
			String customJsScripts, String customStyles, boolean isOfflineCacheSupported, IEquoOfflineServer equoOfflineServer) {
		super(originalRequest, equoOfflineServer, isOfflineCacheSupported);
		this.equoContributionsJsApis = equoContributionsJsApis;
		this.equoContributionStyles = equoContributionStyles;
		this.customJsScripts = customJsScripts;
		this.customStyles = customStyles;
	}

	@Override
	public HttpObject serverToProxyResponse(HttpObject httpObject) {
		if (httpObject instanceof FullHttpResponse
				&& ((FullHttpResponse) httpObject).getStatus().code() == HttpResponseStatus.OK.code()) {
			FullHttpResponse fullResponse = (FullHttpResponse) httpObject;
			IModifiableResponse fullHttpResponseWithTransformersScripts = new FullHttpResponseWithTransformersScripts(
					fullResponse, equoContributionsJsApis, equoContributionStyles, customJsScripts, customStyles);
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
