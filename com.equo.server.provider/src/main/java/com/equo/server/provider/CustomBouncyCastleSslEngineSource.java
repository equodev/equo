/****************************************************************************
**
** Copyright (C) 2021 Equo
**
** This file is part of the Equo SDK.
**
** Commercial License Usage
** Licensees holding valid commercial Equo licenses may use this file in
** accordance with the commercial license agreement provided with the
** Software or, alternatively, in accordance with the terms contained in
** a written agreement between you and Equo. For licensing terms
** and conditions see https://www.equo.dev/terms.
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;

import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.OperatorCreationException;
import org.littleshoot.proxy.SslEngineSource;
import org.littleshoot.proxy.mitm.MergeTrustManager;
import org.littleshoot.proxy.mitm.RootCertificateException;
import org.littleshoot.proxy.mitm.SubjectAlternativeNameHolder;

import com.equo.logging.client.api.Logger;
import com.equo.logging.client.api.LoggerFactory;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

/**
 * Custom SSL engine to manage SSL contexts.
 */
public class CustomBouncyCastleSslEngineSource implements SslEngineSource {
  private static final Logger LOG =
      LoggerFactory.getLogger(CustomBouncyCastleSslEngineSource.class);

  /**
   * The P12 format has to be implemented by every vendor. Oracles proprietary JKS
   * type is not available in Android.
   */
  private static final String KEY_STORE_TYPE = "PKCS12";

  private static final String KEY_STORE_FILE_EXTENSION = ".p12";

  private final CustomAuthority authority;

  private final boolean sendCerts;

  private SSLContext sslContext;

  private Certificate caCert;

  private PrivateKey caPrivKey;

  private Cache<String, SSLContext> serverSslContexts;

  private Certificate cert = null;

  /**
   * Creates a SSL engine source create a Certificate Authority if needed and
   * initializes a SSL context. Exceptions will be thrown to let the manager
   * decide how to react. Don't install a MITM manager in the proxy in case of a
   * failure.
   * @param authority       a parameter object to provide personal informations of
   *                        the Certificate Authority and the dynamic
   *                        certificates.
   * @param trustAllServers wheter to trust on any SSL cert or not
   * @param sslContexts     a cache to store dynamically created server
   *                        certificates. Generation takes between 50 to 500ms,
   *                        but only once per thread, since there is a connection
   *                        cache too. It's save to give a null cache to prevent
   *                        memory or locking issues.
   */
  public CustomBouncyCastleSslEngineSource(CustomAuthority authority, boolean trustAllServers,
      boolean sendCerts, Cache<String, SSLContext> sslContexts) throws GeneralSecurityException,
      OperatorCreationException, RootCertificateException, IOException {
    this.authority = authority;
    this.sendCerts = sendCerts;
    this.serverSslContexts = sslContexts;
    initializeKeyStore();
    initializeSslContext(trustAllServers);
  }

  /**
   * Creates a SSL engine source, create a Certificate Authority if needed, and
   * initializes a SSL context. This constructor defaults a cache to store
   * dynamically created server certificates. Exceptions will be thrown to let the
   * manager decide how to react. Don't install a MITM manager in the proxy in
   * case of a failure.
   * @param authority       a parameter object to provide personal informations of
   *                        the Certificate Authority and the dynamic
   *                        certificates.
   * @param trustAllServers wheter to trust on any SSL cert or not
   */
  public CustomBouncyCastleSslEngineSource(CustomAuthority authority, boolean trustAllServers,
      boolean sendCerts) throws RootCertificateException, GeneralSecurityException, IOException,
      OperatorCreationException {
    this(authority, trustAllServers, sendCerts, initDefaultCertificateCache());
  }

  private static Cache<String, SSLContext> initDefaultCertificateCache() {
    return CacheBuilder.newBuilder() //
        .expireAfterAccess(5, TimeUnit.MINUTES) //
        .concurrencyLevel(16) //
        .build();
  }

  private void filterWeakCipherSuites(SSLEngine sslEngine) {
    List<String> ciphers = new LinkedList<String>();
    for (String each : sslEngine.getEnabledCipherSuites()) {
      if (each.equals("TLS_DHE_RSA_WITH_AES_128_CBC_SHA")
          || each.equals("TLS_DHE_RSA_WITH_AES_256_CBC_SHA")) {
        LOG.debug("Removed cipher {}", each);
      } else {
        ciphers.add(each);
      }
    }
    sslEngine.setEnabledCipherSuites(ciphers.toArray(new String[ciphers.size()]));
    if (LOG.isDebugEnabled()) {
      if (sslEngine.getUseClientMode()) {
        LOG.debug("Enabled server cipher suites:");
      } else {
        String host = sslEngine.getPeerHost();
        int port = sslEngine.getPeerPort();
        LOG.debug("Enabled client {}:{} cipher suites:", host, port);
      }
      for (String each : ciphers) {
        LOG.debug(each);
      }
    }
  }

  /**
   * Generate a new SSl engine with the current SSL context.
   */
  public SSLEngine newSslEngine() {
    SSLEngine sslEngine = sslContext.createSSLEngine();
    filterWeakCipherSuites(sslEngine);
    return sslEngine;
  }

  @Override
  public SSLEngine newSslEngine(String remoteHost, int remotePort) {
    SSLEngine sslEngine = sslContext.createSSLEngine(remoteHost, remotePort);
    sslEngine.setUseClientMode(true);
    if (!tryHostNameVerificationJava7(sslEngine)) {
      LOG.debug("Host Name Verification is not supported, causes insecure HTTPS connection");
    }
    filterWeakCipherSuites(sslEngine);
    return sslEngine;
  }

  private boolean tryHostNameVerificationJava7(SSLEngine sslEngine) {
    for (Method method : SSLParameters.class.getMethods()) {
      // method is available since Java 7
      if ("setEndpointIdentificationAlgorithm".equals(method.getName())) {
        SSLParameters sslParams = new SSLParameters();
        try {
          method.invoke(sslParams, "HTTPS");
        } catch (IllegalAccessException e) {
          LOG.debug("SSLParameters#setEndpointIdentificationAlgorithm", e);
          return false;
        } catch (InvocationTargetException e) {
          LOG.debug("SSLParameters#setEndpointIdentificationAlgorithm", e);
          return false;
        }
        sslEngine.setSSLParameters(sslParams);
        return true;
      }
    }
    return false;
  }

  private void initializeKeyStore() throws RootCertificateException, GeneralSecurityException,
      OperatorCreationException, IOException {
    if (authority.aliasFile(KEY_STORE_FILE_EXTENSION).exists()
        && authority.aliasFile(".pem").exists()) {
      return;
    }
    MillisecondsDuration duration = new MillisecondsDuration();
    KeyStore keystore = CertificateHelper.createRootCertificate(authority, KEY_STORE_TYPE);
    LOG.debug("Created root certificate authority key store in {}ms", duration);

    try (OutputStream os = new FileOutputStream(authority.aliasFile(KEY_STORE_FILE_EXTENSION))) {
      keystore.store(os, authority.password());
    }

    cert = keystore.getCertificate(authority.alias());
    exportPem(authority.aliasFile(".pem"), cert);
  }

  public Certificate getCert() {
    return cert;
  }

  void initializeSslContext(boolean trustAllServers) throws GeneralSecurityException, IOException {
    KeyStore ks = loadKeyStore();
    caCert = ks.getCertificate(authority.alias());
    caPrivKey = (PrivateKey) ks.getKey(authority.alias(), authority.password());

    TrustManager[] trustManagers;
    if (trustAllServers) {
      trustManagers = InsecureTrustManagerFactory.INSTANCE.getTrustManagers();
    } else {
      trustManagers = new TrustManager[] { new MergeTrustManager(ks) };
    }

    KeyManager[] keyManagers;
    if (sendCerts) {
      keyManagers = CertificateHelper.getKeyManagers(ks, authority);
    } else {
      keyManagers = new KeyManager[0];
    }

    sslContext = CertificateHelper.newClientContext(keyManagers, trustManagers);
    SSLEngine sslEngine = sslContext.createSSLEngine();
    if (!tryHostNameVerificationJava7(sslEngine)) {
      LOG.warn("Host Name Verification is not supported,"
          + "causes insecure HTTPS connection to upstream servers.");
    }
  }

  private KeyStore loadKeyStore() throws GeneralSecurityException, IOException {
    KeyStore ks = KeyStore.getInstance(KEY_STORE_TYPE);

    try (FileInputStream is = new FileInputStream(authority.aliasFile(KEY_STORE_FILE_EXTENSION))) {
      ks.load(is, authority.password());
    }
    return ks;
  }

  /**
   * Generates an 1024 bit RSA key pair using SHA1PRNG. Thoughts: 2048 takes much
   * longer time on older CPUs. And for almost every client, 1024 is sufficient.
   * Derived from Zed Attack Proxy (ZAP). ZAP is an HTTP/HTTPS proxy for assessing
   * web application security. Copyright 2011 mawoki@ymail.com Licensed under the
   * Apache License, Version 2.0
   * @param commonName              the common name to use in the server
   *                                certificate
   * @param subjectAlternativeNames a List of the subject alternative names to use
   *                                in the server certificate, could be empty, but
   *                                must not be null
   */
  public SSLEngine createCertForHost(final String commonName,
      final SubjectAlternativeNameHolder subjectAlternativeNames)
      throws GeneralSecurityException, OperatorCreationException, IOException, ExecutionException {
    if (commonName == null) {
      throw new IllegalArgumentException("Error, 'commonName' is not allowed to be null!");
    }
    if (subjectAlternativeNames == null) {
      throw new IllegalArgumentException(
          "Error, 'subjectAlternativeNames' is not allowed to be null!");
    }

    SSLContext ctx;
    if (serverSslContexts == null) {
      ctx = createServerContext(commonName, subjectAlternativeNames);
    } else {
      ctx = serverSslContexts.get(commonName, new Callable<SSLContext>() {
        @Override
        public SSLContext call() throws Exception {
          return createServerContext(commonName, subjectAlternativeNames);
        }
      });
    }
    return ctx.createSSLEngine();
  }

  private SSLContext createServerContext(String commonName,
      SubjectAlternativeNameHolder subjectAlternativeNames)
      throws GeneralSecurityException, IOException, OperatorCreationException {

    MillisecondsDuration duration = new MillisecondsDuration();

    KeyStore ks = CertificateHelper.createServerCertificate(commonName, subjectAlternativeNames,
        authority, caCert, caPrivKey);
    KeyManager[] keyManagers = CertificateHelper.getKeyManagers(ks, authority);

    SSLContext result = CertificateHelper.newServerContext(keyManagers);

    LOG.debug("Impersonated {} in {}ms", commonName, duration);
    return result;
  }

  /**
   * Creates the needed certificates for the website.
   */
  public void initializeServerCertificates(String commonName,
      SubjectAlternativeNameHolder subjectAlternativeNames)
      throws GeneralSecurityException, OperatorCreationException, IOException {

    KeyStore ks = CertificateHelper.createServerCertificate(commonName, subjectAlternativeNames,
        authority, caCert, caPrivKey);

    PrivateKey key = (PrivateKey) ks.getKey(authority.alias(), authority.password());
    exportPem(authority.aliasFile("-" + commonName + "-key.pem"), key);

    Object[] certs = ks.getCertificateChain(authority.alias());
    exportPem(authority.aliasFile("-" + commonName + "-cert.pem"), certs);
  }

  private void exportPem(File exportFile, Object... certs)
      throws IOException, CertificateEncodingException {

    try (Writer sw = new FileWriter(exportFile)) {
      try (JcaPEMWriter pw = new JcaPEMWriter(sw)) {
        for (Object cert : certs) {
          pw.writeObject(cert);
          pw.flush();
        }
      }
    }
  }

  static class MillisecondsDuration {
    private final long mStartTime = System.currentTimeMillis();

    @Override
    public String toString() {
      return String.valueOf(System.currentTimeMillis() - mStartTime);
    }
  }

}
