package com.equo.server.provider.filters;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.littleshoot.proxy.impl.ProxyUtils;

import com.equo.contribution.api.handler.IEquoContributionRequestHandler;
import com.equo.server.offline.api.IEquoOfflineServer;
import com.equo.server.provider.EquoHttpProxyServer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaders.Names;
import io.netty.handler.codec.http.HttpRequest;

/**
 * Adapter that search in contributions if the request should be modified or
 * not. Derives requests to other adapters.
 */
public class EquoHttpFiltersSourceAdapter extends HttpFiltersSourceAdapter {
  private IEquoContributionRequestHandler contributionRequestHandler;
  private IEquoOfflineServer equoOfflineServer;

  private boolean connectionLimited = false;

  private List<String> proxiedUrls;

  /**
   * Parameterized constructor.
   */
  public EquoHttpFiltersSourceAdapter(IEquoContributionRequestHandler contributionRequestHandler,
      IEquoOfflineServer equoOfflineServer, List<String> proxiedUrls) {
    this.contributionRequestHandler = contributionRequestHandler;
    this.equoOfflineServer = equoOfflineServer;
    this.proxiedUrls = proxiedUrls;
  }

  @Override
  public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext clientCtx) {
    if (ProxyUtils.isCONNECT(originalRequest)) {
      return new HttpFiltersAdapter(originalRequest, clientCtx);
    }

    HttpFilters filter = contributionRequestHandler.handle(originalRequest);

    if (filter != null) {
      return filter;
    }

    if (isConnectionLimited()) {
      if (EquoHttpProxyServer.isOfflineCacheSupported()) {
        return equoOfflineServer.getOfflineHttpFiltersAdapter(originalRequest);
      } else {
        return contributionRequestHandler.handleOffline(originalRequest);
      }
    } else {
      Optional<String> url = getRequestedUrl(originalRequest);
      if (url.isPresent() && !isFilteredOutFromProxy(originalRequest)) {
        return new EquoHttpModifierFiltersAdapter(originalRequest,
            contributionRequestHandler.getResolvedContributions(), equoOfflineServer);
      } else {
        return new EquoHttpFiltersAdapter(originalRequest, equoOfflineServer);
      }
    }
  }

  private boolean isFilteredOutFromProxy(HttpRequest originalRequest) {
    String uri = originalRequest.getUri();
    return uri.contains("/shared") || uri.contains("/static");
  }

  private Optional<String> getRequestedUrl(HttpRequest originalRequest) {
    return Stream.concat(contributionRequestHandler.getContributionProxiedUris().stream(),
        proxiedUrls.stream()).filter(url -> containsHeader(url, originalRequest)).findFirst();
  }

  private boolean containsHeader(String url, HttpRequest originalRequest) {
    String host = originalRequest.headers().get(Names.HOST);
    if (host.indexOf(":") != -1) {
      return url.contains(host.substring(0, host.indexOf(":")));
    } else {
      return url.contains(host);
    }
  }

  @Override
  public int getMaximumResponseBufferSizeInBytes() {
    return 20 * 1024 * 1024;
  }

  @Override
  public int getMaximumRequestBufferSizeInBytes() {
    return 20 * 1024 * 1024;
  }

  private boolean isConnectionLimited() {
    return connectionLimited;
  }

  public void setConnectionLimited() {
    connectionLimited = true;
  }

  public void setConnectionUnlimited() {
    connectionLimited = false;
  }
}
