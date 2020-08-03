package com.make.equo.testapp;

import java.io.IOException;
import java.net.URISyntaxException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.osgi.service.component.annotations.Component;

import com.make.equo.application.api.IEquoApplication;
import com.make.equo.application.model.EquoApplicationBuilder;
import com.make.equo.application.model.EquoApplicationModel;
import com.make.equo.application.model.EquoMenu;
import com.make.equo.application.model.EquoMenuItem;
import com.make.equo.application.model.EquoMenuModel;

@Component
public class TestApp implements IEquoApplication {

	@Override
	public EquoApplicationBuilder buildApp(EquoApplicationBuilder appBuilder) {
		final EquoApplicationModel application = EquoApplicationModel.getApplicaton();
		final EquoMenuModel model1 = new EquoMenuModel();
		final EquoMenuModel model2 = new EquoMenuModel();
		model1.addMenu(new EquoMenu("Make Technology").addItem(new EquoMenuItem("Change menu", () -> application.setMenu(model2)))
				.addItem(new EquoMenuItem("Contact US")));
		model1.addMenu(new EquoMenu("About").addItem(new EquoMenuItem("About")));
		model2.addMenu(new EquoMenu("Make Technology").addItem(new EquoMenuItem("Change menu", () -> application.setMenu(model1)))
				.addItem(new EquoMenuItem("Contact US")));
		model2.addMenu(new EquoMenu("Products").addItem(new EquoMenuItem("Chromium")).addItem(new EquoMenuItem("Equo")));

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
							application.setMenu(model1);

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
