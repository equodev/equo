package com.make.equo.server.provider;

import java.nio.file.Paths;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import org.littleshoot.proxy.MitmManager;
import io.netty.handler.codec.http.HttpRequest;

public class CustomSelfSignedMitmManager implements MitmManager{
    CustomSelfSignedSslEngineSource selfSignedSslEngineSource =
    		new CustomSelfSignedSslEngineSource(Paths.get(System.getProperty("user.home"),".equo",".littleproxy_keystore.jks").toString(), true, true);

    @Override
    public SSLEngine serverSslEngine(String peerHost, int peerPort) {
        return selfSignedSslEngineSource.newSslEngine(peerHost, peerPort);
    }

    @Override
    public SSLEngine serverSslEngine() {
        return selfSignedSslEngineSource.newSslEngine();
    }

    @Override
    public SSLEngine clientSslEngineFor(HttpRequest httpRequest, SSLSession serverSslSession) {
        return selfSignedSslEngineSource.newSslEngine();
    }
}
