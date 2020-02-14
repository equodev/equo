package com.make.equo.testapp;

import java.io.IOException;
import java.net.URISyntaxException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.osgi.service.component.annotations.Component;

import com.make.equo.application.api.IEquoApplication;
import com.make.equo.application.model.EquoApplicationBuilder;

@Component
public class TestApp implements IEquoApplication {

	@Override
	public EquoApplicationBuilder buildApp(EquoApplicationBuilder appBuilder) {
		try {

			return appBuilder.plainApp("index.html").enableAnalytics().withToolbar()
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
					}).start();

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
