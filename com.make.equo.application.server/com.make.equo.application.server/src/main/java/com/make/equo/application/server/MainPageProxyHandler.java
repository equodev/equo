package com.make.equo.application.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentProvider;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.proxy.AsyncMiddleManServlet;
import org.eclipse.jetty.util.Callback;
import org.eclipse.jetty.util.ssl.SslContextFactory;

public class MainPageProxyHandler extends AsyncMiddleManServlet.Transparent {

	private static final long serialVersionUID = 1094851237951702329L;
	
	@Override
	protected HttpClient newHttpClient() {
		SslContextFactory factory = new SslContextFactory();
        factory.setTrustAll(true);
        return new HttpClient(factory);
	}
	
	
//	@Override
//	public void init() throws ServletException {
//		super.init();
//	    ServletConfig config = getServletConfig();
//	    config.getServletContext().set
//	    placesUrl = config.getInitParameter("PlacesUrl");
//	    apiKey = config.getInitParameter("GoogleApiKey");
//	    // Allow override with system property
//	    try {
//	        placesUrl = System.getProperty("PlacesUrl", placesUrl);
//	        apiKey = System.getProperty("GoogleApiKey", apiKey);
//	    } catch (SecurityException e) {
//	    }
//	    if (null == placesUrl) {
//	        placesUrl = "https://maps.googleapis.com/maps/api/place/search/json";
//	    }
//	}
	
	@Override
	protected void onClientRequestFailure(HttpServletRequest clientRequest, Request proxyRequest,
			HttpServletResponse proxyResponse, Throwable failure) {
		// TODO Auto-generated method stub
		super.onClientRequestFailure(clientRequest, proxyRequest, proxyResponse, failure);
	}
	
	@Override
	protected void onContinue(HttpServletRequest clientRequest, Request proxyRequest) {
		// TODO Auto-generated method stub
		super.onContinue(clientRequest, proxyRequest);
	}
	
	@Override
	protected void onServerResponseHeaders(HttpServletRequest clientRequest, HttpServletResponse proxyResponse,
			Response serverResponse) {
		// TODO Auto-generated method stub
		super.onServerResponseHeaders(clientRequest, proxyResponse, serverResponse);
	}
	
	@Override
	protected void onProxyResponseFailure(HttpServletRequest clientRequest, HttpServletResponse proxyResponse,
			Response serverResponse, Throwable failure) {
		// TODO Auto-generated method stub
		super.onProxyResponseFailure(clientRequest, proxyResponse, serverResponse, failure);
	}
	
	@Override
	protected void onProxyRewriteFailed(HttpServletRequest clientRequest, HttpServletResponse proxyResponse) {
		// TODO Auto-generated method stub
		super.onProxyRewriteFailed(clientRequest, proxyResponse);
	}
	
	@Override
	protected void onProxyResponseSuccess(HttpServletRequest clientRequest, HttpServletResponse proxyResponse,
			Response serverResponse) {
		// TODO Auto-generated method stub
		super.onProxyResponseSuccess(clientRequest, proxyResponse, serverResponse);
	}
	
	@Override
	protected String rewriteTarget(HttpServletRequest request) {
		System.out.println("request is " + request);
		
		System.out.println("remote host is " + request.getRemoteHost());
		System.out.println("header host is " + request.getHeader("Host"));
		System.out.println("header host is " + request.getAttribute("Host"));
		
//		System.out.println("rewriting target..." + super.rewriteTarget(request));
		return super.rewriteTarget(request);
//		return "https://www.netflix.com";
	}
	
//	@Override
//	protected ContentProvider proxyRequestContent(HttpServletRequest request, HttpServletResponse response,
//			Request proxyRequest) throws IOException {
//		// TODO Auto-generated method stub
//		proxyRequest.getHeaders().remove("Host");
//		return super.proxyRequestContent(request, response, proxyRequest);
//	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}
	
	@Override
	protected void onResponseContent(HttpServletRequest request, HttpServletResponse response, Response proxyResponse,
			byte[] buffer, int offset, int length, Callback callback) {
//		try {
//			System.out.println("server response is " + response.getOutputStream().toString());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		// TODO Auto-generated method stub
		super.onResponseContent(request, response, proxyResponse, buffer, offset, length, callback);
	}
	
	
	
//	@Override
//	protected void customizeProxyRequest(Request proxyRequest,
//	        HttpServletRequest request) {
//		System.out.println("headerss: " + proxyRequest.getHeaders());
//	    proxyRequest.getHeaders().remove("Host");
//	}
//	@Override
//	protected URI rewriteURI(HttpServletRequest request) {
//	    String query = request.getQueryString();
//	    return URI.create(placesUrl + "?" + query + "&key=" + apiKey);
//	}
	
//	@Override
//	protected void customizeProxyRequest(Request proxyRequest,
//	        HttpServletRequest request) {
////	    proxyRequest.getHeaders().remove("Host");
//	}

}
