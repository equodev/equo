package com.make.equo.server.contribution;

import java.util.List;

import org.littleshoot.proxy.HttpFiltersAdapter;

import com.make.equo.server.api.IEquoServer;
import com.make.equo.server.offline.api.filters.IHttpRequestFilter;
import com.make.equo.server.offline.api.resolvers.ILocalUrlResolver;

import io.netty.handler.codec.http.HttpRequest;

public class EquoContribution {

	private boolean activeContribution = false;
	
	private IEquoServer server;
	private ILocalUrlResolver urlResolver;
	private IFiltersAdapterHandler filtersAdapterHandler;

	private String contributedResourceName;
	private String contributionBaseUri;

	private List<String> contributedUris;
	private List<String> contributedScripts;
	private List<String> excludedResources;

	private IHttpRequestFilter filter;
	
	public EquoContribution(IEquoServer server, ILocalUrlResolver urlResolver, IFiltersAdapterHandler filtersAdapterHandler,
			String contributedResourceName, String contributionBaseUri, List<String> contributedUris,
			List<String> contributedScripts, List<String> excludedResources, IHttpRequestFilter filter) {
		this.server = server;
		this.urlResolver = urlResolver;
		this.filtersAdapterHandler = filtersAdapterHandler;
		this.contributedResourceName = contributedResourceName;
		this.contributionBaseUri = contributionBaseUri;
		this.contributedUris = contributedUris;
		this.contributedScripts = contributedScripts;
		this.excludedResources = excludedResources;
		this.filter = filter;
		this.startContributing();
	}
		
	public IEquoServer getServer() {
		return this.server;
	}
	
	void setServer(IEquoServer server) {
		this.server = server;
	}
	
	public String getContributedResourceName() {
		return contributedResourceName;
	}

	void setContributedResourceName(String name) {
		this.contributedResourceName = name;
	}

	public String getContributionBaseUri() {
		return contributionBaseUri;
	}

	void setContributionBaseUri(String contributionUri) {
		this.contributionBaseUri = contributionUri;
	}

	public List<String> getContributedScripts() {
		return contributedScripts;
	}

	public void addContributedScript(String script) {
		if (!this.contributedScripts.contains(script)) {
			if (this.server != null && activeContribution) {
				this.server.addScriptToContribution(script, this);
			}
			this.contributedScripts.add(script);
		}
	}
	
	void setContributedScripts(List<String> scripts) {
		this.contributedScripts = scripts;
	}
	
	public List<String> getExcludedResources() {
		return excludedResources;
	}

	public void addExcludedResource(String excludedResource) {
		if (!this.excludedResources.contains(excludedResource)) {
			this.excludedResources.add(excludedResource);
		}
	}
	
	void setExcludedResources(List<String> excludedResources) {
		this.excludedResources = excludedResources;
	}

	public List<String> getContributedUris() {
		return contributedUris;
	}

	public void addUri(String uri) {
		if (!this.contributedUris.contains(uri)) {
			if (this.server != null && activeContribution) {
				this.server.addUrl(uri);
			}
			this.contributedUris.add(uri);
		}
	}

	public IHttpRequestFilter getFilter() {
		return filter;
	}

	public void setFilter(IHttpRequestFilter filter) {
		this.filter = filter;
	}

	public ILocalUrlResolver getUrlResolver() {
		return urlResolver;
	}

	public void setUrlResolver(ILocalUrlResolver urlResolver) {
		this.urlResolver = urlResolver;
	}

	public boolean hasCustomFiltersAdapter() {
		return this.filtersAdapterHandler != null;
	}
	
	public void setFiltersAdapterHandler(IFiltersAdapterHandler filtersAdapterHandler) {
		this.filtersAdapterHandler = filtersAdapterHandler;
	}

	public HttpFiltersAdapter getFiltersAdapter(HttpRequest request) {
		if (filtersAdapterHandler == null) {
			return HttpFiltersAdapter.NOOP_FILTER;
		}
		return filtersAdapterHandler.getFiltersAdapter(request);
	}
	
	/**
	 * Adds the contribution to its server
	 * 
	 * @return true if the contribution was added successfully to the server
	 */
	public boolean startContributing() {
		if (this.server != null) {
			this.server.addContribution(this);
			this.activeContribution = true;
			return true;
		}
		return false;
	}

}
