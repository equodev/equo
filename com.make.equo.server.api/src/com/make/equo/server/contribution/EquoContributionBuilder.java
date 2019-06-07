package com.make.equo.server.contribution;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

import com.make.equo.server.api.IEquoServer;
import com.make.equo.server.offline.api.filters.IHttpRequestFilter;
import com.make.equo.server.offline.api.resolvers.ILocalUrlResolver;

/**
 * 
 * Serves as a builder for any external EquoContribution.
 * It is recommended to use OSGi Declarative Services to get an instance of this class to build their own contribution.
 * 
 * All resource/file handling is done using the contribution's URL resolver so defining one is required.
 * 
 */
@Component(scope = ServiceScope.PROTOTYPE, service = EquoContributionBuilder.class)
public class EquoContributionBuilder {

	public static Integer CONTRIBUTION_COUNT = 0;
	public static String DEFAULT_CONTRIBUTION_URI = "http://equocontribution";

	@Reference
	private IEquoServer server;

	private ILocalUrlResolver urlResolver;
	private IFiltersAdapterHandler filtersAdapterHandler;

	private String contributedHtmlName;
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
		this.contributedHtmlName = "";
		CONTRIBUTION_COUNT = CONTRIBUTION_COUNT + 1;
	}

	/**
	 * Changes the IEquoServer of the contribution. Can be used to add the server outside of OSGi services life-cycle.
	 * 
	 * @param server
	 * @return this
	 */
	public EquoContributionBuilder withServer(IEquoServer server) {
		this.server = server;
		return this;
	}

	
	/**
	 * Defines the base URI of the contribution. Used to resolve resource locations for this contribution.
	 * If not assigned a default value will be used.
	 * 
	 * @param contributionBaseUri URI for this contribution.
	 * @return this
	 */
	public EquoContributionBuilder withContributionBaseUri(String contributionBaseUri) {
		if (contributionBaseUri.endsWith("/")) {
			this.contributionBaseUri = contributionBaseUri.toLowerCase();
		} else {
			this.contributionBaseUri = contributionBaseUri.toLowerCase() + "/";
		}
		return this;
	}

	/**
	 * Adds an URI to the contribution.
	 * These URIs will be proxied by the EquoServer when accessed, adding all contributed scripts to it.
	 * 
	 * @param contributedUri
	 * @return this
	 */
	public EquoContributionBuilder withContributedUri(String contributedUri) {
		this.contributedUris.add(contributedUri);
		return this;
	}
	
	/**
	 * Defines an HTML resource to proxy for the contribution.
	 * A browser accessing this contribution's base URI will be proxied using the file defined here as a base.
	 * 
	 * @param contributedHtmlName Name of the html file to be proxied.
	 * @return this
	 */
	public EquoContributionBuilder withBaseHtmlResource(String contributedHtmlName) {
		this.contributedHtmlName = contributedHtmlName;
		return this;
	}

	
	/**
	 * Adds an script to the contribution. These scripts are global and can be used by other contributions of the framework.
	 * 
	 * @param script Name of the script file to be added.
	 * @return this
	 */
	public EquoContributionBuilder withScriptFile(String script) {
		this.contributedScripts.add(script);
		return this;
	}

	/**
	 * Adds a list of scripts to the contribution. These scripts are global and can be used by other contributions of the framework.
	 * 
	 * @param scripts List of names of the script files to be added.
	 * @return this
	 */
	public EquoContributionBuilder withScriptFiles(List<String> scripts) {
		this.contributedScripts.addAll(scripts);
		return this;
	}

	
	/**
	 * Changes the URL resolver of the contribution. The URL resolver is required by the framework to process file requests to the contribution.
	 * 
	 * @param urlResolver
	 * @return this
	 */
	public EquoContributionBuilder withURLResolver(ILocalUrlResolver urlResolver) {
		this.urlResolver = urlResolver;
		return this;
	}

	/**
	 * Changes the custom filter of the contribution. Default is a no-op filter.
	 * This filter is called before any resource handling is attempted by the framework.
	 * 
	 * @param filter
	 * @return this
	 */
	public EquoContributionBuilder withFilter(IHttpRequestFilter filter) {
		this.filter = filter;
		return this;
	}
	
	/**
	 * Changes the adapter handler of the contribution.
	 * The handler is called by the framework with the original request to be proxied and the request's response is delegated to the adapter returned by this handler.
	 * 
	 * @param filtersAdapterHandler
	 * @return this
	 */
	public EquoContributionBuilder withFiltersAdapterHandler(IFiltersAdapterHandler filtersAdapterHandler) {
		this.filtersAdapterHandler = filtersAdapterHandler;
		return this;
	}
	
	/**
	 * Builds the contribution defined by this builder.
	 * 
	 * @return an EquoContribution instance.
	 */
	public EquoContribution build() {
		return new EquoContribution(server, urlResolver, filtersAdapterHandler, contributedHtmlName,
				contributionBaseUri, contributedUris, contributedScripts, excludedResources, filter);
	}

}
