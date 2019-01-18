package com.make.equo.application.model;

import com.make.equo.application.EquoApplicationModel;

public class EquoApplicationBuilderConfigurator {

	private EquoApplicationBuilder equoApplicationBuilder;
	private EquoApplicationModel equoApplicationModel;

	public EquoApplicationBuilderConfigurator(EquoApplicationModel equoApplicationModel,
			EquoApplicationBuilder equoApplicationBuilder) {
		this.equoApplicationModel = equoApplicationModel;
		this.equoApplicationBuilder = equoApplicationBuilder;
	}

	public OptionalViewBuilder configure() {
		return this.equoApplicationBuilder.configure(equoApplicationModel);
	}
}