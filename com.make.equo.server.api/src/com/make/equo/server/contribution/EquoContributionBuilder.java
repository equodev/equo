package com.make.equo.server.contribution;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import com.make.equo.server.api.IEquoServer;
import com.make.equo.server.offline.api.filters.IHttpRequestFilter;
import com.make.equo.server.offline.api.resolvers.ILocalUrlResolver;

@Component(scope = ServiceScope.PROTOTYPE, service = EquoContributionBuilder.class)
public class EquoContributionBuilder {

	public static Integer CONTRIBUTION_COUNT = 0;
	public static String DEFAULT_CONTRIBUTION_URI = "http://equoContribution";

	private EquoContribution contribution;

	public EquoContributionBuilder() {
		this.contribution = new EquoContribution();
		contribution.setContributionBaseUri(DEFAULT_CONTRIBUTION_URI + CONTRIBUTION_COUNT + "/");
		CONTRIBUTION_COUNT = CONTRIBUTION_COUNT + 1;
	}
	
	public static EquoContributionBuilder createContribution() {
		EquoContributionBuilder builder = new EquoContributionBuilder();
		return builder;
	}
	
	public EquoContributionBuilder withServer(IEquoServer server) {
		this.contribution.setServer(server);
		return this;
	}
	
	public EquoContributionBuilder withContributionBaseUri(String contributionBaseUri) {
		this.contribution.setContributionBaseUri(contributionBaseUri);
		return this;
	}
	
	public EquoContributionBuilder withContributedResource(String contributedResourceName) {
		this.contribution.setContributedResourceName(contributedResourceName);
		return this;
	}
	
	public EquoContributionBuilder withScriptFile(String script) {
		this.contribution.addContributedScript(script);
		return this;
	}
	
	public EquoContributionBuilder withScriptFiles(List<String> scripts) {
		this.contribution.setContributedScripts(scripts);
		return this;
	}
	
	public EquoContributionBuilder withURLResolver(ILocalUrlResolver urlResolver) {
		this.contribution.setUrlResolver(urlResolver);
		return this;
	}
	
	public EquoContributionBuilder withFilter(IHttpRequestFilter filter) {
		this.contribution.setFilter(filter);
		return this;
	}
	
	public EquoContributionBuilder withFiltersAdapterHandler(IFiltersAdapterHandler filtersAdapterHandler) {
		this.contribution.setFiltersAdapterHandler(filtersAdapterHandler);
		return this;
	}
	
	public EquoContribution build() {
		return this.contribution;
	}
}
