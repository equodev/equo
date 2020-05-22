package com.make.equo.application.handlers.filesystem;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Named;

import org.eclipse.core.commands.IParameter;
import org.eclipse.e4.core.di.annotations.Execute;

import com.make.equo.application.util.IConstants;
import com.make.equo.contribution.api.handler.CommandParameter;
import com.make.equo.contribution.api.handler.ParameterizedHandler;
import com.make.equo.ws.api.IEquoEventHandler;

public class ReadFileHandler extends ParameterizedHandler {
	@Execute
	public void execute(@Named(IConstants.EQUO_WEBSOCKET_PARAMS_RESPONSE_ID) String idResponse,
			@Named(IConstants.EQUO_WEBSOCKET_PARAMS_FILE_PATH) String path, IEquoEventHandler eventHandler) {
		Map<String, Object> response = readFile(path);
		eventHandler.send(idResponse, response);
	}

	public static Map<String, Object> readFile(String path) {
		Map<String, Object> response = new HashMap<>();
		Path filePath = FileSystems.getDefault().getPath(path);
		try {
			String content = Files.lines(filePath).collect(Collectors.joining("\n"));
			response.put("content", content);
		} catch (IOException e) {
			response.put("err", 1);
		}
		return response;
	}

	@Override
	protected String getCommandId() {
		return "org.eclipse.ui.file.read";
	}

	@Override
	protected IParameter[] getParameters() {
		IParameter[] parameters = { new CommandParameter(IConstants.EQUO_WEBSOCKET_PARAMS_RESPONSE_ID, "Response Id"),
				new CommandParameter(IConstants.EQUO_WEBSOCKET_PARAMS_FILE_PATH, "File Path") };
		return parameters;
	}
}
