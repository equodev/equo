package com.make.equo.server.provider;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.make.equo.server.api.IEquoServer;
import com.make.equo.server.contribution.EquoContribution;
import com.make.equo.server.contribution.EquoContributionBuilder;

@Component
public class EquoHttpProxyContribution {

	private static final String equoFrameworkJsApi = "equoFramework.js";
	private static final String domModifierJsApi = "domModifier.js";
	private static final String jqueryJsApi = "https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js";

	private EquoContribution contribution;
	
	private IEquoServer server;

	@Activate
	protected void activate() {
		contribution = EquoContributionBuilder.createContribution()
				.withScriptFile(equoFrameworkJsApi)
				.withScriptFile(jqueryJsApi)
				.withServer(server)
				.build();
		contribution.setUrlResolver(new EquoHttpProxyServerURLResolver(contribution));
		String value = System.getProperty("change_original_html");
		if (value == null || (value != null && Boolean.parseBoolean(value))) {
			contribution.addContributedScript(domModifierJsApi);
		}
		contribution.startContributing();
	}
	
	@Reference
	void setEquoServer(IEquoServer server) {
		this.server = server;
	}

	void unsetEquoServer(IEquoServer server) {
		this.server = null;
	}
}
