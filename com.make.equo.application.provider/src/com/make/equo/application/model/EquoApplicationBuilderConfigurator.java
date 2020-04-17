package com.make.equo.application.model;


import com.make.equo.application.api.IEquoApplication;

public class EquoApplicationBuilderConfigurator {

	private EquoApplicationBuilder equoApplicationBuilder;
	private EquoApplicationModel equoApplicationModel;
	private IEquoApplication equoApp;

	public EquoApplicationBuilderConfigurator(EquoApplicationModel equoApplicationModel,
			EquoApplicationBuilder equoApplicationBuilder, IEquoApplication equoApp) {
		this.equoApplicationModel = equoApplicationModel;
		this.equoApplicationBuilder = equoApplicationBuilder;
		this.equoApp = equoApp;
	}

	public OptionalViewBuilder configure() {
		return this.equoApplicationBuilder.configure(equoApplicationModel, equoApp);
	}
}
