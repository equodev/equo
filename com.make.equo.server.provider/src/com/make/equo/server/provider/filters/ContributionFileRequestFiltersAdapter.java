package com.make.equo.server.provider.filters;

import java.net.URL;

import com.make.equo.server.contribution.resolvers.IEquoContributionUrlResolver;
import com.make.equo.server.offline.api.filters.IModifiableResponse;
import com.make.equo.server.offline.api.filters.OfflineRequestFiltersAdapter;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public class ContributionFileRequestFiltersAdapter extends OfflineRequestFiltersAdapter implements IModifiableResponse {

	private String contributionBaseUri;
	protected IEquoContributionUrlResolver urlResolver;

	public ContributionFileRequestFiltersAdapter(HttpRequest originalRequest, IEquoContributionUrlResolver urlResolver,
			String contributionBaseUri) {
		super(originalRequest);
		this.urlResolver = urlResolver;
		this.contributionBaseUri = contributionBaseUri;
	}

	@Override
	public HttpResponse clientToProxyRequest(HttpObject httpObject) {
		String requestUri = originalRequest.getUri();
		String fileName = requestUri.substring(requestUri.indexOf(contributionBaseUri) + contributionBaseUri.length(), requestUri.length());
		URL resolvedUrl = urlResolver.resolve(fileName);
		return super.buildHttpResponse(resolvedUrl);
	}

	@Override
	public FullHttpResponse getOriginalFullHttpResponse() {
		return null;
	}

	@Override
	public boolean isModifiable() {
		return true;
	}

	@Override
	public String modifyOriginalResponse(String responseToTransform) {
		return null;
	}
}
