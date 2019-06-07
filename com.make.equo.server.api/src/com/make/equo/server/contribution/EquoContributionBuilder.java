package com.make.equo.server.contribution;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

import com.make.equo.server.api.IEquoServer;
import com.make.equo.server.offline.api.filters.IHttpRequestFilter;
import com.make.equo.server.offline.api.resolvers.ILocalUrlResolver;

@Component(scope = ServiceScope.PROTOTYPE, service = EquoContributionBuilder.class)
public class EquoContributionBuilder {

	public static Integer CONTRIBUTION_COUNT = 0;
	public static String DEFAULT_CONTRIBUTION_URI = "http://equocontribution";

	@Reference
	private IEquoServer server;

	private ILocalUrlResolver urlResolver;
	private IFiltersAdapterHandler filtersAdapterHandler;

	private String contributedResourceName;
	private String contributionBaseUri;

	private List<String> contributedUris;
	private List<String> contributedScripts;
	private List<String> excludedResources;

	private IHttpRequestFilter filter;

	public EquoContributionBuilder() {
		this.contributedUris = new ArrayList<String>();
		this.contributedScripts = new ArrayList<String>();
		this.excludedResources = new ArrayList<String>();
		this.filter = ((originalRequest) -> {
			return originalRequest;
		});
		this.contributionBaseUri = DEFAULT_CONTRIBUTION_URI + CONTRIBUTION_COUNT + "/";
		this.contributedResourceName = "";
		CONTRIBUTION_COUNT = CONTRIBUTION_COUNT + 1;
	}

	public EquoContributionBuilder withServer(IEquoServer server) {
		this.server = server;
		return this;
	}

	public EquoContributionBuilder withContributionBaseUri(String contributionBaseUri) {
		this.contributionBaseUri = contributionBaseUri;
		return this;
	}

	public EquoContributionBuilder withBaseHtmlResource(String contributedResourceName) {
		this.contributedResourceName = contributedResourceName;
		return this;
	}

	public EquoContributionBuilder withScriptFile(String script) {
		this.contributedScripts.add(script);
		return this;
	}

	public EquoContributionBuilder withScriptFiles(List<String> scripts) {
		this.contributedScripts.addAll(scripts);
		return this;
	}

	public EquoContributionBuilder withURLResolver(ILocalUrlResolver urlResolver) {
		this.urlResolver = urlResolver;
		return this;
	}

	public EquoContributionBuilder withFilter(IHttpRequestFilter filter) {
		this.filter = filter;
		return this;
	}

	public EquoContributionBuilder withFiltersAdapterHandler(IFiltersAdapterHandler filtersAdapterHandler) {
		this.filtersAdapterHandler = filtersAdapterHandler;
		return this;
	}

	public EquoContribution build() {
		return new EquoContribution(server, urlResolver, filtersAdapterHandler, contributedResourceName,
				contributionBaseUri.toLowerCase(), contributedUris, contributedScripts, excludedResources, filter);
	}

}
