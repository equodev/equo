package com.make.equo.application.model;

import static com.make.equo.contribution.api.IEquoContributionConstants.OFFLINE_SUPPORT_CONTRIBUTION_NAME;

import java.net.URISyntaxException;

import org.eclipse.e4.ui.model.application.commands.MBindingContext;
import org.eclipse.e4.ui.model.application.commands.MBindingTable;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.make.equo.analytics.internal.api.AnalyticsService;
import com.make.equo.application.api.IEquoApplication;
import com.make.equo.application.util.IConstants;
import com.make.equo.contribution.api.EquoContributionBuilder;
import com.make.equo.contribution.api.resolvers.EquoGenericURLResolver;
import com.make.equo.server.api.IEquoServer;

@Component(service = ViewBuilder.class)
public class ViewBuilder {

	private EquoApplicationBuilder equoAppBuilder;
	private MBindingTable mainPartBindingTable;
	private MPart part;
	private String url;

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
	private EquoContributionBuilder mainAppBuilder;
	
	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
	private EquoContributionBuilder offlineSupportBuilder;
	
	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
	private IEquoServer equoServer;

	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.DYNAMIC)
	private volatile AnalyticsService analyticsService;

	private OptionalViewBuilder optionalViewBuilder;

	OptionalViewBuilder withSingleView(String url) {
		mainAppBuilder.withContributionName("webwrapper");
		this.url = normalizeUrl(url);
		addUrlToProxyServer(this.url);
		part.getProperties().put(IConstants.MAIN_URL_KEY, this.url);
		return optionalViewBuilder;
	}

	OptionalViewBuilder withBaseHtml(String baseHtmlFile) throws URISyntaxException {
		mainAppBuilder.withContributionName("plainequoapp");
		this.url = "http://plainequoapp/";
		part.getProperties().put(IConstants.MAIN_URL_KEY, this.url);
		return optionalViewBuilder.withBaseHtml(baseHtmlFile);
	}

	OptionalViewBuilder configureViewPart(EquoApplicationBuilder equoApplicationBuilder, IEquoApplication equoApp) {
		this.equoAppBuilder = equoApplicationBuilder;
		part = MBasicFactory.INSTANCE.createPart();
		part.setElementId(IConstants.MAIN_PART_ID);
		part.setContributionURI(IConstants.SINGLE_PART_CONTRIBUTION_URI);

		// Get the Window binding context.
		MBindingContext mBindingContext = equoAppBuilder.getmApplication().getBindingContexts().get(1);
		part.getBindingContexts().add(mBindingContext);

		mainPartBindingTable = MCommandsFactory.INSTANCE.createBindingTable();
		mainPartBindingTable.setBindingContext(mBindingContext);
		mainPartBindingTable.setElementId("com.make.equo.application.provider.bindingtable.mainpart");
		equoAppBuilder.getmApplication().getBindingTables().add(mainPartBindingTable);

		equoAppBuilder.getmWindow().getChildren().add(part);

		EquoGenericURLResolver equoAppUrlResolver = new EquoGenericURLResolver(equoApp.getClass().getClassLoader());
		mainAppBuilder.withURLResolver(equoAppUrlResolver);
		offlineSupportBuilder.withContributionName(OFFLINE_SUPPORT_CONTRIBUTION_NAME);
		offlineSupportBuilder.withURLResolver(equoAppUrlResolver);
		optionalViewBuilder = new OptionalViewBuilder(this, equoServer, analyticsService, mainAppBuilder, offlineSupportBuilder, equoApp);

		return optionalViewBuilder;
	}

	private void addUrlToProxyServer(String url) {
		mainAppBuilder.withProxiedUri(url);
	}

	private String normalizeUrl(String url) {
		String protocolURl = url.substring(0, 4).toLowerCase();
		if (!protocolURl.equals("http")) {
			url = "http://" + url;
		}
		// if there is no connection, convert the url from https to http
		if (!equoServer.isAddressReachable(url) && url.startsWith("https")) {
			url = url.replace("https", "http");
		}
		if (url.endsWith("/")) {
			return url;
		} else {
			return url + "/";
		}
	}

	MBindingTable getBindingTable() {
		return mainPartBindingTable;
	}

	EquoApplicationBuilder getEquoApplicationBuilder() {
		return equoAppBuilder;
	}

	MPart getPart() {
		return part;
	}

	String getUrl() {
		return url;
	}

	public EquoApplicationBuilder start() {
		analyticsService.registerLaunchApp();
		mainAppBuilder.build();
		return this.equoAppBuilder;
	}

}
