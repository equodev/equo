package com.make.equo.renderers.contributions;

import static com.make.equo.renderers.util.IRendererConstants.BASE_HTML_FILE;
import static com.make.equo.renderers.util.IRendererConstants.DIALOG_RENDERER_NAME;
import static com.make.equo.renderers.util.IRendererConstants.DIALOG_RENDERER_SCRIPT_FILE;
import static com.make.equo.renderers.util.IRendererConstants.EQUO_RENDERERS_CONTRIBUTION_NAME;
import static com.make.equo.renderers.util.IRendererConstants.PARTSTACK_RENDERER_NAME;
import static com.make.equo.renderers.util.IRendererConstants.PARTSTACK_RENDERER_SCRIPT_FILE;
import static com.make.equo.renderers.util.IRendererConstants.TOOLBAR_RENDERER_NAME;
import static com.make.equo.renderers.util.IRendererConstants.TOOLBAR_RENDERER_SCRIPT_FILE;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.make.equo.server.contribution.EquoContributionBuilder;

@Component
public class EquoRenderersContribution {
	
	private EquoContributionBuilder builder;
	
	@Activate
	protected void activate() {
		builder
			.withBaseHtmlResource(BASE_HTML_FILE)
			.withContributionName(EQUO_RENDERERS_CONTRIBUTION_NAME)
			.withPathWithScript(DIALOG_RENDERER_NAME, DIALOG_RENDERER_SCRIPT_FILE)
			.withPathWithScript(PARTSTACK_RENDERER_NAME, PARTSTACK_RENDERER_SCRIPT_FILE)
			.withPathWithScript(TOOLBAR_RENDERER_NAME, TOOLBAR_RENDERER_SCRIPT_FILE)
			.withURLResolver(new EquoRenderersURLResolver())
			.build();
	}
	
	@Reference
	void setEquoBuilder(EquoContributionBuilder builder) {
		this.builder = builder;
	}
	
	void unsetEquoBuilder(EquoContributionBuilder builder) {
		this.builder = null;
	}
}
