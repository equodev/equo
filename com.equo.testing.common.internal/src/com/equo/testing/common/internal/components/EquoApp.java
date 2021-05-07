package com.equo.testing.common.internal.components;

import org.osgi.service.component.annotations.Component;

import com.equo.application.api.IEquoApplication;
import com.equo.application.model.EquoApplicationBuilder;

@Component
public class EquoApp implements IEquoApplication {

	@Override
	public EquoApplicationBuilder buildApp(EquoApplicationBuilder appBuilder) {
		return null;
	}

}
