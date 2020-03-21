package com.make.equo.analytics.client.provider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.make.equo.analytics.client.api.IAnalyticsApi;
import com.make.equo.contribution.api.EquoContributionBuilder;
import com.make.equo.contribution.api.resolvers.EquoGenericURLResolver;

@Component
public class AnalyticsApiContribution {

	private static final String ANALYTICS_CONTRIBUTION_NAME = "equoanalytics";
	private static final String ANALYTICS_JS_API = "analyticsApi.js";
	
	@SuppressWarnings("unused")
	private IAnalyticsApi analyticsApi;

	private EquoContributionBuilder builder;
	
	@Activate
	protected void activate() {
		builder
			.withContributionName(ANALYTICS_CONTRIBUTION_NAME)
			.withScriptFile(ANALYTICS_JS_API)
			.withURLResolver(new EquoGenericURLResolver(AnalyticsApiContribution.class.getClassLoader()))
			.build();
	}

	@Reference
	void setEquoBuilder(EquoContributionBuilder builder) {
		this.builder = builder;
	}

	void unsetEquoBuilder(EquoContributionBuilder builder) {
		this.builder = null;
	}
}
