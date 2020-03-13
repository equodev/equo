package com.make.equo.server.api;

import com.make.equo.server.offline.api.filters.IHttpRequestFilter;

public interface IEquoServer {

	void start();

	void startServer();

	void addUrl(String url);

	void enableOfflineCache();

	void addOfflineSupportFilter(IHttpRequestFilter httpRequestFilter);

	void addLimitedConnectionPage(String limitedConnectionPagePath);

	boolean isAddressReachable(String appUrl);

}
