package com.make.equo.server.api;

import org.osgi.framework.Bundle;

public interface IEquoServer {

	void startServer();

	void addCustomScript(String url, String scriptUrl);

	void addUrl(String url);

	void setMainAppBundle(Bundle mainEquoAppBundle);

	String getLocalScriptProtocol();

	String getBundleScriptProtocol();

	void enableOfflineCache();

	void setConnectionLimited();

	void setConnectionUnlimited();

}
