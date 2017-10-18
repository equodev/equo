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
	
	protected void addProxyHeaders(HttpServletRequest clientRequest, Request proxyRequest)
    {
        addViaHeader(proxyRequest);
        addXForwardedHeaders(clientRequest, proxyRequest);
    }

    protected void addViaHeader(Request proxyRequest)
    {
        proxyRequest.header(HttpHeader.VIA, "http/1.1 " + getViaHost());
    }

    protected void addXForwardedHeaders(HttpServletRequest clientRequest, Request proxyRequest)
    {
//        proxyRequest.header(HttpHeader.X_FORWARDED_FOR, clientRequest.getRemoteAddr());
//        proxyRequest.header(HttpHeader.X_FORWARDED_PROTO, clientRequest.getScheme());
//        proxyRequest.header(HttpHeader.X_FORWARDED_HOST, clientRequest.getHeader(HttpHeader.HOST.asString()));
//        proxyRequest.header(HttpHeader.X_FORWARDED_SERVER, clientRequest.getLocalName());
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
		}
		if (null == appUrl) {
			appUrl = "https://netflix.com";
		}
	}

	//issue https://gitlab.com/maketechnology/equo-framework/issues/31
	@Override
	protected String filterServerResponseHeader(HttpServletRequest clientRequest, Response serverResponse,
			String headerName, String headerValue) {
//		if (headerName.equalsIgnoreCase("location")) {
//			URI targetUri = serverResponse.getRequest().getURI();
//			String toReplace = targetUri.getScheme() + "://" + targetUri.getAuthority();
//			if (headerValue.startsWith(toReplace)) {
//				headerValue = clientRequest.getScheme() + "://" + clientRequest.getHeader("host");
//				// + headerValue.substring(toReplace.length());
//				System.out.println("Rewrote location header to " + headerValue);
//				return headerValue;
//			}
//		}
		return super.filterServerResponseHeader(clientRequest, serverResponse, headerName, headerValue);
	}

	@Override
	protected String rewriteTarget(HttpServletRequest request) {
		URI rewrittenURI = URI.create(appUrl.toString()).normalize();
		return rewrittenURI.toString();
	}

}
