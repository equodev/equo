package com.make.equo.logging.client.core.provider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.make.equo.contribution.api.EquoContributionBuilder;
import com.make.equo.contribution.api.resolvers.EquoGenericURLResolver;

@Component
public class LoggingApiContribution {

	private static final String LOGGING_CONTRIBUTION_NAME = "equologging";
	private static final String LOGGING_JS_API = "loggingApi.js";

	private EquoContributionBuilder builder;

	@Activate
	protected void activate() {
		builder.withContributionName(LOGGING_CONTRIBUTION_NAME).withScriptFile(LOGGING_JS_API)
				.withURLResolver(new EquoGenericURLResolver(LoggingApiContribution.class.getClassLoader())).build();
	}

	@Reference
	void setEquoBuilder(EquoContributionBuilder builder) {
		this.builder = builder;
	}

	void unsetEquoBuilder(EquoContributionBuilder builder) {
		this.builder = null;
	}
}
