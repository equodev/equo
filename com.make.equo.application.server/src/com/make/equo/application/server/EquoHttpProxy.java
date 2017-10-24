package com.make.equo.application.server;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import com.make.equo.application.server.util.IConstants;

public class EquoHttpProxy {

	private String url;
	private Server server;

	public EquoHttpProxy(String url) {
		this.url = url;
	}

	public void startProxy() throws Exception {
		server = createNewServer();
		while (true) {
			try {
				server.start();
				// TODO log the address
				System.out.println("Address is " + getAddress());
				break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private Server createNewServer() {
		Server server = new Server();

		ServerConnector httpConnector = new ServerConnector(server);
		// httpConnector.setPort(9898);

		HttpConfiguration https = new HttpConfiguration();
		https.addCustomizer(new SecureRequestCustomizer());
		SslContextFactory sslContextFactory = new SslContextFactory();
		sslContextFactory.setKeyStorePath(EquoHttpProxy.class.getResource("/keystore.jks").toExternalForm());
		sslContextFactory.setKeyStorePassword("123456");
		sslContextFactory.setKeyManagerPassword("123456");
		ServerConnector sslConnector = new ServerConnector(server,
				new SslConnectionFactory(sslContextFactory, "http/1.1"), new HttpConnectionFactory(https));
		sslConnector.setPort(9998);

		server.setConnectors(new Connector[] { httpConnector, sslConnector });

		server.setHandler(createNewHandlers(server));
		return server;
	}

	private Handler createNewHandlers(Server server) {
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { createProxyHandler() });
		return handlers;
	}

	private Handler createProxyHandler() {
		ServletContextHandler contextHandler = new ServletContextHandler();

		MainPageProxyHandler mainPageProxyHandler = new MainPageProxyHandler();
		ServletHolder holder = new ServletHolder(IConstants.MAIN_PAGE_PROXY_HANDLER_NAME, mainPageProxyHandler);
		holder.setName(IConstants.MAIN_PAGE_PROXY_HANDLER_NAME);

		holder.setInitParameter(IConstants.APP_URL_PARAM, url);
		holder.setAsyncSupported(true);

		contextHandler.addServlet(holder, "/*");

		return contextHandler;
	}

	public String getAddress() {
		if (server != null) {
			ServerConnector serverConnector = (ServerConnector) server.getConnectors()[0];
			if (serverConnector != null) {
				int localPort = serverConnector.getLocalPort();
				if (localPort == -1) {
					return null;
				} else {
					return "localhost" + ":" + localPort;
				}
			}
		}
		return null;
	}

	public boolean isStarted() {
		return server != null && server.isStarted();
	}

	public void stop() {
		try {
			server.stop();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(100);
		}
	}

}
