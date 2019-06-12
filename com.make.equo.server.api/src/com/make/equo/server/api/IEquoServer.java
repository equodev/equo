package com.make.equo.server.api;

import com.make.equo.server.contribution.EquoContribution;
import com.make.equo.server.offline.api.filters.IHttpRequestFilter;

public interface IEquoServer {

	void start();

	void startServer();

	void addCustomScript(String url, String scriptUrl);
	
	void addCustomStyle(String url, String styleUrl);

	void addUrl(String url);
	
	void addContribution(EquoContribution contribution);
	
	void addScriptToContribution(String script);

	String getLocalScriptProtocol();

	String getBundleScriptProtocol();

	String getLocalStyleProtocol();

	String getBundleStyleProtocol();
	
	void enableOfflineCache();

	void addOfflineSupportFilter(IHttpRequestFilter httpRequestFilter);

	void addLimitedConnectionPage(String limitedConnectionPagePath);

	String getLocalFileProtocol();

	boolean isAddressReachable(String appUrl);

	void withBaseHtml(String baseHtmlPathWithPrefix);
}
