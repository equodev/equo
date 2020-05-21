package com.make.equo.application.handlers.filesystem;

import java.io.File;
import java.util.Map;

import javax.inject.Named;

import org.eclipse.core.commands.IParameter;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.ui.IWorkbenchCommandConstants;

import com.make.equo.application.util.IConstants;
import com.make.equo.contribution.api.handler.CommandParameter;
import com.make.equo.contribution.api.handler.ParameterizedHandler;
import com.make.equo.ws.api.IEquoEventHandler;

public class RenameFileHandler extends ParameterizedHandler {
	@Execute
	public void execute(@Named(IConstants.EQUO_WEBSOCKET_PARAMS_RESPONSE_ID) String idResponse,
			@Named(IConstants.EQUO_WEBSOCKET_PARAMS_FILE_PATH) String path,
			@Named(IConstants.EQUO_WEBSOCKET_PARAMS_CONTENT) String newName, IEquoEventHandler eventHandler) {
		File oldFile = new File(path);
		File fileDest = new File(oldFile.getParentFile(), newName);
		Map<String, Object> response = MoveFileHandler.moveFile(oldFile, fileDest);
		eventHandler.send(idResponse, response);
	}

	@Override
	protected String getCommandId() {
		return IWorkbenchCommandConstants.FILE_RENAME;
	}

	@Override
	protected String getCategoryName() {
		return "someCategory";
	}

	@Override
	protected IParameter[] getParameters() {
		IParameter[] parameters = { new CommandParameter(IConstants.EQUO_WEBSOCKET_PARAMS_RESPONSE_ID, "Response Id"),
				new CommandParameter(IConstants.EQUO_WEBSOCKET_PARAMS_FILE_PATH, "File Path"),
				new CommandParameter(IConstants.EQUO_WEBSOCKET_PARAMS_CONTENT, "New Name") };
		return parameters;
	}

}
