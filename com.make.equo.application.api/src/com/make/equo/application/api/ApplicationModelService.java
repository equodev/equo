package com.make.equo.application.api;

import org.eclipse.e4.ui.model.application.ui.basic.MWindow;

import com.make.equo.application.model.IMenuHandler;

public interface ApplicationModelService {

	void initializeAppModel(String name);

	void setMainWindowUrl(String url);

	void buildModelApp();
	
	MWindow getMainWindow();

	String addMenu(String menuLabel);

	String addMenuItem(String parentId, String menuItemlabel);

	void addHandler(String handlerId,  IMenuHandler menuHanlder);

	String addMenu(String parentId, String label);
}
