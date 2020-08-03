package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.workbench.renderers.swt.MenuManagerRenderer;
import org.eclipse.swt.widgets.Display;

import com.google.gson.JsonObject;
import com.make.equo.application.handlers.ParameterizedCommandRunnable;
import com.make.equo.application.util.IConstants;

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
	
	public void openBrowser(String url, String name, String position) {
		JsonObject params = new JsonObject();
		params.addProperty("name", name);
		params.addProperty("url", url);
		params.addProperty("position", position);
		
		new ParameterizedCommandRunnable(IConstants.EQUO_WEBSOCKET_OPEN_BROWSER,
				getMainApplication().getContext()).run(params.toString());;
	}
	
	public void updateBrowser(String url, String name) {
		JsonObject params = new JsonObject();
		params.addProperty("name", name);
		params.addProperty("url", url);
		
		new ParameterizedCommandRunnable(IConstants.EQUO_WEBSOCKET_UPDATE_BROWSER,
				getMainApplication().getContext()).run(params.toString());;
	}

	public static EquoApplicationModel getApplicaton() {
		return currentModel;
	}

}
