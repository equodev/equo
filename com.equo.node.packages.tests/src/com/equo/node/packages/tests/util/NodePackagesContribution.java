package com.equo.node.packages.tests.util;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.equo.contribution.api.EquoContributionBuilder;
import com.equo.contribution.api.resolvers.EquoGenericURLResolver;

@Component
public class NodePackagesContribution {

	@Reference
	private EquoContributionBuilder builder;

	@Activate
	protected void activate() {
		builder //
				.withContributionName("testBundles") //
				.withBaseHtmlResource("index.html") //
				.withPathWithScript("", "index.bundle.js") //
				.withURLResolver(new EquoGenericURLResolver(NodePackagesContribution.class.getClassLoader())) //
				.build();
	}

}
