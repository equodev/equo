package com.make.equo.application.model;

import com.make.equo.application.EquoApplicationModel;
import com.make.equo.application.util.FrameworkUtil;

public class EquoApplication {
	
	private EquoApplicationModel equoApplicationModel;

	public EquoApplication(EquoApplicationModel equoApplicationModel) {
		this.equoApplicationModel = equoApplicationModel;
	}

	public UrlMandatoryBuilder name(String name) {
		EquoApplicationBuilder equoApplicationBuilder = new EquoApplicationBuilder(this);
		FrameworkUtil.INSTANCE.inject(equoApplicationBuilder);
		try {
			return equoApplicationBuilder.name(name);
		} catch (Exception e) {
			System.out.println("Should never reach this state");
		}
		return null;
	}

	EquoApplicationModel getEquoApplicationModel() {
		return equoApplicationModel;
	}
	
}
