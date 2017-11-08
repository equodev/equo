package com.make.equo.application.server;

public class DummyEquoServer {
	
	public static void main(String[] args) throws Exception {
		new EquoHttpProxyServer("https://www.netflix.com").startProxy();
	}
	
}
