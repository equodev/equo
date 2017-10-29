package com.make.equo.application.server;

import java.net.URI;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.proxy.AsyncMiddleManServlet;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import com.make.equo.application.server.util.IConstants;

public class MainPageProxyHandler extends AsyncMiddleManServlet {

	private static final long serialVersionUID = 1094851237951702329L;
	private String appUrl;
	private String jsScripts;

	@Override
	protected HttpClient newHttpClient() {
		SslContextFactory factory = new SslContextFactory();
		factory.setTrustAll(true);
		HttpClient httpClient = new HttpClient(factory) {
			@Override
			protected void doStart() throws Exception {
				super.doStart();
				this.setCookieStore(new EquoApplicationCookieStore(appUrl));
			}
		};
		return httpClient;
	}

	protected void addXForwardedHeaders(HttpServletRequest clientRequest, Request proxyRequest) {
		proxyRequest.header(HttpHeader.X_FORWARDED_FOR, clientRequest.getRemoteAddr());
		proxyRequest.header(HttpHeader.X_FORWARDED_PROTO, clientRequest.getScheme());
		proxyRequest.header(HttpHeader.X_FORWARDED_SERVER, clientRequest.getLocalName());
	}

	@Override
	protected ContentTransformer newServerResponseContentTransformer(HttpServletRequest clientRequest,
			HttpServletResponse proxyResponse, Response serverResponse) {
		return new EquoAppContentTransformer(clientRequest, jsScripts);
	}

	@Override
	public void init() throws ServletException {
		ServletConfig config = getServletConfig();
		appUrl = config.getInitParameter(IConstants.APP_URL_PARAM);
		if (appUrl == null) {
			throw new UnavailableException("Init parameter 'appUrl' is required.");
		}
		jsScripts = config.getInitParameter(IConstants.APP_JS_SCRIPTS_PARAM);
		// Allow override with system property
		try {
			appUrl = System.getProperty(IConstants.APP_URL_PARAM, appUrl);
			jsScripts = System.getProperty(IConstants.APP_JS_SCRIPTS_PARAM, jsScripts);
		} catch (SecurityException e) {
			// TODO log exception
			System.out.println("Error while trying to read appUrl System property" + e);
		}
		super.init();
	}

	@Override
	protected String rewriteTarget(HttpServletRequest request) {
		String pathInfo = request.getPathInfo();
		URI rewrittenURI;
		if (pathInfo != null) {
			rewrittenURI = URI.create(appUrl + pathInfo).normalize();
		} else {
			rewrittenURI = URI.create(appUrl);
		}
		return rewrittenURI.toString();
	}

}
