package com.equo.server.api;

import com.equo.server.offline.api.filters.IHttpRequestFilter;

/**
 * Equo server interface.
 */
public interface IEquoServer {

  void start();

  void startServer();

  void addUrl(String url);

  void enableOfflineCache();

  void addOfflineSupportFilter(IHttpRequestFilter httpRequestFilter);

  void setTrust(boolean trustAllServers);

  boolean isAddressReachable(String appUrl);

}
