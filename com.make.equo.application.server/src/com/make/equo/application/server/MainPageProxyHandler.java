package com.make.equo.application.server;

import java.net.URI;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.proxy.AsyncMiddleManServlet;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class MainPageProxyHandler extends AsyncMiddleManServlet {

	private static final long serialVersionUID = 1094851237951702329L;
	private String appUrl;

	@Override
	protected HttpClient newHttpClient() {
		SslContextFactory factory = new SslContextFactory();
		factory.setTrustAll(true);
		return new HttpClient(factory);
	}

	protected void addXForwardedHeaders(HttpServletRequest clientRequest, Request proxyRequest) {
		proxyRequest.header(HttpHeader.X_FORWARDED_FOR, clientRequest.getRemoteAddr());
		proxyRequest.header(HttpHeader.X_FORWARDED_PROTO, clientRequest.getScheme());
		proxyRequest.header(HttpHeader.X_FORWARDED_SERVER, clientRequest.getLocalName());
	}

	@Override
	protected ContentTransformer newServerResponseContentTransformer(HttpServletRequest clientRequest,
			HttpServletResponse proxyResponse, Response serverResponse) {
		return ContentTransformer.IDENTITY;
	}

	@Override
	public void init() throws ServletException {
		super.init();
		ServletConfig config = getServletConfig();
		appUrl = config.getInitParameter("appUrl");
		// Allow override with system property
		try {
			appUrl = System.getProperty("appUrl", appUrl);
		} catch (SecurityException e) {
			// TODO log exception
			System.out.println("Error while trying to read appUrl System property" + e);
		}
		if (null == appUrl) {
			appUrl = "http://www.equo.wemaketechnology.com/";
		}
	}

	@Override
	protected String rewriteTarget(HttpServletRequest request) {
		String pathInfo = request.getPathInfo();
		URI rewrittenURI;
		if (pathInfo != null) {
			rewrittenURI = URI.create(appUrl + pathInfo).normalize();
		} else {
;			rewrittenURI = URI.create(appUrl);
		}
		return rewrittenURI.toString();
	}

}
