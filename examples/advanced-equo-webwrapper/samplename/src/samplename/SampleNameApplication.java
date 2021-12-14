package samplename;

import org.osgi.service.component.annotations.Component;

import com.equo.application.api.IEquoApplication;
import com.equo.application.model.EquoApplicationBuilder;

@Component
public class SampleNameApplication implements IEquoApplication {

	@Override
	public EquoApplicationBuilder buildApp(EquoApplicationBuilder appBuilder) {
		return appBuilder
			.wrap("sampleurl")

			// Add custom scripts to modify the Web application
			.withCustomScript("js/actions.js")

			// Add main menues, menues, and menues items
			.withMainMenu("Equo menu")
				.addMenuItem("Equo menu item")
				// Functionality declared in actions.js
				.onClick("equoMenuItem")
				.withShortcut("M1+M")
				// Optional functionality before exiting
				.onBeforeExit(() -> {
					beforeExit();
				})
				.start();
	}

	private void beforeExit() {
		// Your code goes here
	}

}
