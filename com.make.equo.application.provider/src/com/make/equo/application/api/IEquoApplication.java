package com.make.equo.application.api;

import com.google.gson.JsonObject;
import com.make.equo.application.handlers.ParameterizedCommandRunnable;
import com.make.equo.application.model.EquoApplicationBuilder;
import com.make.equo.application.model.EquoApplicationModel;
import com.make.equo.application.util.IConstants;

public interface IEquoApplication {

	public EquoApplicationBuilder buildApp(EquoApplicationBuilder appBuilder);
	public static void openBrowser(String url, String name, String position) {
		JsonObject params = new JsonObject();
		params.addProperty("name", name);
		params.addProperty("url", url);
		params.addProperty("position", position);
		
		new ParameterizedCommandRunnable(IConstants.EQUO_WEBSOCKET_OPEN_BROWSER,
				EquoApplicationModel.getApplicaton().getMainApplication().getContext()).run(params.toString());
	};
	
	public static void updateBrowser(String url, String name) {
		JsonObject params = new JsonObject();
		params.addProperty("name", name);
		params.addProperty("url", url);
		
		new ParameterizedCommandRunnable(IConstants.EQUO_WEBSOCKET_UPDATE_BROWSER,
				EquoApplicationModel.getApplicaton().getMainApplication().getContext()).run(params.toString());
	};

}
