package com.make.equo.testapp;

import java.io.IOException;
import java.net.URISyntaxException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.osgi.service.component.annotations.Component;

import com.make.equo.application.api.IEquoApplication;
import com.make.equo.application.model.EquoApplicationBuilder;
import com.make.equo.application.model.EquoMainMenu;

@Component
public class TestApp implements IEquoApplication {

	@Override
	public EquoApplicationBuilder buildApp(EquoApplicationBuilder appBuilder) {
		final EquoMainMenu model1 = new EquoMainMenu();
		final EquoMainMenu model2 = new EquoMainMenu();

		model1.withMainMenu("Make Technology")
			.addMenuItem("Change Menu").onClick(() -> model2.setApplicationMenu())
			.addMenuItem("Contact Us")
		.withMainMenu("About")
			.addMenuItem("About");

		model2.withMainMenu("Make Technology")
		.addMenuItem("Change Menu").onClick(() -> model1.setApplicationMenu())
		.addMenuItem("Contact Us")
		.withMainMenu("Products")
			.addMenuItem("Chromium")
			.addMenuItem("Equo");

		try {

			return appBuilder.plainApp("index.html").enableAnalytics()
					.withCustomScript("js/testAnalytics.js").withCustomScript("js/testLogging.js").withMainMenu("File")
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
					}).withMainMenu("Dialog Test").addMenuItem("Message Dialog").onClick(new Runnable() {

						@Override
						public void run() {
							try {
								MessageDialog.openInformation(null, "info dialog", "info msg");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}).addMenuItem("Message Dialog with Toggle").onClick(new Runnable() {

						@Override
						public void run() {
							try {
								MessageDialogWithToggle.openYesNoQuestion(null, "mensaje en dialogo", "soy el mensaje",
										"soy el toggle del mensaje", true, null, "key");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}).withMainMenu("Menu Test").addMenuItem("Change menu").onClick(new Runnable() {

						@Override
						public void run() {
							model1.setApplicationMenu();
						}
					}).addMenuItem("Open New Browser").onClick(new Runnable() {
						
						@Override
						public void run() {
							IEquoApplication.openBrowser("https://www.maketechnology.io", "test", "left");
						}
					}).addMenuItem("Update Browser").onClick(new Runnable() {
						
						@Override
						public void run() {
							IEquoApplication.updateBrowser("https://www.linkedin.com/company/make-technology", "test");
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

	private void createInfoToggleDialog() {
		try {
			MessageDialogWithToggle.openYesNoQuestion(null, "mensaje en dialogo", "soy el mensaje",
					"soy el toggle del mensaje", true, null, "key");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createInfoDialog() {
		try {
			MessageDialog.openInformation(null, "info dialog", "info msg");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
