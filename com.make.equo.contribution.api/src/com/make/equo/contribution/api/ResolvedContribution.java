package com.make.equo.contribution.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResolvedContribution {

	private List<String> scripts;
	private List<String> styles;
	private Map<String, String> urlsToScripts;
	private Map<String, String> urlsToStyles;

	public ResolvedContribution(List<String> scripts, List<String> styles, Map<String, String> urlsToScripts,
			Map<String, String> urlsToStyles) {
		this.scripts = scripts;
		this.styles = styles;
		this.urlsToScripts = urlsToScripts;
		this.urlsToStyles = urlsToStyles;
	}

	public List<String> getScripts() {
		return new ArrayList<String>(scripts);
	}

	public List<String> getStyles() {
		return new ArrayList<String>(styles);
	}

	public Map<String, String> getUrlsToScripts() {
		return new HashMap<String, String>(urlsToScripts);
	}

	public Map<String, String> getUrlsToStyles() {
		return new HashMap<String, String>(urlsToStyles);
	}

	public String getCustomScripts(String url) {
		return resourcesOrEmptyString(urlsToScripts.get(url));
	}

	public String getCustomStyles(String url) {
		return resourcesOrEmptyString(urlsToStyles.get(url));
	}

	private String resourcesOrEmptyString(String resources) {
		if (resources != null) {
			return resources;
		}
		return "";
	}

}
