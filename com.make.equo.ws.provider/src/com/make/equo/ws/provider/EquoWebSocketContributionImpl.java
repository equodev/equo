package com.make.equo.ws.provider;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

import com.make.equo.contribution.api.IEquoContribution;
import com.make.equo.ws.api.IEquoWebSocketService;

@Component(name = "equoWebsocketContribution", property = { "type=websocketContribution",
		"service.ranking:Integer=10000" })
public class EquoWebSocketContributionImpl implements IEquoContribution {

	private static final String equoWebsocketsJsApi = "equoWebsockets.js";
	private IEquoWebSocketService equoWebSocketService;
	private Map<String, Object> properties;

	@Activate
	@Modified
	protected void activate() {
		this.properties = new HashMap<>();
		this.properties.put("websocketPort", this.equoWebSocketService.getPort());
	}

	@Reference
	void setEquoWebSocketService(IEquoWebSocketService equoWebSocketService) {
		this.equoWebSocketService = equoWebSocketService;
	}

	void unsetEquoWebSocketService(IEquoWebSocketService equoWebSocketService) {
		this.equoWebSocketService = null;
	}

	@Override
	public Map<String, Object> getProperties() {
		return properties;
	}

	@Override
	public URL getJavascriptAPIResource(String name) {
		return this.getClass().getClassLoader().getResource(name);
	}

	@Override
	public List<String> getJavascriptFileNames() {
		return Arrays.asList(equoWebsocketsJsApi);
	}
}
