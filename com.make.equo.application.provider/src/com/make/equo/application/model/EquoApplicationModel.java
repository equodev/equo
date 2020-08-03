package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.workbench.renderers.swt.MenuManagerRenderer;
import org.eclipse.swt.widgets.Display;

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

	public void setMenu(EquoMenuModel model) {
		final EquoApplicationBuilder currentBuilder = EquoApplicationBuilder.getCurrentBuilder();
		final OptionalViewBuilder optionalViewBuilder = currentBuilder.getOptionalViewBuilder();
		Display.getDefault().asyncExec(() -> {
			optionalViewBuilder.inicMainMenu();
			MenuBuilder menuBuilder = new MenuBuilder(optionalViewBuilder);
			menuBuilder.remove();
			model.implement(menuBuilder);
			MMenu mainMenu = optionalViewBuilder.getMainMenu();
			if (mainMenu == null) {
				mainMenu = currentBuilder.getmWindow().getMainMenu();
			}
			MenuManagerRenderer renderer = (MenuManagerRenderer) mainMenu.getRenderer();
			if (renderer != null) {
				renderer.getManager(mainMenu).update(true);
			}
		});
	}

	public static EquoApplicationModel getApplicaton() {
		return currentModel;
	}

}
