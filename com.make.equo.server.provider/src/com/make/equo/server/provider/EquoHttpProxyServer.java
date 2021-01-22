package com.make.equo.server.provider;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.make.equo.aer.api.IEquoLoggingService;
import com.make.equo.contribution.api.handler.IEquoContributionRequestHandler;
import com.make.equo.server.api.IEquoServer;
import com.make.equo.server.offline.api.IEquoOfflineServer;
import com.make.equo.server.offline.api.filters.IHttpRequestFilter;
import com.make.equo.server.provider.filters.EquoHttpFiltersSourceAdapter;

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
		httpFiltersSourceAdapter = new EquoHttpFiltersSourceAdapter(contributionRequestHandler, equoOfflineServer,
				proxiedUrls);

		int port = getPortForServer();
		System.setProperty("swt.chromium.args", "--proxy-server=localhost:" + port
				+ ";--allow-running-insecure-content;--allow-file-access-from-files;--disable-web-security;--enable-widevine-cdm;--proxy-bypass-list=127.0.0.1;--ignore-certificate-errors");

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
			proxyServer = DefaultHttpProxyServer.bootstrap().withPort(port).withTransparent(false)
					.withFiltersSource(httpFiltersSourceAdapter).withServerResolver(serverResolver)
					.withManInTheMiddle(new CustomHostNameMitmManager()).start();
		} catch (RootCertificateException e) {
			e.printStackTrace();
		}
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
		internetConnectionChecker.scheduleAtFixedRate(internetConnectionRunnable, 0, 5, TimeUnit.SECONDS);
	}

	@Override
	public void addOfflineSupportFilter(IHttpRequestFilter httpRequestFilter) {
		if (isOfflineCacheSupported()) {
			equoOfflineServer.addHttpRequestFilter(httpRequestFilter);
		}
	}

	private boolean isInternetReachable() {
		if (proxiedUrls.isEmpty()) {
			return false;
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
}
