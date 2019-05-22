package com.make.equo.server.provider.contribution.util;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ServiceScope;

import com.make.equo.server.api.IEquoServer;
import com.make.equo.server.contribution.ContributionDefinition;

@Component(scope = ServiceScope.PROTOTYPE, service = EquoContributionBuilder.class)
public class EquoContributionBuilder {

	public static Integer CONTRIBUTION_COUNT = 0;
	public static String DEFAULT_CONTRIBUTION_URI = "http://equoContribution" + CONTRIBUTION_COUNT;
	private ContributionDefinition contribution;

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
	private IEquoServer equoServer;

	public EquoContributionBuilder() {
		ContributionDefinition contribution = new ContributionDefinition();
		contribution.setContributionUri(DEFAULT_CONTRIBUTION_URI);
		CONTRIBUTION_COUNT++;
	}

	public ContributionDefinition build() {
		equoServer.addContribution(this.contribution);
		return this.contribution;
	}
}
