package com.make.equo.application.server;

import java.net.CookieStore;
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
	private EquoApplicationCookieStore equoApplicationCookieStore;
	private HttpClient httpClient;

	@Override
	protected HttpClient newHttpClient() {
		SslContextFactory factory = new SslContextFactory();
		factory.setTrustAll(true);
		httpClient = new HttpClient(factory) {

			@Override
			public CookieStore getCookieStore() {
				return equoApplicationCookieStore;
			}
		};
		return httpClient;
	}

	protected void addXForwardedHeaders(HttpServletRequest clientRequest, Request proxyRequest) {
		proxyRequest.header(HttpHeader.X_FORWARDED_FOR, clientRequest.getRemoteAddr());
		proxyRequest.header(HttpHeader.X_FORWARDED_PROTO, clientRequest.getScheme());
		proxyRequest.header(HttpHeader.X_FORWARDED_SERVER, clientRequest.getLocalName());
		// Cookie[] cookies = clientRequest.getCookies();
		// if (cookies != null) {
		// for (Cookie cookie : cookies) {
		// HttpCookie httpCookie = new HttpCookie(cookie.getName(), cookie.getValue());
		// System.out.println("cookie is " + httpCookie);
		//// proxyRequest.getCookies().add(httpCookie);
		// this.getHttpClient().getCookieStore().add(URI.create(appUrl), httpCookie);
		// }
		// }
	}

	@Override
	protected String filterServerResponseHeader(HttpServletRequest clientRequest, Response serverResponse,
			String headerName, String headerValue) {
		if (headerName.equalsIgnoreCase("location")) {
			URI targetUri = serverResponse.getRequest().getURI();
			String toReplace = targetUri.getScheme() + "://" + targetUri.getAuthority();
			if (headerValue.startsWith(toReplace)) {
				headerValue = clientRequest.getScheme() + "://" + clientRequest.getHeader("host")
						+ headerValue.substring(toReplace.length());
//				log.info("Rewrote location header to " + headerValue);
				System.out.println("Rewrote location header to " + headerValue);
				return headerValue;
			}
		}
		return super.filterServerResponseHeader(clientRequest, serverResponse, headerName, headerValue);
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
		equoApplicationCookieStore = new EquoApplicationCookieStore(appUrl);
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
