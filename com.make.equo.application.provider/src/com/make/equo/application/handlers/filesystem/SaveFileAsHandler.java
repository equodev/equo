package com.make.equo.application.handlers.filesystem;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import org.eclipse.core.commands.IParameter;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchCommandConstants;

import com.make.equo.application.util.IConstants;
import com.make.equo.contribution.api.handler.CommandParameter;
import com.make.equo.ws.api.IEquoEventHandler;

public class SaveFileAsHandler extends SaveFileHandler {
	@Execute
	public void execute(@Named(IConstants.EQUO_WEBSOCKET_PARAMS_RESPONSE_ID) String idResponse,
			@Named(IConstants.EQUO_WEBSOCKET_PARAMS_CONTENT) String content, Shell shell,
			IEquoEventHandler eventHandler) {
		FileDialog dialog = new FileDialog(shell, SWT.SAVE);
		dialog.setText("Save file");
		String result = dialog.open();
		if (result == null) {
			Map<String, Object> response = new HashMap<>();
			response.put("err", 2);
			eventHandler.send(idResponse, response);
			return;
		}
		Map<String, Object> response = writeFile(result, content);
		eventHandler.send(idResponse, response);
	}

	@Override
	public String getCommandId() {
		return IWorkbenchCommandConstants.FILE_SAVE_AS;
	}

	@Override
	public IParameter[] getParameters() {
		IParameter[] parameters = { new CommandParameter(IConstants.EQUO_WEBSOCKET_PARAMS_RESPONSE_ID, "Response Id"),
				new CommandParameter(IConstants.EQUO_WEBSOCKET_PARAMS_CONTENT, "Content") };
		return parameters;
	}
}
