/****************************************************************************
**
** Copyright (C) 2021 Equo
**
** This file is part of Equo Framework.
**
** Commercial License Usage
** Licensees holding valid commercial Equo licenses may use this file in
** accordance with the commercial license agreement provided with the
** Software or, alternatively, in accordance with the terms contained in
** a written agreement between you and Equo. For licensing terms
** and conditions see https://www.equoplatform.com/terms.
**
** GNU General Public License Usage
** Alternatively, this file may be used under the terms of the GNU
** General Public License version 3 as published by the Free Software
** Foundation. Please review the following
** information to ensure the GNU General Public License requirements will
** be met: https://www.gnu.org/licenses/gpl-3.0.html.
**
****************************************************************************/

package com.equo.server.provider;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.littleshoot.proxy.DefaultHostResolver;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.littleshoot.proxy.mitm.RootCertificateException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ServiceScope;

import com.equo.aer.api.IEquoLoggingService;
import com.equo.contribution.api.handler.IEquoContributionRequestHandler;
import com.equo.logging.client.api.Logger;
import com.equo.logging.client.api.LoggerFactory;
import com.equo.server.api.IEquoServer;
import com.equo.server.offline.api.IEquoOfflineServer;
import com.equo.server.offline.api.filters.IHttpRequestFilter;
import com.equo.server.provider.filters.EquoHttpFiltersSourceAdapter;

/**
 * Server implementation. Manages the proxy lifecycle.
 */
@Component(scope = ServiceScope.SINGLETON)
public class EquoHttpProxyServer implements IEquoServer {
  protected static final Logger logger = LoggerFactory.getLogger(EquoHttpProxyServer.class);

  private static final List<String> proxiedUrls = new ArrayList<String>();

  private static boolean enableOfflineCache = false;

  private static volatile HttpProxyServer proxyServer;
  private ScheduledExecutorService internetConnectionChecker;

  @Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.STATIC)
  private volatile IEquoOfflineServer equoOfflineServer;

  @Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.STATIC)
  private volatile IEquoLoggingService equoLoggingService;

  @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
  private IEquoContributionRequestHandler contributionRequestHandler;

  private static EquoHttpProxyServer equoHttpProxyServer = null;
  private EquoHttpFiltersSourceAdapter httpFiltersSourceAdapter;
  private CustomHostNameMitmManager customHostNameMitmManager = null;

  @Override
  @Activate
  public void start() {
    if (proxyServer == null) {
      startServer();
    }
  }

  @Override
  public void startServer() {
    equoHttpProxyServer = this;
    httpFiltersSourceAdapter = new EquoHttpFiltersSourceAdapter(contributionRequestHandler,
        equoOfflineServer, proxiedUrls);

    if (!isInternetReachable()) {
      httpFiltersSourceAdapter.setConnectionLimited();
    }

    int port = getPortForServer();

    final String pacFileContent = createPacFileContent(port);
    final String base64PacFile = base64Encode(pacFileContent);

    System
        .setProperty("swt.chromium.args",
        "--proxy-pac-url=data:application/x-javascript-config\\;base64," + base64PacFile
            + ";--allow-running-insecure-content;--allow-file-access-from-files;"
            + "--disable-web-security;--enable-widevine-cdm;--proxy-bypass-list=<-loopback>;"
            + "--enable-media-stream;"
            + "--ignore-certificate-errors");

    DefaultHostResolver serverResolver = new DefaultHostResolver() {
      @Override
      public InetSocketAddress resolve(String host, int port) throws UnknownHostException {
        if (!isInternetReachable()) { // <- enable offline
          return new InetSocketAddress(host, port);
        }
        return super.resolve(host, port);
      }
    };

    try {
      customHostNameMitmManager = new CustomHostNameMitmManager(false);
      proxyServer = DefaultHttpProxyServer.bootstrap().withPort(port).withTransparent(false)
          .withFiltersSource(httpFiltersSourceAdapter).withServerResolver(serverResolver)
          .withManInTheMiddle(customHostNameMitmManager).start();
    } catch (RootCertificateException e) {
      e.printStackTrace();
    }
  }

  private static String base64Encode(String value) {
    try {
      return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8.toString()));
    } catch (UnsupportedEncodingException ex) {
      throw new RuntimeException(ex);
    }
  }

  private String createPacFileContent(int port) {
    return "function FindProxyForURL(url, host)\n"
      + "{\n"
      + "    if (url.substring(0, 3) === \"ws:\" || url.substring(0, 4) === \"wss:\")\n"
      + "    {\n"
      + "        return \"DIRECT\";\n"
      + "    } else {\n"
      + "        return \"PROXY localhost:" + port + "\";\n"
      + "    }\n"
      + "}\n";
  }

  private int getPortForServer() {
    int port = 9896;
    try {
      ServerSocket socketPortReserve = new ServerSocket(0);
      socketPortReserve.setReuseAddress(true);
      port = socketPortReserve.getLocalPort();
      socketPortReserve.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return port;
  }

  /**
   * Stops the proxy when the server is deactivated.
   */
  @Deactivate
  public void stop() {
    logger.info("Stopping proxy...");
    if (internetConnectionChecker != null) {
      internetConnectionChecker.shutdownNow();
    }
    if (proxyServer != null) {
      proxyServer.stop();
    }
  }

  @Override
  public void addUrl(String url) {
    if (!proxiedUrls.contains(url)) {
      proxiedUrls.add(url);
    } else if (equoLoggingService != null) {
      equoLoggingService.logWarning("The url " + url + " was already added to the Proxy server.");
    }
  }

  public static boolean isOfflineCacheSupported() {
    return (equoHttpProxyServer == null) ? false
        : equoHttpProxyServer.isOfflineServerSupported() && enableOfflineCache;
  }

  private boolean isOfflineServerSupported() {
    return equoOfflineServer != null;
  }

  @Override
  public void enableOfflineCache() {
    EquoHttpProxyServer.enableOfflineCache = true;
    if (isOfflineCacheSupported()) {
      equoOfflineServer.setProxiedUrls(proxiedUrls);
    }

    Runnable internetConnectionRunnable = new Runnable() {
      @Override
      public void run() {
        if (!isInternetReachable()) {
          httpFiltersSourceAdapter.setConnectionLimited();
        } else {
          httpFiltersSourceAdapter.setConnectionUnlimited();
        }
      }
    };

    internetConnectionChecker = Executors.newSingleThreadScheduledExecutor();
    internetConnectionChecker.scheduleAtFixedRate(internetConnectionRunnable, 0, 5,
        TimeUnit.SECONDS);
  }

  @Override
  public void addOfflineSupportFilter(IHttpRequestFilter httpRequestFilter) {
    if (isOfflineCacheSupported()) {
      equoOfflineServer.addHttpRequestFilter(httpRequestFilter);
    }
  }

  private boolean isInternetReachable() {
    if (proxiedUrls.isEmpty()) {
      return isAddressReachable("http://google.com");
    }
    return isAddressReachable(proxiedUrls.get(0));
  }

  @Override
  public boolean isAddressReachable(String appUrl) {
    try (Socket socket = new Socket()) {
      URI uri = new URI(appUrl);
      String host = uri.getHost();
      socket.connect(new InetSocketAddress(host, 80));
      return true;
    } catch (IOException | URISyntaxException e) {
      return false; // Either timeout or unreachable or failed DNS lookup.
    }
  }

  @Override
  public void setTrust(boolean trustAllServers) {
    if (customHostNameMitmManager != null) {
      try {
        customHostNameMitmManager.changeTrust(trustAllServers);
      } catch (GeneralSecurityException | IOException e) {
        logger.error("Error changing SSL trust", e);
      }
    }
  }
}
