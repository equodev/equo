package com.equo.server.provider.filters;

import org.littleshoot.proxy.HttpFiltersAdapter;

import com.equo.server.offline.api.IEquoOfflineServer;
import com.equo.server.provider.EquoHttpProxyServer;

import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;

/**
 * Simple adapter that caches requests.
 */
public class EquoHttpFiltersAdapter extends HttpFiltersAdapter {

  protected IEquoOfflineServer equoOfflineServer;

  public EquoHttpFiltersAdapter(HttpRequest originalRequest, IEquoOfflineServer equoOfflineServer) {
    super(originalRequest);
    this.equoOfflineServer = equoOfflineServer;
  }

  @Override
  public HttpObject serverToProxyResponse(HttpObject httpObject) {
    saveRequestResponseIfPossible(originalRequest, httpObject);
    return super.serverToProxyResponse(httpObject);
  }

  public IEquoOfflineServer getEquoOfflineServer() {
    return equoOfflineServer;
  }

  /**
   * Save the request to be used when entering offline mode.
   */
  public void saveRequestResponseIfPossible(HttpRequest originalRequest, HttpObject httpObject) {
    if (EquoHttpProxyServer.isOfflineCacheSupported()) {
      getEquoOfflineServer().saveRequestResponse(originalRequest, httpObject);
    }
  }
}
