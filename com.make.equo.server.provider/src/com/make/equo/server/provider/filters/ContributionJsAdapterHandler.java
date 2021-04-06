package com.make.equo.server.provider.filters;

import java.util.Arrays;
import java.util.List;

import org.littleshoot.proxy.HttpFiltersAdapter;

import com.make.equo.contribution.api.handler.IFiltersAdapterHandler;
import com.make.equo.contribution.api.resolvers.EquoGenericURLResolver;
import com.make.equo.server.provider.EquoHttpProxyContribution;

import io.netty.handler.codec.http.HttpRequest;

import com.make.equo.logging.client.api.Logger;
import com.make.equo.logging.client.api.LoggerFactory;

public class ContributionJsAdapterHandler implements IFiltersAdapterHandler {
	protected static final Logger logger = LoggerFactory.getLogger(ContributionJsAdapterHandler.class);

	private static final List<String> ALLOWED_LEVELS = Arrays.asList("TRACE", "INFO", "DEBUG", "WARN", "ERROR", "TRACE",
			"TIME", "OFF");
	private static final String DEFAULT_LEVEL = "INFO";

	@Override
	public HttpFiltersAdapter getFiltersAdapter(HttpRequest request) {
		String loggerLevel = System.getProperty("logger.level", DEFAULT_LEVEL).toUpperCase();
		if (!ALLOWED_LEVELS.contains(loggerLevel)) {
			logger.warn("Wrong Logger level configured. Default one (" + DEFAULT_LEVEL + ") will be used");
			loggerLevel = DEFAULT_LEVEL;
		}
		return new ContributionJsFilterAdapter(request,
				new EquoGenericURLResolver(EquoHttpProxyContribution.class.getClassLoader()), "Logger." + loggerLevel);
	}

}