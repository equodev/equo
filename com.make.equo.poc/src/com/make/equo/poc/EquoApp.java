package com.make.equo.poc;

import java.io.IOException;
import java.net.URISyntaxException;
import org.osgi.service.component.annotations.Component;

import com.make.equo.application.api.IEquoApplication;
import com.make.equo.application.model.EquoApplicationBuilder;

@Component
public class EquoApp implements IEquoApplication {

	@Override
	public EquoApplicationBuilder buildApp(EquoApplicationBuilder appBuilder) {
		try {

			return appBuilder.plainApp("index.html")
					.withCustomScript("js/testAnalytics.js")
					.withCustomScript("js/testLogging.js")
					.withMainMenu("File")
						.addMenuItem("New").onClick(() -> System.out.println("ON_NEW"))
						.onAbout(() -> System.out.println("ON_ABOUT"))
						.onPreferences(() -> System.out.println("ON_PREFERENCES"))
						.onBeforeExit(() -> System.out.println("Bye Bye Equo"))
					.start();

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}



}
