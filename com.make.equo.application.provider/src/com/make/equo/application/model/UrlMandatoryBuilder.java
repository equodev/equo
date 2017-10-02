package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;

import com.make.equo.application.util.IConstants;

public final class UrlMandatoryBuilder {

	private EquoApplicationBuilder equoAppBuilder;

	UrlMandatoryBuilder(EquoApplicationBuilder equoApplicationBuilder) {
		this.equoAppBuilder = equoApplicationBuilder;
	}

	public OptionalFieldBuilder withSingleView(String url) {
		setMainWindowUrl(url);
		return equoAppBuilder.optionalBuilder;
	}

	private void setMainWindowUrl(String url) {
		MPart mainPart = MBasicFactory.INSTANCE.createPart();
		mainPart.setElementId(IConstants.MAIN_PART_ID);
		mainPart.setContributionURI("bundleclass://com.make.equo.application.provider/com.make.equo.application.parts.MainPagePart");
		mainPart.getProperties().put(IConstants.MAIN_URL_KEY, url);
		equoAppBuilder.mWindow.getChildren().add(mainPart);
	}
}