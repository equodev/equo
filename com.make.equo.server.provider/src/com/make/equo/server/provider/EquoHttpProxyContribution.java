package com.make.equo.server.provider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.make.equo.server.contribution.EquoContribution;
import com.make.equo.server.contribution.EquoContributionBuilder;

@Component
public class EquoHttpProxyContribution {

	private static final String EQUO_FRAMEWORK_JS_API = "equoFramework.js";
	private static final String JQUERY_JS_API = "https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js";
	private static final String DOM_MODIFIER_JS_API = "domModifier.js";

	private EquoContribution contribution;

	private EquoContributionBuilder builder;

	@Activate
	protected void activate() {
		contribution = builder.withScriptFile(EQUO_FRAMEWORK_JS_API)
				.withScriptFile(JQUERY_JS_API)
				.withURLResolver(new EquoHttpProxyServerURLResolver())
				.build();
		String value = System.getProperty("change_original_html");
		if (value == null || (value != null && Boolean.parseBoolean(value))) {
			contribution.addContributedScript(DOM_MODIFIER_JS_API);
		}
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
