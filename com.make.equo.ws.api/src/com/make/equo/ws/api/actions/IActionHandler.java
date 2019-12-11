package com.make.equo.ws.api.actions;

import com.google.gson.JsonObject;

public interface IActionHandler extends IEquoCallable<JsonObject>{
	
	
	public Object call(JsonObject payload);
}
