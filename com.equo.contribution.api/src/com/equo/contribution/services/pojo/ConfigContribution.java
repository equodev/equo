package com.equo.contribution.services.pojo;

import java.util.List;
import java.util.Map;

public class ConfigContribution {

	private String contributionName;
	private String contributionHtmlName;
	private List<String> proxiedUris;
	private List<String> contributedScripts;
	private Map<String, String> pathsWithScripts;

	public String getContributionName() {
		return contributionName;
	}

	public void setContributionName(String contributionName) {
		this.contributionName = contributionName;
	}

	public String getContributionHtmlName() {
		return contributionHtmlName;
	}

	public void setContributionHtmlName(String contributionHtmlName) {
		this.contributionHtmlName = contributionHtmlName;
	}

	public List<String> getProxiedUris() {
		return proxiedUris;
	}

	public void setProxiedUris(List<String> proxiedUris) {
		this.proxiedUris = proxiedUris;
	}

	public List<String> getContributedScripts() {
		return contributedScripts;
	}

	public void setContributedScripts(List<String> contributedScripts) {
		this.contributedScripts = contributedScripts;
	}

	public Map<String, String> getPathsWithScripts() {
		return pathsWithScripts;
	}

	public void setPathsWithScripts(Map<String, String> pathsWithScripts) {
		this.pathsWithScripts = pathsWithScripts;
	}

	public boolean isEmpty() {
		return ((this.getContributionName() == null) && (this.getContributionHtmlName() == null)
				&& (this.getProxiedUris() == null) && (this.getContributedScripts() == null)
				&& (this.getPathsWithScripts() == null));
	}

}
