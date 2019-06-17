package com.make.equo.server.contribution;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.littleshoot.proxy.HttpFiltersAdapter;

import com.make.equo.server.api.IEquoServer;
import com.make.equo.server.contribution.resolvers.IEquoContributionUrlResolver;
import com.make.equo.server.offline.api.filters.IHttpRequestFilter;

import io.netty.handler.codec.http.HttpRequest;

public class EquoContribution {

	private final IEquoServer server;
	private final IEquoContributionUrlResolver urlResolver;
	private final IFiltersAdapterHandler filtersAdapterHandler;

	private final String contributedHtmlName;
	private final String contributionName;

	private final List<String> proxiedUris;
	private final List<String> contributedScripts;
	private final List<String> excludedResources;

	private final Map<String, String> pathsToScripts;

	private final IHttpRequestFilter filter;

	public EquoContribution(IEquoServer server, IEquoContributionUrlResolver urlResolver,
			IFiltersAdapterHandler filtersAdapterHandler, String contributedHtmlName, String contributionName,
			List<String> proxiedUris, List<String> contributedScripts, List<String> excludedResources,
			Map<String, String> pathsToScripts, IHttpRequestFilter filter) {
		this.server = server;
		this.urlResolver = urlResolver;
		this.filtersAdapterHandler = filtersAdapterHandler;
		this.contributedHtmlName = contributedHtmlName;
		this.contributionName = contributionName;
		this.proxiedUris = proxiedUris;
		this.contributedScripts = contributedScripts;
		this.excludedResources = excludedResources;
		this.pathsToScripts = pathsToScripts;
		this.filter = filter;
	}

	public String getContributedResourceName() {
		return contributedHtmlName;
	}

	public String getContributionName() {
		return contributionName;
	}

	public List<String> getProxiedUris() {
		return new ArrayList<String>(proxiedUris);
	}

	public List<String> getContributedScripts() {
		return new ArrayList<String>(contributedScripts);
	}

	public List<String> getExcludedResources() {
		return new ArrayList<String>(excludedResources);
	}

	public Map<String, String> getPathsToScripts() {
		return new HashMap<String, String>(pathsToScripts);
	}

	public IHttpRequestFilter getFilter() {
		return filter;
	}

	public IEquoContributionUrlResolver getUrlResolver() {
		return urlResolver;
	}

	public boolean hasCustomFiltersAdapter() {
		return filtersAdapterHandler != null;
	}

	public HttpFiltersAdapter getFiltersAdapter(HttpRequest request) {
		if (filtersAdapterHandler == null) {
			return HttpFiltersAdapter.NOOP_FILTER;
		}
		return filtersAdapterHandler.getFiltersAdapter(request);
	}

	public boolean accepts(HttpRequest request, URI requestUri) {
		if (requestUri.getPath() != null && requestUri.getPath().matches(".*\\..+$")) {
			return true;
		}
		return false;
	}

	/**
	 * Adds the contribution to its server
	 * 
	 * @return true if the contribution was added successfully to the server
	 */
	public boolean startContributing() {
		if (server != null) {
			server.addContribution(this);
			return true;
		}
		return false;
	}

}
