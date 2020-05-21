package com.make.equo.application.handlers.filesystem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.eclipse.core.commands.IParameter;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.ui.IWorkbenchCommandConstants;

import com.make.equo.application.util.IConstants;
import com.make.equo.contribution.api.handler.CommandParameter;
import com.make.equo.contribution.api.handler.ParameterizedHandler;
import com.make.equo.ws.api.IEquoEventHandler;

public class FileInfoHandler extends ParameterizedHandler {
	@Execute
	public void execute(@Named(IConstants.EQUO_WEBSOCKET_PARAMS_RESPONSE_ID) String idResponse,
			@Named(IConstants.EQUO_WEBSOCKET_PARAMS_FILE_PATH) String path, IEquoEventHandler eventHandler) {
		Map<String, Object> response = fileInfo(path);
		eventHandler.send(idResponse, response);
	}

	public static Map<String, Object> fileInfo(String path) {
		Map<String, Object> response = new HashMap<>();
		if (path == null) {
			response.put("err", 1);
			return response;
		}

		File file = new File(path);
		response = getItem(file);

		if (file.isDirectory()) {
			List<Map<String, Object>> children = new ArrayList<>();
			for (File f : file.listFiles()) {
				children.add(getItem(f));
			}
			response.put("children", children);
		}
		return response;
	}

	private static Map<String, Object> getItem(File file) {
		Map<String, Object> response = new HashMap<>();
		response.put("exists", file.exists());
		response.put("path", file.getAbsolutePath());
		response.put("name", file.getName());
		response.put("read", file.canRead());
		response.put("write", file.canWrite());
		response.put("execute", file.canExecute());
		response.put("isDirectory", file.isDirectory());
		return response;
	}

	@Override
	protected String getCommandId() {
		return IWorkbenchCommandConstants.FILE_PROPERTIES;
	}

	@Override
	protected String getCategoryName() {
		return "someCategory";
	}

	@Override
	protected IParameter[] getParameters() {
		IParameter[] parameters = { new CommandParameter(IConstants.EQUO_WEBSOCKET_PARAMS_RESPONSE_ID, "Response Id"),
				new CommandParameter(IConstants.EQUO_WEBSOCKET_PARAMS_FILE_PATH, "File Path") };
		return parameters;
	}

}
