package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.MApplication;

public class EquoApplicationModel {

	private static EquoApplicationModel currentModel;

	private MApplication mainApplication;

	public EquoApplicationModel() {
		EquoApplicationModel.currentModel = this;
	}

	public MApplication getMainApplication() {
		return mainApplication;
	}

	public void setMainApplication(MApplication mainApplication) {
		this.mainApplication = mainApplication;
	}

	public static EquoApplicationModel getApplicaton() {
		return currentModel;
	}

}
