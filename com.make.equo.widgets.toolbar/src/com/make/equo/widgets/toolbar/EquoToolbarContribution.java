package com.make.equo.widgets.toolbar;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.make.equo.contribution.api.EquoContributionBuilder;
import com.make.equo.contribution.api.resolvers.EquoGenericURLResolver;

@Component
public class EquoToolbarContribution {
	

	static final String TOOLBAR_CONTRIBUTION_NAME = "equotoolbar";
	private static String TOOLBAR_WEB_COMPONENTS_JS = "equotoolbarwc.js";
	private static final String EQUO_TOOLBAR_JS_API = "equotoolbar.js";
	private static final String VUE_JS = "vue.js";
	private static final String VUE_MATERIAL_MIN_JS = "vue-material.min.js";
	
	private EquoContributionBuilder builder;

	@Activate
	public void start() {
		builder.withScriptFile(VUE_JS).withScriptFile(VUE_MATERIAL_MIN_JS)
				.withScriptFile(EQUO_TOOLBAR_JS_API)
				.withScriptFile(TOOLBAR_WEB_COMPONENTS_JS)
				.withContributionName(TOOLBAR_CONTRIBUTION_NAME)
				.withURLResolver(new EquoGenericURLResolver(EquoToolbarContribution.class.getClassLoader()))
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
