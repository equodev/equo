package com.make.equo.contribution.media.provider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.make.equo.server.contribution.EquoContributionBuilder;

@Component
public class EquoMediaApiContributionImpl {

	private static final String MEDIA_CONTRIBUTION_NAME = "equomedia";
	private static final String MEDIA_JS_API = "media.js";

	private EquoContributionBuilder builder;

	@Activate
	protected void activate() {
		builder
			.withContributionName(MEDIA_CONTRIBUTION_NAME)
			.withScriptFile(MEDIA_JS_API)
			.withURLResolver(new MediaContributionURLResolver())
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
