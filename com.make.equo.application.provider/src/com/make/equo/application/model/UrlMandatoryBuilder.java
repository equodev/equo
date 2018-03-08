package com.make.equo.application.model;

import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.commands.MBindingContext;
import org.eclipse.e4.ui.model.application.commands.MBindingTable;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.osgi.framework.Bundle;

import com.make.equo.application.util.FrameworkUtil;
import com.make.equo.application.util.IConstants;
import com.make.equo.server.api.IEquoServer;

public class UrlMandatoryBuilder {

	private EquoApplicationBuilder equoAppBuilder;
	private MBindingTable mainPartBindingTable;
	private MPart part;
	private String url;
	@Inject
	private IEquoServer equoServer;

	UrlMandatoryBuilder(EquoApplicationBuilder equoApplicationBuilder) {
		this.equoAppBuilder = equoApplicationBuilder;
	}

	public OptionalViewBuilder withSingleView(String url) {
		setMainWindowUrl(url);
		OptionalViewBuilder optionalViewBuilder = new OptionalViewBuilder(this);
		FrameworkUtil.INSTANCE.inject(optionalViewBuilder);
		return optionalViewBuilder;
	}

	private void setMainWindowUrl(String windowUrl) {
		this.url = normalizeUrl(windowUrl);
		addUrlToProxyServer(url);
		part = MBasicFactory.INSTANCE.createPart();
		part.setElementId(IConstants.MAIN_PART_ID);
		part.setContributionURI(IConstants.SINGLE_PART_CONTRIBUTION_URI);
		part.getProperties().put(IConstants.MAIN_URL_KEY, url);

		// Get the Window binding context.
		MBindingContext mBindingContext = equoAppBuilder.getmApplication().getBindingContexts().get(1);
		part.getBindingContexts().add(mBindingContext);

		mainPartBindingTable = MCommandsFactory.INSTANCE.createBindingTable();
		mainPartBindingTable.setBindingContext(mBindingContext);
		mainPartBindingTable.setElementId("com.make.equo.application.provider.bindingtable.mainpart");
		equoAppBuilder.getmApplication().getBindingTables().add(mainPartBindingTable);

		equoAppBuilder.getmWindow().getChildren().add(part);
	}

	private void addUrlToProxyServer(String url) {
		Bundle mainEquoAppBundle = FrameworkUtil.INSTANCE.getMainEquoAppBundle();
		if (mainEquoAppBundle != null) {
			equoServer.setMainAppBundle(mainEquoAppBundle);
		}
		FrameworkUtil.INSTANCE.inject(equoServer);
		equoServer.addUrl(url);
	}

	private String normalizeUrl(String url) {
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
}
