package com.make.equo.filesystem.provider.handlers;

import org.eclipse.swt.widgets.Display;

import com.google.gson.JsonObject;
import com.make.equo.filesystem.api.IEquoFileSystem;
import com.make.equo.filesystem.provider.util.ICommandConstants;
import com.make.equo.ws.api.IEquoEventHandler;

public abstract class FileSystemHandler {
	protected IEquoFileSystem equoFileSystem;

	protected abstract Object execute(JsonObject payload);

	protected abstract String getCommandName();

	public void register(IEquoFileSystem equoFileSystem, IEquoEventHandler eventHandler) {
		this.equoFileSystem = equoFileSystem;
		eventHandler.on(getCommandName(), (JsonObject payload) -> {
			Display.getDefault().asyncExec(() -> {
				String idResponse = payload.get(ICommandConstants.PARAM_RESPONSE_ID).getAsString();
				Object response = execute(payload);
				eventHandler.send(idResponse, response);
			});
		});
	}

	protected String getPathParam(JsonObject payload) throws ClassCastException {
		return payload.get(ICommandConstants.PARAM_FILE_PATH).getAsString();
	}

	protected String getContentParam(JsonObject payload) throws ClassCastException {
		return payload.get(ICommandConstants.PARAM_CONTENT).getAsString();
	}
}
