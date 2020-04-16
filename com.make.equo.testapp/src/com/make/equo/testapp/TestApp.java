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

			return appBuilder.plainApp("index.html").enableAnalytics().withCustomScript("js/testAnalytics.js")
					.withCustomScript("js/testLogging.js")
					.withMainMenu("File")
						.addMenuItem("New").onClick(() -> System.out.println("ON_NEW"))
						.onAbout(() -> System.out.println("ON_ABOUT"))
						.onPreferences(() -> System.out.println("ON_PREFERENCES"))
						.onBeforeExit(() -> System.out.println("Bye Bye Equo"))
					.withMainMenu("Dialog Test")
						.addMenuItem("Message Dialog").onClick(() -> createInfoDialog())
						.addMenuItem("Message Dialog with Toggle").onClick(() -> createInfoToggleDialog())
					.withToolbar()
						.addToolItem("chat", "Chat").onClick(() -> System.out.println("click en ToolItem"))
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
