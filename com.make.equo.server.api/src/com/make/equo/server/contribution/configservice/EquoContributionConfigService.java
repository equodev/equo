package com.make.equo.server.contribution.configservice;

import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.make.equo.analytics.client.provider.AnalyticsURLResolver;
import com.make.equo.contribution.media.provider.MediaContributionURLResolver;
import com.make.equo.renderers.contributions.EquoRenderersURLResolver;
import com.make.equo.server.contribution.EquoContribution;
import com.make.equo.server.contribution.EquoContributionBuilder;
import com.make.equo.server.contribution.configservice.pojo.ConfigContribution;
import com.make.equo.server.contribution.resolvers.IEquoContributionUrlResolver;
import com.make.equo.server.provider.EquoHttpProxyServerURLResolver;
import com.make.equo.ws.provider.EquoWebSocketURLResolver;

@Component
public class EquoContributionConfigService {
	
	EquoContributionBuilder builder;
	
	public EquoContribution defineContribution(JsonObject configJson) {
		return parseContributionJsonConfig(configJson);
	}
	
	private EquoContribution parseContributionJsonConfig(JsonObject configJson) {
		Gson parser = new Gson();
		ConfigContribution config = parser.fromJson(configJson, ConfigContribution.class);
		String contributionName = config.getContributionName();
		String contributionHtmlName = config.getContributionHtmlName();
		List<String> proxiedUris = config.getProxiedUris();
		List<String> scripts = config.getContributedScripts();
		Map<String,String> pathsWithScripts = config.getPathsWithScripts();
		if(contributionName!=null){
			builder.withContributionName(contributionName);
		}

		if(contributionHtmlName!=null){
			builder.withBaseHtmlResource(contributionHtmlName);
		}
		if(proxiedUris!=null){
			for(String uri : proxiedUris) {
				builder.withProxiedUri(uri);
			}
		}
		if(scripts!=null){
			builder.withScriptFiles(scripts);
		}
		if(pathsWithScripts!=null){
			for(String path : pathsWithScripts.keySet()) {
				builder.withPathWithScript(path, pathsWithScripts.get(path));
			}
		}
		return builder.build();
	}
	
	private IEquoContributionUrlResolver getResolver(String resolver) {
		switch (resolver) {
		case "httpProxy":
			return new EquoHttpProxyServerURLResolver();
		case "renderers":
			return new EquoRenderersURLResolver();
		case "webSocket":
			return new EquoWebSocketURLResolver();
		case "media":
			return new MediaContributionURLResolver();
		case "Analytics":
			return new AnalyticsURLResolver();
		default: 
			return null; // no me convence devolver null pero no se que otra opcion hay.
		}
	}
	
	
	@Reference
	void setEquoBuilder(EquoContributionBuilder builder) {
		this.builder = builder;
	}

	void unsetEquoBuilder(EquoContributionBuilder builder) {
		this.builder = null;
	}
	

}
