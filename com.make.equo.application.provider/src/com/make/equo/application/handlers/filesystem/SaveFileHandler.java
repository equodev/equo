package com.make.equo.application.handlers.filesystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import org.eclipse.core.commands.IParameter;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.ui.IWorkbenchCommandConstants;

import com.make.equo.application.util.IConstants;
import com.make.equo.contribution.api.handler.CommandParameter;
import com.make.equo.contribution.api.handler.ParameterizedHandler;
import com.make.equo.ws.api.IEquoEventHandler;

public class SaveFileHandler extends ParameterizedHandler {
	@Execute
	public void execute(@Named(IConstants.EQUO_WEBSOCKET_PARAMS_RESPONSE_ID) String idResponse,
			@Named(IConstants.EQUO_WEBSOCKET_PARAMS_FILE_PATH) String path,
			@Named(IConstants.EQUO_WEBSOCKET_PARAMS_CONTENT) String content, IEquoEventHandler eventHandler) {
		Map<String, Object> response = writeFile(path, content);
		eventHandler.send(idResponse, response);
	}

	protected static Map<String, Object> writeFile(String path, String content) {
		Map<String, Object> response = new HashMap<>();
		PrintWriter writer;
		try {
			writer = new PrintWriter(new File(path));
			writer.print(content);
			writer.close();
			response.put("ok", "ok");
		} catch (FileNotFoundException e) {
			response.put("err", 1);
		}
		return response;
	}

	@Override
	public String getCommandId() {
		return IWorkbenchCommandConstants.FILE_SAVE;
	}

	@Override
	public IParameter[] getParameters() {
		IParameter[] parameters = { new CommandParameter(IConstants.EQUO_WEBSOCKET_PARAMS_RESPONSE_ID, "Response Id"),
				new CommandParameter(IConstants.EQUO_WEBSOCKET_PARAMS_FILE_PATH, "File Path"),
				new CommandParameter(IConstants.EQUO_WEBSOCKET_PARAMS_CONTENT, "Content") };
		return parameters;
	}

}
