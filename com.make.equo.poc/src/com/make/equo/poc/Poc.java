package com.make.equo.poc;

import java.io.IOException;
import java.net.URISyntaxException;


import com.make.equo.application.api.IEquoApplication;
import com.make.equo.application.model.EquoApplicationBuilder;

public class Poc implements IEquoApplication {

	@Override
	public EquoApplicationBuilder buildApp(EquoApplicationBuilder appBuilder) {
		try {

			return appBuilder.plainApp("index.html").enableAnalytics().withMainMenu("File")
					.addMenuItem("New").onClick(new Runnable() {

						@Override
						public void run() {
							System.out.println("ON_NEW");

						}
					}).onAbout(new Runnable() {

						@Override
						public void run() {
							System.out.println("ON_ABOUT");

						}
					}).onPreferences(new Runnable() {

						@Override
						public void run() {
							System.out.println("ON_PREFERENCES");

						}
					}).onBeforeExit(new Runnable() {

						@Override
						public void run() {
							System.out.println("Bye Bye Equo");

						}
					}).withToolbar().addToolItem("save", "save").start();

		}catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
