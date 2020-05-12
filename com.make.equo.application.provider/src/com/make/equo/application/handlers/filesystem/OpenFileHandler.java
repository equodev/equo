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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import com.make.equo.application.handlers.CommandParameter;
import com.make.equo.application.util.IConstants;
import com.make.equo.contribution.api.handler.ParameterizedHandler;
import com.make.equo.ws.api.IEquoEventHandler;

public class OpenFileHandler extends ParameterizedHandler {
	@Execute
	public void execute(@Named(IConstants.EQUO_WEBSOCKET_PARAMS_RESPONSE_ID) String idResponse, Shell shell,
			IEquoEventHandler eventHandler) {
		FileDialog dialog = new FileDialog(shell, SWT.OPEN);
		dialog.setText("Open file");
		String result = dialog.open();
		Map<String, Object> response = new HashMap<>();
		if (result == null) {
			response.put("err", 1);
			eventHandler.send(idResponse, response);
			return;
		}
		Path filePath = FileSystems.getDefault().getPath(result);
		try {
			String content = Files.lines(filePath).collect(Collectors.joining("\n"));
			response.put("content", content);
		} catch (IOException e) {
			response.put("err", 2);
		}
		eventHandler.send(idResponse, response);
	}

	@Override
	protected String getCommandId() {
		return "org.eclipse.ui.file.open";
	}

	@Override
	protected String getCategoryName() {
		return "someCategory";
	}

	@Override
	protected IParameter[] getParameters() {
		IParameter[] parameters = { new CommandParameter(IConstants.EQUO_WEBSOCKET_PARAMS_RESPONSE_ID, "Response Id") };
		return parameters;
	}
}
