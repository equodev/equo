package com.make.equo.analytics.client.provider;

import java.net.URL;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.make.equo.analytics.client.api.IAnalyticsApi;
import com.make.equo.contribution.api.IEquoContribution;

@Component(name = "equoAnalyticsContribution", property = { "type=analyticsContribution" })
public class AnalyticsApiContributionImpl implements IEquoContribution {

	private static final String analyticsJsApi = "analyticsApi.js";
	@SuppressWarnings("unused")
	private IAnalyticsApi analyticsApi;

	@Override
	public URL getJavascriptAPIResource() {
		return this.getClass().getClassLoader().getResource(analyticsJsApi);
	}

	@Override
	public Map<String, Object> getProperties() {
		return null;
	}

	@Override
	public boolean containsJavascriptApi() {
		return true;
	}

	@Reference
	void setAnalyticsApi(IAnalyticsApi analyticsApi) {
		this.analyticsApi = analyticsApi;
	}

	void unsetAnalyticsApi(IAnalyticsApi analyticsApi) {
		this.analyticsApi = null;
	}

}
