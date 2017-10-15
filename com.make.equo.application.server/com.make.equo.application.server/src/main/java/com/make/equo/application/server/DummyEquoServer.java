package com.make.equo.application.server;

public class DummyEquoServer {
	
	public static void main(String[] args) throws Exception {
		new EquoHttpProxy("https://www.facebook.com").startReverseProxy();
	}
	
}
