package com.make.equo.application.server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NetworkTrafficServerConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class EquoHttpProxy {

	// private static final String MAIN_URL = "www.netflix.com";
	private String url;
	private Server server;
	// private SocketConnector socketConnector;

	public EquoHttpProxy(String url) {
		this.url = url;
	}

	public void startReverseProxy() throws Exception {
//		int timeout = (int) Duration.ofHours(1).getSeconds() * 1000;
		Server server = createNewServer();

		// socketConnector = new SocketConnector();
		// socketConnector.setHost("127.0.0.1");
		// socketConnector.setPort(8888);

		// server.setConnectors(new Connector[] { socketConnector });

		// Setup proxy handler to handle CONNECT methods
		// ConnectHandler proxy = new ConnectHandler();
		// server.setHandler(proxy);

		// Setup proxy servlet
		// ServletContextHandler context = new ServletContextHandler(proxy, "/",
		// ServletContextHandler.SESSIONS);
		//
		// ServletHolder proxyServlet = new ServletHolder(MainPageProxyHandler.class);
		// proxyServlet.setInitParameter("ProxyTo", url);
		// proxyServlet.setInitParameter("Prefix", "/");
		// context.addServlet(proxyServlet, "/*");

		// server.start();

		try {
			System.out.println(">>> STARTING EMBEDDED JETTY SERVER, PRESS ANY KEY TO STOP");
			server.start();
			System.in.read();
			System.out.println(">>> STOPPING EMBEDDED JETTY SERVER");
			server.stop();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private Server createNewServer() {
		server = new Server();
		server.setHandler(createNewHandlers(server));
		server.addConnector(createNewConnector(server));
		return server;
	}

	private ServerConnector createNewConnector(Server server) {
		NetworkTrafficServerConnector connector = new NetworkTrafficServerConnector(server);
		// Set some timeout options to make debugging easier.
		connector.setSoLingerTime(-1);
		connector.setPort(8888);
		return connector;
		

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
		
//		ServletHandler handler = new ServletHandler();
//		ServletHolder holder = handler.addServletWithMapping(MainPageProxyHandler.class, "/*");
		holder.setInitParameter("proxyTo", url);
		holder.setInitParameter("prefix", "/");
		holder.setInitParameter("debug","true");
		holder.setAsyncSupported(true);
		
		contextHandler.addServlet(holder, "/*");
		
//		FilterHolder filter = contextHandler.getServletHandler().addFilterWithMapping(.class,"/*",0);
	    
//		contextHandler.setServletHandler(handler);

		return contextHandler;
	}

	public String getAdress() {
		// return server.getHandler().getServer().ge
		return null;
		// return socketConnector.getHost() + ":" + socketConnector.getPort();
	}

}
