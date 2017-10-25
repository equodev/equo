package com.make.equo.application.server;

import java.net.HttpCookie;
import java.net.URI;

import org.eclipse.jetty.util.HttpCookieStore;

public class EquoApplicationCookieStore extends HttpCookieStore {

	private URI appUrl;

	public EquoApplicationCookieStore(String appUrl) {
		this.appUrl = URI.create(appUrl);
	}

	@Override
	public void add(URI uri, HttpCookie cookie) {
		if (uri.getHost().endsWith(appUrl.getHost())) {
			super.add(uri, cookie);
		}
	}

}
