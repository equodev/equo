package com.make.equo.server.api;

import org.osgi.framework.Bundle;

public interface IEquoServer {

	public void startServer();

	public void addCustomScript(String url, String scriptUrl);

	public void addUrl(String url);

	public void setMainAppBundle(Bundle mainEquoAppBundle);

	public String getLocalScriptProtocol();

	public String getBundleScriptProtocol();

	public String generateEquoAppUrl(String originalUrl);

}
