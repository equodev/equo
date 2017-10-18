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

public class EquoHttpProxy {

	private String url;

	public EquoHttpProxy(String url) {
		this.url = url;
	}

	public void startReverseProxy() throws Exception {
		Server server = createNewServer();
		while (true) {
			try {
				server.start();
				break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			System.in.read();
			server.stop();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(100);
		}
	}

	private Server createNewServer() {
		Server server = new Server();

		ServerConnector connector = new ServerConnector(server);
		connector.setPort(9898);

		HttpConfiguration https = new HttpConfiguration();
        https.addCustomizer(new SecureRequestCustomizer());
		SslContextFactory sslContextFactory = new SslContextFactory();
		sslContextFactory.setKeyStorePath(EmbeddedServer.class.getResource(
				"/keystore.jks").toExternalForm());
		sslContextFactory.setKeyStorePassword("123456");
		sslContextFactory.setKeyManagerPassword("123456");
		ServerConnector sslConnector = new ServerConnector(server,
				new SslConnectionFactory(sslContextFactory, "http/1.1"),
				new HttpConnectionFactory(https));
		sslConnector.setPort(9998);

		server.setConnectors(new Connector[] { connector, sslConnector });
		
		server.setHandler(createNewHandlers(server));
		return server;
	}

	private Handler createNewHandlers(Server server) {
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { createProxyHandler()});
		return handlers;
	}

	private Handler createProxyHandler() {
		ServletContextHandler contextHandler = new ServletContextHandler();

		MainPageProxyHandler mainPageProxyHandler = new MainPageProxyHandler();
        ServletHolder holder = new ServletHolder("mainPageProxyHandler", mainPageProxyHandler);
        holder.setName("mainPageProxyHandler");
		
		holder.setInitParameter("appUrl", url);
		holder.setAsyncSupported(true);
		
		contextHandler.addServlet(holder, "/*");

		return contextHandler;
	}

	public String getAdress() {
		// return server.getHandler().getServer().ge
		return null;
		// return socketConnector.getHost() + ":" + socketConnector.getPort();
	}

}
