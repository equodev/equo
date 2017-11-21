package com.make.equo.application.server;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Lists;

public class DummyEquoServer {
	
	public static void main(String[] args) throws Exception {
		String netflixUrl = "https://www.netflix.com/";
		Map<String, Object> urlToScripts = new HashMap<String, Object>();
		urlToScripts.put(netflixUrl, Lists.newArrayList("https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js", "file:/Users/seba/myRepository/equo-product/equo-framework/com.make.equo.application.server/script.js"));
		EquoHttpProxyServer equoHttpProxyServer = new EquoHttpProxyServer(Lists.newArrayList(netflixUrl), urlToScripts, "/Users/seba/myRepository/equo-product/netflix-desktop-app/bin/", "equo");
		equoHttpProxyServer.startProxy();
	}
	
}
