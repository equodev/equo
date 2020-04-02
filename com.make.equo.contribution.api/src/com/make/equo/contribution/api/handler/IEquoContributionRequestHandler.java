package com.make.equo.contribution.api.handler;

import java.util.List;

import org.littleshoot.proxy.HttpFilters;

import com.make.equo.contribution.api.ResolvedContribution;

import io.netty.handler.codec.http.HttpRequest;

public interface IEquoContributionRequestHandler {

	HttpFilters handle(HttpRequest request);

	HttpFilters handleOffline(HttpRequest request);

	ResolvedContribution getResolvedContributions();

	List<String> getContributionProxiedUris();

}
