package com.make.equo.contribution.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.littleshoot.proxy.HttpFiltersAdapter;

import com.make.equo.contribution.api.handler.IFiltersAdapterHandler;
import com.make.equo.contribution.api.resolvers.IEquoContributionUrlResolver;
import com.make.equo.server.offline.api.filters.IHttpRequestFilter;

import io.netty.handler.codec.http.HttpRequest;

public class EquoContribution {

	private final IEquoContributionManager manager;
	private final IEquoContributionUrlResolver urlResolver;
	private final IFiltersAdapterHandler filtersAdapterHandler;

	private final String contributedHtmlName;
	private final String contributionName;

	private final List<String> proxiedUris;
	private final List<String> contributedScripts;
	private final List<String> contributedStyles;
	private final List<String> excludedResources;

	private final Map<String, String> pathsToScripts;
	private final Map<String, String> pathsToStyles;

	private final IHttpRequestFilter filter;

	private final Runnable runnableAtStart;

	public EquoContribution(IEquoContributionManager manager, IEquoContributionUrlResolver urlResolver,
			IFiltersAdapterHandler filtersAdapterHandler, String contributedHtmlName, String contributionName,
			List<String> proxiedUris, List<String> contributedScripts, List<String> contributedStyles,
			List<String> excludedResources, Map<String, String> pathsToScripts, Map<String, String> pathsToStyles,
			IHttpRequestFilter filter, Runnable runnableAtStart) {
		this.manager = manager;
		this.urlResolver = urlResolver;
		this.filtersAdapterHandler = filtersAdapterHandler;
		this.contributedHtmlName = contributedHtmlName;
		this.contributionName = contributionName.toLowerCase();
		this.proxiedUris = proxiedUris;
		this.contributedScripts = contributedScripts;
		this.contributedStyles = contributedStyles;
		this.excludedResources = excludedResources;
		this.pathsToScripts = pathsToScripts;
		this.pathsToStyles = pathsToStyles;
		this.filter = filter;
		this.runnableAtStart = runnableAtStart;
		addToManager();
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

	public List<String> getContributedStyles() {
		return new ArrayList<String>(contributedStyles);
	}

	public List<String> getExcludedResources() {
		return new ArrayList<String>(excludedResources);
	}

	public Map<String, String> getPathsToScripts() {
		return new HashMap<String, String>(pathsToScripts);
	}

	public Map<String, String> getPathsToStyles() {
		return new HashMap<String, String>(pathsToStyles);
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
		return urlResolver.accepts(request, requestUri);
	}

	private void addToManager() {
		if (manager != null) {
			manager.addContribution(this);
		}
	}

	public Runnable getRunnableAtStart() {
		return runnableAtStart;
	}

}
