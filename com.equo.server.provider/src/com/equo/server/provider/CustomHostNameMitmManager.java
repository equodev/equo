package com.equo.server.provider;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;

import org.littleshoot.proxy.MitmManager;
import org.littleshoot.proxy.mitm.FakeCertificateException;
import org.littleshoot.proxy.mitm.RootCertificateException;
import org.littleshoot.proxy.mitm.SubjectAlternativeNameHolder;

import com.equo.logging.client.api.Logger;
import com.equo.logging.client.api.LoggerFactory;

import io.netty.handler.codec.http.HttpRequest;

/**
 * Manager for proxy man-in-the-middle feature.
 */
public class CustomHostNameMitmManager implements MitmManager {
  protected static final Logger logger = LoggerFactory.getLogger(CustomHostNameMitmManager.class);
  private static final String DEV_APP_URL = "dev.app.url";
  private CustomBouncyCastleSslEngineSource sslEngineSource;
  private static List<String> hosts = new ArrayList<>();

  static {
    try {
      Enumeration<NetworkInterface> n = NetworkInterface.getNetworkInterfaces();
      while (n.hasMoreElements()) {
        NetworkInterface e = n.nextElement();

        Enumeration<InetAddress> a = e.getInetAddresses();
        while (a.hasMoreElements()) {
          InetAddress addr = a.nextElement();
          hosts.add(addr.getHostAddress());
        }
      }
    } catch (SocketException e) {
      logger.error("Error obtaining local network interfaces", e);
    }
  }

  public CustomHostNameMitmManager(boolean trustAllServers) throws RootCertificateException {
    this(new CustomAuthority(), trustAllServers);
  }

  /**
   * Parameterized constructor.
   */
  public CustomHostNameMitmManager(CustomAuthority authority, boolean trustAllServers)
      throws RootCertificateException {
    try {
      boolean sendCerts = true;
      sslEngineSource =
          new CustomBouncyCastleSslEngineSource(authority, trustAllServers, sendCerts);
    } catch (final Exception e) {
      throw new RootCertificateException("Errors during assembling root CA.", e);
    }
  }

  public SSLEngine serverSslEngine(String peerHost, int peerPort) {
    return sslEngineSource.newSslEngine(peerHost, peerPort);
  }

  @Override
  public SSLEngine serverSslEngine() {
    return sslEngineSource.newSslEngine();
  }

  /**
   * Creates a new SSL engine for the request given by parameter.
   */
  public SSLEngine clientSslEngineFor(HttpRequest httpRequest, SSLSession serverSslSession) {
    String serverHostAndPort = httpRequest.getUri();
    try {
      String serverName = serverHostAndPort.split(":")[0];
      SubjectAlternativeNameHolder san = new SubjectAlternativeNameHolder();
      san.addDomainName(serverName);
      return sslEngineSource.createCertForHost(serverName, san);
    } catch (Exception e) {
      throw new FakeCertificateException(
          "Creation dynamic certificate failed for " + serverHostAndPort, e);
    }
  }

  void changeTrust(boolean trustAllServers) throws GeneralSecurityException, IOException {
    sslEngineSource.initializeSslContext(trustAllServers);
  }

  @Override
  public boolean accepts(HttpRequest httpRequest) {
    String developmentUrl = System.getProperty(DEV_APP_URL);
    String uri = httpRequest.getUri();
    if (developmentUrl != null && uri.startsWith(developmentUrl)) {
      return false;
    }
    for (String host : hosts) {
      if (uri.startsWith(host)) {
        return false;
      }
    }
    return true;
  }

}
