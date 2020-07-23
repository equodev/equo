package com.make.equo.server.provider;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ServiceScope;

import com.make.equo.aer.api.IEquoLoggingService;
import com.make.equo.contribution.api.handler.IEquoContributionRequestHandler;
import com.make.equo.server.api.IEquoServer;
import com.make.equo.server.offline.api.IEquoOfflineServer;
import com.make.equo.server.offline.api.filters.IHttpRequestFilter;
import com.make.equo.server.provider.filters.EquoHttpFiltersSourceAdapter;

@Component(scope = ServiceScope.SINGLETON)
public class EquoHttpProxyServer implements IEquoServer {

	private static final List<String> proxiedUrls = new ArrayList<String>();

	private static boolean enableOfflineCache = false;

	private static volatile HttpProxyServer proxyServer;
//	private ScheduledExecutorService internetConnectionChecker;

	@Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.STATIC)
	private volatile IEquoOfflineServer equoOfflineServer;

	@Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.STATIC)
	private volatile IEquoLoggingService equoLoggingService;

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
	private IEquoContributionRequestHandler contributionRequestHandler;

	@Override
	@Activate
	public void start() {
		if (proxyServer == null) {
			startServer();
		}
		
	}

	@Override
	public void startServer() {
		EquoHttpFiltersSourceAdapter httpFiltersSourceAdapter = new EquoHttpFiltersSourceAdapter(
				contributionRequestHandler, equoOfflineServer, isOfflineCacheSupported(), proxiedUrls);

//		Runnable internetConnectionRunnable = new Runnable() {
//			@Override
//			public void run() {
//				if (!isInternetReachable()) {
//					httpFiltersSourceAdapter.setConnectionLimited();
//				} else {
//					httpFiltersSourceAdapter.setConnectionUnlimited();
//				}
//			}
//		};
//		internetConnectionChecker = Executors.newSingleThreadScheduledExecutor();
//		internetConnectionChecker.scheduleAtFixedRate(internetConnectionRunnable, 0, 5, TimeUnit.SECONDS);
		int port = getPortForServer();
		System.setProperty("swt.chromium.args", "--proxy-server=localhost:" + port
				+ ";--ignore-certificate-errors;--allow-file-access-from-files;--disable-web-security;--enable-widevine-cdm;--proxy-bypass-list=127.0.0.1");
		proxyServer = DefaultHttpProxyServer.bootstrap().withPort(port)
				.withManInTheMiddle(new CustomSelfSignedMitmManager()).withAllowRequestToOriginServer(true)
				.withTransparent(false).withFiltersSource(httpFiltersSourceAdapter).start();
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
		System.out.println("Stopping proxy...");
//		if (internetConnectionChecker != null) {
//			internetConnectionChecker.shutdownNow();
//		}
//		if (proxyServer != null) {
//			proxyServer.stop();
//		}
	}

	@Override
	public void addUrl(String url) {
		if (!proxiedUrls.contains(url)) {
			proxiedUrls.add(url);
		} else if (equoLoggingService != null) {
			equoLoggingService.logWarning("The url " + url + " was already added to the Proxy server.");
		}
	}

	private boolean isOfflineCacheSupported() {
		return isOfflineServerSupported() && enableOfflineCache;
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
	}

	@Override
	public void addOfflineSupportFilter(IHttpRequestFilter httpRequestFilter) {
		if (isOfflineCacheSupported()) {
			equoOfflineServer.addHttpRequestFilter(httpRequestFilter);
		}
	}

//	private boolean isInternetReachable() {
//		if (proxiedUrls.isEmpty()) {
//			return false;
//		}
//		return isAddressReachable(proxiedUrls.get(0));
//	}

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
