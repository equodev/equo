package com.make.equo.server.provider.filters;

import org.littleshoot.proxy.HttpFiltersAdapter;

import com.make.equo.contribution.api.handler.IFiltersAdapterHandler;
import com.make.equo.contribution.api.resolvers.EquoGenericURLResolver;
import com.make.equo.server.provider.EquoHttpProxyContribution;

import io.netty.handler.codec.http.HttpRequest;

public class ContributionJsAdapterHandler implements IFiltersAdapterHandler {

	@Override
	public HttpFiltersAdapter getFiltersAdapter(HttpRequest request) {
		String loggerLevel = System.getProperty("logger.level", "INFO").toUpperCase();
		return new ContributionJsFilterAdapter(request,
				new EquoGenericURLResolver(EquoHttpProxyContribution.class.getClassLoader()), "Logger." + loggerLevel);
	}

}