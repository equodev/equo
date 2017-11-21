package com.make.equo.application.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.e4.ui.model.application.commands.MBindingContext;
import org.eclipse.e4.ui.model.application.commands.MBindingTable;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;

import com.make.equo.application.util.IConstants;

public class UrlMandatoryBuilder {

	private EquoApplicationBuilder equoAppBuilder;
	private MBindingTable mainPartBindingTable;
	private MPart part;
	private String url;

	UrlMandatoryBuilder(EquoApplicationBuilder equoApplicationBuilder) {
		this.equoAppBuilder = equoApplicationBuilder;
	}

	public OptionalViewBuilder withSingleView(String url) {
		setMainWindowUrl(url);
		return new OptionalViewBuilder(this);
	}

	private void setMainWindowUrl(String windowUrl) {
		this.url = normalizeUrl(windowUrl);
		addUrlToProxyServer(url);
		part = MBasicFactory.INSTANCE.createPart();
		part.setElementId(IConstants.MAIN_PART_ID);
		part.setContributionURI(
				"bundleclass://com.make.equo.application.provider/com.make.equo.application.parts.SinglePagePart");
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

	@SuppressWarnings("unchecked")
	private void addUrlToProxyServer(String url) {
		Map<String, Object> transientData = equoAppBuilder.getEquoProxyServerAddon().getTransientData();
		List<String> urlsToProxy = (List<String>) transientData.get(IConstants.URLS_TO_PROXY);
		if (urlsToProxy == null) {
			urlsToProxy = new ArrayList<>();
		}
		urlsToProxy.add(url);
		transientData.put(IConstants.URLS_TO_PROXY, urlsToProxy);
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