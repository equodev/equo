package com.make.equo.server.api;

import com.make.equo.server.contribution.ContributionDefinition;
import com.make.equo.server.offline.api.filters.IHttpRequestFilter;

public interface IEquoServer {

	void start();

	void startServer();

	void addCustomScript(String url, String scriptUrl);

	void addUrl(String url);
	
	void addContribution(ContributionDefinition contribution);

	String getLocalScriptProtocol();

	String getBundleScriptProtocol();

	void enableOfflineCache();

	void addOfflineSupportFilter(IHttpRequestFilter httpRequestFilter);

	void addLimitedConnectionPage(String limitedConnectionPagePath);

	String getLocalFileProtocol();

	boolean isAddressReachable(String appUrl);

	String getEquoContributionPath();

}
