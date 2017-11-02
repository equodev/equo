package com.make.equo.application.server;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.DispatcherType;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import com.make.equo.application.server.util.IConstants;

public class EquoHttpProxy {

	private static final String URL_PATH = "urlPath";
	private static final String PATH_TO_STRING_REG = "/PATHTOSTRING";
	private String url;
	private List<String> jsScripts;
	private Server server;
	private static final String URL_SCRIPT_SENTENCE = "<script src=\"urlPath\"></script>";
	private static final String LOCAL_SCRIPT_SENTENCE = "<script src=\"http://localhost:PORT/PATHTOSTRING\"></script>";
	
	public static final String PORT = "PORT";

	public EquoHttpProxy(String url) {
		this.url = url;
		this.jsScripts = new ArrayList<>();
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

		server.setConnectors(new Connector[] { httpConnector });

		server.setHandler(createNewHandlers(server));
		return server;
	}

	private Handler createNewHandlers(Server server) {
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { createServlets(handlers), new DefaultHandler() });
		return handlers;
	}

	private Handler createServlets(HandlerList handlers) {
		ServletContextHandler contextHandler = new ServletContextHandler(handlers, "/", ServletContextHandler.SESSIONS);

		try {
			createLocalScriptsServletHandlers(contextHandler);
		} catch (URISyntaxException | IOException e) {
			//TODO log exception
			e.printStackTrace();
		}
		
		FilterHolder cors = contextHandler.addFilter(CrossOriginFilter.class,"/*",EnumSet.of(DispatcherType.REQUEST));
		cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
		cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
		cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,HEAD");
		cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");
		
		ServletHolder proxyServlet = new ServletHolder(MainPageProxyHandler.class);
		proxyServlet.setInitParameter(IConstants.APP_URL_PARAM, url);
		proxyServlet.setInitParameter(IConstants.APP_JS_SCRIPTS_PARAM, convertJsScriptsToString());
		proxyServlet.setAsyncSupported(true);
		contextHandler.addServlet(proxyServlet, "/*");

		return contextHandler;
	}

	private List<String> createLocalScriptsServletHandlers(ServletContextHandler contextHandler)
			throws URISyntaxException, IOException {
		List<String> localScripts = getLocalScripts();
		List<String> servletHandlersPaths = new ArrayList<>();
		for (String localScript : localScripts) {
			URL url = new URL(localScript);
			File file = new File(FileLocator.resolve(url).toURI());
			String absolutePath = file.getParentFile().getAbsolutePath();
			if (!servletHandlersPaths.contains(absolutePath)) {
				ServletHolder holderHome = new ServletHolder(absolutePath, DefaultServlet.class);
				holderHome.setInitParameter("resourceBase", absolutePath);
				holderHome.setInitParameter("dirAllowed", "true");
				holderHome.setInitParameter("pathInfoOnly", "true");
				contextHandler.addServlet(holderHome, absolutePath + "/*");
				servletHandlersPaths.add(absolutePath);
			}
		}
		return servletHandlersPaths;
	}

	private List<String> getLocalScripts() {
		List<String> fileScripts = new ArrayList<>();
		for (String scriptPath : jsScripts) {
			if (isLocalScript(scriptPath)) {
				fileScripts.add(scriptPath);
			}
		}
		return fileScripts;
	}

	private boolean isLocalScript(String scriptPath) {
		String scriptPathLoweredCase = scriptPath.trim().toLowerCase();
		return scriptPathLoweredCase.startsWith("file:/");
	}

	private String convertJsScriptsToString() {
		if (jsScripts.isEmpty()) {
			return "";
		}
		String newLineSeparetedScripts = jsScripts.stream().map(string -> generateScriptSentence(string))
				.collect(Collectors.joining("\n"));
		return newLineSeparetedScripts;
	}

	private String generateScriptSentence(String scriptPath) {
		try {
			if (isLocalScript(scriptPath)) {
				URL url = new URL(scriptPath);
				File file = new File(FileLocator.resolve(url).toURI());
				String absolutePath = file.getAbsolutePath();

				String scriptSentence = LOCAL_SCRIPT_SENTENCE.replaceAll(PATH_TO_STRING_REG, absolutePath);
				return scriptSentence;
			} else {
				URL url = new URL(scriptPath);
				String scriptSentence = URL_SCRIPT_SENTENCE.replaceAll(URL_PATH, url.toString());
				return scriptSentence;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
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

	public void addScripts(List<String> customScripts) {
		jsScripts.addAll(customScripts);
	}

}
