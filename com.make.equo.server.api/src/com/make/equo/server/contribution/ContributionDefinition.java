package com.make.equo.server.contribution;

import java.util.ArrayList;
import java.util.List;

import com.make.equo.server.offline.api.filters.IHttpRequestFilter;
import com.make.equo.server.offline.api.resolvers.ILocalUrlResolver;

public class ContributionDefinition {

	private String contributedResourceName = "index.html";
	private String contributionBaseUri;

	private List<String> contributedUris;
	private List<String> contributedScripts;
	private List<String> excludedResources;

	private IHttpRequestFilter filter = ((originalRequest) -> {
		return originalRequest;
	});

	private ILocalUrlResolver urlResolver = null;

	public ContributionDefinition() {
		this.contributedScripts = new ArrayList<String>();
		this.excludedResources = new ArrayList<String>();
		this.contributedUris = new ArrayList<String>();
	}

	public String getContributedResourceName() {
		return contributedResourceName;
	}

	public void setContributedResourceName(String name) {
		this.contributedResourceName = name;
	}
	
	public String getContributionBaseUri() {
		return contributionBaseUri;
	}

	public void setContributionUri(String contributionUri) {
		this.contributionBaseUri = contributionUri;
	}

	public List<String> getContributedScripts() {
		return contributedScripts;
	}

	public void addContributedScript(String script) {
		if (!this.contributedScripts.contains(script)) {
			this.contributedScripts.add(script);
		}
	}

	public List<String> getExcludedResources() {
		return excludedResources;
	}

	public void addExcludedResource(String excludedResource) {
		if (!this.excludedResources.contains(excludedResource)) {
			this.excludedResources.add(excludedResource);
		}
	}

	public List<String> getContributedUris() {
		return contributedUris;
	}

	public void addUri(String uri) {
		if (!this.contributedUris.contains(uri)) {
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

}
