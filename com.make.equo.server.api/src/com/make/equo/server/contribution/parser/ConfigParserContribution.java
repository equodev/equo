package com.make.equo.server.contribution.parser;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;


import com.make.equo.server.contribution.EquoContributionBuilder;
import com.make.equo.server.contribution.resolvers.EquoGenericURLResolver;

@Component
public class ConfigParserContribution {

	private static final String LOGGING_CONTRIBUTION_NAME = "equoconfigparser";

	@SuppressWarnings("unused")
	private EquoConfigParser equoConfigParser;

	private EquoContributionBuilder builder;

	@Activate
	protected void activate() {
		builder.withContributionName(LOGGING_CONTRIBUTION_NAME)
				.withURLResolver(new EquoGenericURLResolver(ConfigParserContribution.class.getClassLoader())).build();
	}

	@Reference
	void setEquoBuilder(EquoContributionBuilder builder) {
		this.builder = builder;
	}

	void unsetEquoBuilder(EquoContributionBuilder builder) {
		this.builder = null;
	}
	@Reference
	public void setEquoConfigParser(EquoConfigParser configParser) {
		this.equoConfigParser = configParser;
	}

	public void unsetEquoConfigParser(EquoConfigParser configParser) {
		this.equoConfigParser = null;
	}
	
	
	
}
