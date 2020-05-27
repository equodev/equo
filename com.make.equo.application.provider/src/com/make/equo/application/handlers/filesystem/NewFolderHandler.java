package com.make.equo.application.handlers.filesystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import org.eclipse.core.commands.IParameter;
import org.eclipse.e4.core.di.annotations.Execute;

import com.make.equo.application.util.IConstants;
import com.make.equo.contribution.api.handler.CommandParameter;
import com.make.equo.contribution.api.handler.ParameterizedHandler;
import com.make.equo.ws.api.IEquoEventHandler;

public class NewFolderHandler extends ParameterizedHandler {
	@Execute
	public void execute(@Named(IConstants.EQUO_WEBSOCKET_PARAMS_RESPONSE_ID) String idResponse,
			@Named(IConstants.EQUO_WEBSOCKET_PARAMS_FILE_PATH) String path, IEquoEventHandler eventHandler) {
		Map<String, Object> response = new HashMap<>();
		File newFolder = new File(path);
		if (newFolder.mkdirs()) {
			response.put("ok", "ok");
		} else {
			response.put("err", 1);
		}
		eventHandler.send(idResponse, response);
	}

	@Override
	public String getCommandId() {
		return "org.eclipse.ui.file.newFolder";
	}

	@Override
	public IParameter[] getParameters() {
		IParameter[] parameters = { new CommandParameter(IConstants.EQUO_WEBSOCKET_PARAMS_RESPONSE_ID, "Response Id"),
				new CommandParameter(IConstants.EQUO_WEBSOCKET_PARAMS_FILE_PATH, "File Path") };
		return parameters;
	}

}
