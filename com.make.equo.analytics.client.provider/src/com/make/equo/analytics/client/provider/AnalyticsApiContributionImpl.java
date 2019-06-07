package com.make.equo.analytics.client.provider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.make.equo.analytics.client.api.IAnalyticsApi;
import com.make.equo.server.contribution.EquoContribution;
import com.make.equo.server.contribution.EquoContributionBuilder;

@Component
public class AnalyticsApiContributionImpl {

	private static final String ANALYTICS_JS_API = "analyticsApi.js";
	
	@SuppressWarnings("unused")
	private IAnalyticsApi analyticsApi;

	private EquoContributionBuilder builder;
	
	private EquoContribution contribution;

	@Activate
	protected void activate() {
		contribution = builder.withScriptFile(ANALYTICS_JS_API)
				.withURLResolver(new AnalyticsURLResolver())
				.build();
		contribution.startContributing();
	}

	@Reference
	void setEquoBuilder(EquoContributionBuilder builder) {
		this.builder = builder;
	}

	void unsetEquoBuilder(EquoContributionBuilder builder) {
		this.builder = null;
	}
}
