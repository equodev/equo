package com.make.equo.eclipse.testapp;

import org.osgi.service.component.annotations.Component;

import com.make.equo.application.api.IEquoApplication;
import com.make.equo.application.model.EquoApplicationBuilder;
import com.make.equo.application.model.IApplicationBuilder;

@Component
public class TestApp implements IEquoApplication {

	@Override
	public IApplicationBuilder buildApp(EquoApplicationBuilder appBuilder) {
		return null;
	}

}
