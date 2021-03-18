package com.make.equo.poc;

import java.net.URISyntaxException;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.osgi.service.component.annotations.Component;

import com.make.equo.application.api.IEquoApplication;
import com.make.equo.application.model.EquoApplicationBuilder;
import com.make.equo.poc.eventhandlers.ExitAppHandler;

@Component
public class EquoApp implements IEquoApplication {

	@Override
	public EquoApplicationBuilder buildApp(EquoApplicationBuilder appBuilder) {
		try {
			return appBuilder.plainApp("index.html").beforeExit(() -> {
				return new ExitAppHandler().showDialog() == IDialogConstants.OK_ID;
			}).start();

		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

}
