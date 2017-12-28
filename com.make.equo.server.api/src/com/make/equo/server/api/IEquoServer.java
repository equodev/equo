package com.make.equo.server.api;

public interface IEquoServer {
	
	public void startServer();

	public void addCustomScript(String url, String scriptUrl);

	public void addUrl(String url);

	//TODO temporary method to test the correct injection of OSGI services. Remove it.
	public void setAppBundlePath(String appBundlePath);

}
