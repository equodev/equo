package com.make.equo.server.provider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.make.equo.contribution.api.EquoContributionBuilder;
import com.make.equo.contribution.api.resolvers.EquoGenericURLResolver;
import com.make.equo.server.provider.filters.ContributionJsAdapterHandler;

@Component
public class EquoHttpProxyContribution {

	public static final String PROXY_CONTRIBUTION_NAME = "equoproxy";
	private static final String LOGGER_API = "logger.min.js";
	private static final String JQUERY_JS_API = "https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js";
	private static final String EQUO_FRAMEWORK_JS_API = "equoFramework.js";
	private static final String DOM_MODIFIER_JS_API = "domModifier.js";

	private EquoContributionBuilder builder;

	@Activate
	protected void activate() {
		String value = System.getProperty("change_original_html");
		if (value == null || (value != null && Boolean.parseBoolean(value))) {
			builder.withContributionName(PROXY_CONTRIBUTION_NAME).withScriptFile(LOGGER_API)
					.withScriptFile(EQUO_FRAMEWORK_JS_API).withScriptFile(JQUERY_JS_API)
					.withScriptFile(DOM_MODIFIER_JS_API)
					.withURLResolver(new EquoGenericURLResolver(EquoHttpProxyContribution.class.getClassLoader()))
					.withFiltersAdapterHandler(new ContributionJsAdapterHandler())
					.build();
		} else {
			builder.withContributionName(PROXY_CONTRIBUTION_NAME).withScriptFile(LOGGER_API)
					.withScriptFile(EQUO_FRAMEWORK_JS_API).withScriptFile(JQUERY_JS_API)
					.withURLResolver(new EquoGenericURLResolver(EquoHttpProxyContribution.class.getClassLoader()))
					.withFiltersAdapterHandler(new ContributionJsAdapterHandler())
					.build();
		}
	}

	@Reference
	void setEquoBuilder(EquoContributionBuilder builder) {
		this.builder = builder;
	}

	void unsetEquoBuilder(EquoContributionBuilder builder) {
		this.builder = null;
	}
}
