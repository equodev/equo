package com.make.equo.server.contribution;

import java.util.ArrayList;
import java.util.List;

import org.littleshoot.proxy.HttpFiltersAdapter;

import com.make.equo.server.api.IEquoServer;
import com.make.equo.server.offline.api.filters.IHttpRequestFilter;
import com.make.equo.server.offline.api.resolvers.ILocalUrlResolver;

import io.netty.handler.codec.http.HttpRequest;

public class EquoContribution {

	private IEquoServer server = null;
	
	private String contributedResourceName = "index.html";
	private String contributionBaseUri;

	private List<String> contributedUris;
	private List<String> contributedScripts;
	private List<String> excludedResources;

	private IHttpRequestFilter filter = ((originalRequest) -> {
		return originalRequest;
	});

	private ILocalUrlResolver urlResolver = null;
	private HttpFiltersAdapter filtersAdapter = HttpFiltersAdapter.NOOP_FILTER;

	public EquoContribution() {
		this.contributedScripts = new ArrayList<String>();
		this.excludedResources = new ArrayList<String>();
		this.contributedUris = new ArrayList<String>();
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

	void setContributionUri(String contributionUri) {
		this.contributionBaseUri = contributionUri;
	}

	public List<String> getContributedScripts() {
		return contributedScripts;
	}

	public void addContributedScript(String script) {
		if (!this.contributedScripts.contains(script)) {
			if (this.server != null) {
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
			if (this.server != null) {
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
		return this.filtersAdapter != HttpFiltersAdapter.NOOP_FILTER;
	}
	
	public HttpFiltersAdapter getFiltersAdapter(HttpRequest originalRequest) {
		return filtersAdapter;
	}

	public void setFiltersAdapter(HttpFiltersAdapter filtersAdapter) {
		this.filtersAdapter = filtersAdapter;
	}
	
	public boolean startContributing() {
		if (this.server != null) {
			this.server.addContribution(this);
			return true;
		}
		return false;
	}

}
