package com.make.equo.renderers.contributions;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.make.equo.server.contribution.EquoContribution;
import com.make.equo.server.contribution.EquoContributionBuilder;

@Component
public class EquoRenderersContribution {
	
	public static final String EQUO_RENDERERS_BASE_URI = "http://equorenderers/";
	
	private EquoContributionBuilder builder;
	private static EquoContribution contribution;
	
	@Activate
	protected void activate() {
		contribution = builder.withBaseHtmlResource("baseRenderer.html")
			.withContributionBaseUri(EQUO_RENDERERS_BASE_URI)
			.withURLResolver(new EquoRenderersURLResolver())
			.build();
		contribution.startContributing();
	}
	
	public static EquoContribution getContributionDefinition() {
		return contribution;
	}
	
	@Reference
	void setEquoBuilder(EquoContributionBuilder builder) {
		this.builder = builder;
	}
	
	void unsetEquoBuilder(EquoContributionBuilder builder) {
		this.builder = null;
	}
}
