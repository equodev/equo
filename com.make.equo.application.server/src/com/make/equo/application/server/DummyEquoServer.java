package com.make.equo.application.server;

import java.util.ArrayList;
import java.util.List;

public class DummyEquoServer {
	
	public static void main(String[] args) throws Exception {
		EquoHttpProxyServer equoHttpProxyServer = new EquoHttpProxyServer("https://www.netflix.com", "/Users/seba/myRepository/equo-product/netflix-desktop-app/bin/", "equo");
		List<String> customScripts = new ArrayList<>();
		customScripts.add("https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js");
		customScripts.add("file:/Users/seba/myRepository/equo-product/equo-framework/com.make.equo.application.server/script.js");
		equoHttpProxyServer.addScripts(customScripts);
		equoHttpProxyServer.startProxy();
	}
	
}
