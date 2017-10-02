package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuFactoryImpl;

import com.make.equo.application.util.IConstants;

public final class EquoApplicationBuilder {

	final OptionalFieldBuilder optionalBuilder;
	EquoApplication equoApplication;
	final MApplication mApplication;
	MTrimmedWindow mWindow;
	private final UrlMandatoryBuilder urlMandatoryFieldBuilder;

	EquoApplicationBuilder(EquoApplication equoApplication) {
		this.equoApplication = equoApplication;
		this.mApplication = equoApplication.mApplication;
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
		return this.urlMandatoryFieldBuilder;
	}
}