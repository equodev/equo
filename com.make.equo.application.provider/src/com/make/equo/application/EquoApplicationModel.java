package com.make.equo.application;

import org.eclipse.e4.ui.model.application.MApplication;
import org.osgi.service.component.annotations.Component;

@Component(service = EquoApplicationModel.class)
public class EquoApplicationModel {

	private MApplication mainApplication;

	public MApplication getMainApplication() {
		return mainApplication;
	}

	public void setMainApplication(MApplication mainApplication) {
		this.mainApplication = mainApplication;
	}

}
