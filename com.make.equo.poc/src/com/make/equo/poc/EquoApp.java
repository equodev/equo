package com.make.equo.poc;

import java.net.URISyntaxException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.make.equo.application.api.IEquoApplication;
import com.make.equo.application.model.EquoApplicationBuilder;
import com.make.equo.ws.api.IEquoEventHandler;
import com.make.equo.ws.api.IEquoRunnable;

@Component
public class EquoApp implements IEquoApplication {
	@Reference
	private IEquoEventHandler equoEventHandler;

	@Override
	public EquoApplicationBuilder buildApp(EquoApplicationBuilder appBuilder) {
		equoEventHandler.on("_exitapp", (IEquoRunnable) nothing -> System.exit(0));

		try {
			return appBuilder.plainApp("index.html").start();

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
