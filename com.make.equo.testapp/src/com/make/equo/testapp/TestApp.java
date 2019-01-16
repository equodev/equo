	package com.make.equo.testapp;

import java.io.IOException;
import java.net.URISyntaxException;

import org.osgi.service.component.annotations.Component;

import com.make.equo.application.api.IEquoApplication;
import com.make.equo.application.model.EquoApplicationBuilder;

@Component
public class TestApp implements IEquoApplication {

	@Override
	public EquoApplicationBuilder buildApp(EquoApplicationBuilder appBuilder) {
		// TODO Auto-generated method stub
		try {
			return appBuilder
			        .withSingleView("http://www.maketechnology.io")
			        .enableAnalytics()
			        .addCustomScript("js/testAnalytics.js")
			        .start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
