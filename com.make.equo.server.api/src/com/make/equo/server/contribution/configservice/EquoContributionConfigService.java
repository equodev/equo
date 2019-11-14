package com.make.equo.server.contribution.configservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.make.equo.server.api.IEquoServer;
import com.make.equo.server.contribution.EquoContribution;
import com.make.equo.server.contribution.EquoContributionBuilder;
import com.make.equo.server.contribution.configservice.pojo.ConfigContribution;
import com.make.equo.server.contribution.configservice.pojo.ContributionSet;
import com.make.equo.server.contribution.resolvers.EquoGenericURLResolver;

@Component
public class EquoContributionConfigService implements IContributionConfigService {

	private IEquoServer server;

	public List<EquoContribution> defineContributions(JsonObject configJson, Bundle bundle) {

		ArrayList<EquoContribution> contributions = new ArrayList<EquoContribution>();
		Gson parser = new Gson();
		ContributionSet configSet = parser.fromJson(configJson, ContributionSet.class);
		for (ConfigContribution configCont : configSet.getContributions()) {
			contributions.add(parseContributionJsonConfig(configCont, bundle));
		}
		return contributions;
	}

	public EquoContribution parseContributionJsonConfig(ConfigContribution config, Bundle bundle) {

		if (config.isEmpty()) {
			throw new RuntimeException(
					"A Contribution Config request must be at least one field in the Json config declared.");
		}

		EquoContributionBuilder builder = new EquoContributionBuilder();

		String contributionName = config.getContributionName();
		String contributionHtmlName = config.getContributionHtmlName();
		List<String> proxiedUris = config.getProxiedUris();
		List<String> scripts = config.getContributedScripts();
		Map<String, String> pathsWithScripts = config.getPathsWithScripts();

		if (contributionName != null) {
			builder.withContributionName(contributionName);
		}
		if (contributionHtmlName != null) {
			builder.withBaseHtmlResource(contributionHtmlName);
		}
		if (proxiedUris != null) {
			for (String uri : proxiedUris) {
				builder.withProxiedUri(uri);
			}
		}
		if (scripts != null) {
			builder.withScriptFiles(scripts);
		}
		if (pathsWithScripts != null) {
			for (String path : pathsWithScripts.keySet()) {
				builder.withPathWithScript(path, pathsWithScripts.get(path));
			}
		}
		return builder.withServer(server).withURLResolver(new EquoGenericURLResolver(bundle.adapt(BundleWiring.class).getClassLoader())).build();
	}

	@Reference
	public void setServer(IEquoServer server) {
		this.server = server;
	}

	public void unsetServer(IEquoServer server) {
		this.server = null;
	}

}
