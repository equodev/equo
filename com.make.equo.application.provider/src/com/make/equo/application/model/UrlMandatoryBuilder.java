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

//		MBindingContext mainPartBindingContext = MCommandsFactory.INSTANCE.createBindingContext();
//		mainPartBindingContext.setElementId("com.make.equo.application.provider.bindingcontext.mainpart");
//		mainPartBindingContext.setName("Part Binding Context");
		MBindingContext mBindingContext = equoAppBuilder.mApplication.getBindingContexts().get(1);
//		mBindingContext.getChildren().add(mainPartBindingContext);
		System.out.println("binding context is " + mBindingContext);
		mainPart.getBindingContexts().add(mBindingContext);
//		mBindingContext.getChildren().add(mainPartBindingContext);
//		mainPart.getBindingContexts().add(mainPartBindingContext);
		
		mainPartBindingTable = MCommandsFactory.INSTANCE.createBindingTable();
		mainPartBindingTable.setBindingContext(mBindingContext);
		mainPartBindingTable.setElementId("com.make.equo.application.provider.bindingtable.mainpart");
		equoAppBuilder.mApplication.getBindingTables().add(mainPartBindingTable);
		
		equoAppBuilder.mWindow.getChildren().add(mainPart);
	}
	
	MBindingTable getBindingTable() {
		return mainPartBindingTable;
	}
}