package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.MApplication;

public class EquoApplication {
	
	MApplication mApplication;

	public EquoApplication(MApplication mApplication) {
		this.mApplication = mApplication;
	}

	public UrlMandatoryBuilder name(String name) {
		EquoApplicationBuilder equoApplicationBuilder = new EquoApplicationBuilder(this);
		try {
			return equoApplicationBuilder.name(name);
		} catch (Exception e) {
			System.out.println("Should never reach this state");
		}
		return null;
	}

}
