package com.make.equo.server.provider;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;

import org.littleshoot.proxy.MitmManager;
import org.littleshoot.proxy.mitm.FakeCertificateException;
import org.littleshoot.proxy.mitm.RootCertificateException;
import org.littleshoot.proxy.mitm.SubjectAlternativeNameHolder;

import io.netty.handler.codec.http.HttpRequest;

public class CustomHostNameMitmManager implements MitmManager{
    private CustomBouncyCastleSslEngineSource sslEngineSource;

    public CustomHostNameMitmManager() throws RootCertificateException {
        this(new CustomAuthority());
    }

    public CustomHostNameMitmManager(CustomAuthority authority)
            throws RootCertificateException {
        try {
            boolean trustAllServers = true;
            boolean sendCerts = true;
            sslEngineSource = new CustomBouncyCastleSslEngineSource(authority,
                    trustAllServers, sendCerts);
        } catch (final Exception e) {
            throw new RootCertificateException(
                    "Errors during assembling root CA.", e);
        }
    }

    public SSLEngine serverSslEngine(String peerHost, int peerPort) {
        return sslEngineSource.newSslEngine(peerHost, peerPort);
    }

    @Override
    public SSLEngine serverSslEngine() {
        return sslEngineSource.newSslEngine();
    }

    public SSLEngine clientSslEngineFor(HttpRequest httpRequest, SSLSession serverSslSession) {
        String serverHostAndPort = httpRequest.getUri();
        try {
            String serverName = serverHostAndPort.split(":")[0];
            SubjectAlternativeNameHolder san = new SubjectAlternativeNameHolder();
            san.addDomainName(serverName);
            return sslEngineSource.createCertForHost(serverName, san);
        } catch (Exception e) {
            throw new FakeCertificateException(
                    "Creation dynamic certificate failed for "
                            + serverHostAndPort, e);
        }
    }
}
