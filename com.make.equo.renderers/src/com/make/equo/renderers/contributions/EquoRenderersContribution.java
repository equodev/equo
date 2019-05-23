package com.make.equo.renderers.contributions;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.make.equo.server.api.IEquoServer;
import com.make.equo.server.contribution.EquoContribution;
import com.make.equo.server.contribution.EquoContributionBuilder;

@Component
public class EquoRenderersContribution {
	
	public static final String EQUO_RENDERERS_BASE_URI = "http://equoRenderers/";
	
	private IEquoServer server;
	private static EquoContribution contribution;
	
	@Activate
	protected void activate() {
		contribution = EquoContributionBuilder.createContribution()
			.withContributedResource("baseRenderer.html")
			.withScriptFile("rendererFramework.js")
			.withContributionBaseUri(EQUO_RENDERERS_BASE_URI)
			.withURLResolver(new EquoRenderersURLResolver())
			.withServer(server)
			.build();
		contribution.startContributing();
	}
	
	public static EquoContribution getContributionDefinition() {
		return contribution;
	}
	
	@Reference
	void setEquoServer(IEquoServer server) {
		this.server = server;
	}
	
	void unsetEquoServer(IEquoServer server) {
		this.server = null;
	}
}
