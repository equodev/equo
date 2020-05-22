package com.make.equo.application.handlers.filesystem;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import org.eclipse.core.commands.IParameter;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import com.make.equo.application.util.IConstants;
import com.make.equo.contribution.api.handler.CommandParameter;
import com.make.equo.contribution.api.handler.ParameterizedHandler;
import com.make.equo.ws.api.IEquoEventHandler;

public class OpenFileHandler extends ParameterizedHandler {
	@Execute
	public void execute(@Named(IConstants.EQUO_WEBSOCKET_PARAMS_RESPONSE_ID) String idResponse, Shell shell,
			IEquoEventHandler eventHandler) {
		FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		dialog.setText("Open file");
		String result = dialog.open();
		if (result == null) {
			Map<String, Object> response = new HashMap<>();
			response.put("err", 2);
			eventHandler.send(idResponse, response);
			return;
		}
		Map<String, Object> response = ReadFileHandler.readFile(result);
		eventHandler.send(idResponse, response);
	}

	@Override
	protected String getCommandId() {
		return "org.eclipse.ui.file.open";
	}

	@Override
	protected IParameter[] getParameters() {
		IParameter[] parameters = { new CommandParameter(IConstants.EQUO_WEBSOCKET_PARAMS_RESPONSE_ID, "Response Id") };
		return parameters;
	}
}
