package com.make.equo.server.api;

public interface IEquoApplication {
	
	IEquoApplication name(String applicationName);
	
	IEquoApplication withSingleView(String url);

	void show();
}
