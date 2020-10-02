package com.make.equo.server.provider.filters;

import java.util.List;

import com.make.equo.contribution.api.ResolvedContribution;
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

	public EquoHttpModifierFiltersAdapter(HttpRequest originalRequest, ResolvedContribution globalContribution,
			IEquoOfflineServer equoOfflineServer) {
		super(originalRequest, equoOfflineServer);
		this.equoContributionsJsApis = globalContribution.getScripts();
		this.equoContributionStyles = globalContribution.getStyles();
		this.customJsScripts = globalContribution.getCustomScripts(originalRequest.getUri());
		this.customStyles = globalContribution.getCustomStyles(originalRequest.getUri());
	}

	@Override
	public HttpObject serverToProxyResponse(HttpObject httpObject) {
		if (httpObject instanceof FullHttpResponse
				&& ((FullHttpResponse) httpObject).getStatus().code() == HttpResponseStatus.OK.code()) {
			FullHttpResponse fullResponse = (FullHttpResponse) httpObject;
			IModifiableResponse fullHttpResponseWithTransformersResources = new FullHttpResponseWithTransformersResources(
					fullResponse, equoContributionsJsApis, equoContributionStyles, customJsScripts, customStyles);
			if (fullHttpResponseWithTransformersResources.isModifiable()) {
				FullHttpResponse generatedModifiedResponse = fullHttpResponseWithTransformersResources
						.generateModifiedResponse();
				saveRequestResponseIfPossible(originalRequest, generatedModifiedResponse);
				return generatedModifiedResponse;
			}
		}
		return super.serverToProxyResponse(httpObject);
	}
}
