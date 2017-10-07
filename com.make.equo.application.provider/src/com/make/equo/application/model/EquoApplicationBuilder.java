package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MBindingContext;
import org.eclipse.e4.ui.model.application.commands.MBindingTable;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuFactoryImpl;

import com.make.equo.application.util.IConstants;

public class EquoApplicationBuilder {

	final OptionalFieldBuilder optionalBuilder;
	EquoApplication equoApplication;
	final MApplication mApplication;
	MTrimmedWindow mWindow;
	UrlMandatoryBuilder urlMandatoryFieldBuilder;

	EquoApplicationBuilder(EquoApplication equoApplication) {
		this.equoApplication = equoApplication;
		this.mApplication = equoApplication.getEquoApplicationModel().getMainApplication();
		this.urlMandatoryFieldBuilder = new UrlMandatoryBuilder(this);
		this.optionalBuilder = new OptionalFieldBuilder(this);
	}

	public UrlMandatoryBuilder name(String name) {
		String appId = IConstants.EQUO_APP_PREFIX + "." + name.trim().toLowerCase();
		mWindow = (MTrimmedWindow) mApplication.getChildren().get(0);
		mWindow.setLabel(name);
		MMenu mainMenu = MenuFactoryImpl.eINSTANCE.createMenu();
		mainMenu.setElementId(appId + "." + "mainmenu");
		mWindow.setMainMenu(mainMenu);
		
		createDefaultBindingContexts();

		MBindingTable mainWindowBindingTable = MCommandsFactory.INSTANCE.createBindingTable();
		mainWindowBindingTable.setBindingContext(mApplication.getBindingContexts().get(0));
		mainWindowBindingTable.setElementId(IConstants.DEFAULT_BINDING_TABLE);
		
		mApplication.getBindingTables().add(mainWindowBindingTable);
		
		return this.urlMandatoryFieldBuilder;
	}

	private void createDefaultBindingContexts() {
		MBindingContext windowAndDialogBindingContext = MCommandsFactory.INSTANCE.createBindingContext();
		windowAndDialogBindingContext.setElementId(IConstants.DIALOGS_AND_WINDOWS_BINDING_CONTEXT);
		windowAndDialogBindingContext.setName("Dialogs and Windows Binding Context");
		
		MBindingContext windowBindingContext = MCommandsFactory.INSTANCE.createBindingContext();
		windowBindingContext.setElementId(IConstants.WINDOWS_BINDING_CONTEXT);
		windowBindingContext.setName("Windows Binding Context");

		MBindingContext dialogBindingContext = MCommandsFactory.INSTANCE.createBindingContext();
		dialogBindingContext.setElementId(IConstants.DIALOGS_BINDING_CONTEXT);
		dialogBindingContext.setName("Dialogs Binding Context");
		
		//keep this order, the order in which they are added is important.
		mApplication.getBindingContexts().add(windowAndDialogBindingContext);
		mApplication.getBindingContexts().add(windowBindingContext);
		mApplication.getBindingContexts().add(dialogBindingContext);
	}
}