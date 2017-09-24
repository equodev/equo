package com.make.equo.application.api;

import org.eclipse.e4.ui.model.application.ui.basic.MWindow;

public interface ApplicationModelService {

	void initializeAppModel(String name);

	void setMainWindowUrl(String url);

	void buildModelApp();
	
	MWindow getMainWindow();
}
