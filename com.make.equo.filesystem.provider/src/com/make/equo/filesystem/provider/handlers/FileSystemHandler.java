package com.make.equo.filesystem.provider.handlers;

import java.util.Map;

import com.google.gson.JsonObject;
import com.make.equo.filesystem.provider.util.ICommandConstants;
import com.make.equo.ws.api.IEquoEventHandler;

public abstract class FileSystemHandler {
	protected abstract Map<String, Object> execute(JsonObject payload);

	protected abstract String getCommandName();

	public void register(IEquoEventHandler eventHandler) {
		eventHandler.on(getCommandName(), (JsonObject payload) -> {
			String idResponse = payload.get(ICommandConstants.PARAM_RESPONSE_ID).getAsString();
			Map<String, Object> response = execute(payload);
			eventHandler.send(idResponse, response);
		});
	}

	protected String getPathParam(JsonObject payload) throws ClassCastException {
		return payload.get(ICommandConstants.PARAM_FILE_PATH).getAsString();
	}

	protected String getNewPathParam(JsonObject payload) throws ClassCastException {
		return payload.get(ICommandConstants.PARAM_NEW_PATH).getAsString();
	}

	protected String getContentParam(JsonObject payload) throws ClassCastException {
		return payload.get(ICommandConstants.PARAM_CONTENT).getAsString();
	}
}
