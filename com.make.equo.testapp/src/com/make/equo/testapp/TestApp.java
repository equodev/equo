	package com.make.equo.testapp;

import java.io.IOException;
import java.net.URISyntaxException;

import org.osgi.service.component.annotations.Component;

import com.make.equo.application.api.IEquoApplication;
import com.make.equo.application.model.EquoApplicationBuilder;
import com.make.equo.application.model.OptionalViewBuilder;



@Component
public class TestApp implements IEquoApplication {

	@Override
	public EquoApplicationBuilder buildApp(EquoApplicationBuilder appBuilder) {
		try {
			
			 return appBuilder
			        .withSingleView("https://www.maketechnology.io")
			        .enableAnalytics()
			        .withCustomScript("js/testAnalytics.js")
			        .withCustomScript("js/testLogging.js")
			        .withMainMenu("File")
			         	.addMenuItem("New")
			         	.onClick(new Runnable() {
							
							@Override
							public void run() {
								System.out.println("ON_NEW");
								
							}
						})
			         	.onAbout(new Runnable() {
							
							@Override
							public void run() {
								System.out.println("ON_ABOUT");
								
							}
						})
			         	.onPreferences(new Runnable() {
							
							@Override
							public void run() {
								System.out.println("ON_PREFERENCES");
								
							}
						})
			         	.onBeforeExit(new Runnable() {
							
							@Override
							public void run() {
								System.out.println("Bye Bye Equo");
								
							}
						})
			         .addMenuItem("Test")
			         .onClick(new Runnable() {
							
							@Override
							public void run() {
								System.out.println("Testeando"); 
								
								//MessageDialog dialog = new MessageDialog(null, "hola", null, "Hola soy el mensaje", MessageDialog.INFORMATION ,new String[] {"Boton 1"}, 0);
								
							}
						})
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
