package com.make.equo.testapp;

import org.osgi.service.component.annotations.Component;

import com.make.equo.application.api.IEquoApplication;
import com.make.equo.application.model.EquoApplicationBuilder;

@Component
public class TestApp implements IEquoApplication {

	@Override
	public EquoApplicationBuilder buildApp(EquoApplicationBuilder appBuilder) {
		// TODO Auto-generated method stub
		return appBuilder
                .withSingleView("http://equo.maketechnology.io")
                .start();
	}

}
