package com.make.equo.application.model;

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

	UrlMandatoryBuilder(EquoApplicationBuilder equoApplicationBuilder) {
		this.equoAppBuilder = equoApplicationBuilder;
	}

	public OptionalViewBuilder withSingleView(String url) {
		setMainWindowUrl(url);
		return new OptionalViewBuilder(this);
	}

	private void setMainWindowUrl(String url) {
		part = MBasicFactory.INSTANCE.createPart();
		part.setElementId(IConstants.MAIN_PART_ID);
		part.setContributionURI("bundleclass://com.make.equo.application.provider/com.make.equo.application.parts.MainPagePart");
		part.getProperties().put(IConstants.MAIN_URL_KEY, url);

		//Get the Window binding context.
		MBindingContext mBindingContext = equoAppBuilder.getmApplication().getBindingContexts().get(1);
		part.getBindingContexts().add(mBindingContext);
		
		mainPartBindingTable = MCommandsFactory.INSTANCE.createBindingTable();
		mainPartBindingTable.setBindingContext(mBindingContext);
		mainPartBindingTable.setElementId("com.make.equo.application.provider.bindingtable.mainpart");
		equoAppBuilder.getmApplication().getBindingTables().add(mainPartBindingTable);
		
		equoAppBuilder.getmWindow().getChildren().add(part);
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
}