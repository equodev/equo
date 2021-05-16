package com.equo.server.api;

import com.equo.server.offline.api.filters.IHttpRequestFilter;

public interface IEquoServer {

	void start();

	void startServer();

	void addUrl(String url);

	void enableOfflineCache();

	void addOfflineSupportFilter(IHttpRequestFilter httpRequestFilter);

	void setTrust(boolean trustAllServers);

	boolean isAddressReachable(String appUrl);

}
