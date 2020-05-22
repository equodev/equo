package com.make.equo.application.handlers.filesystem;

import java.util.Map;

import javax.inject.Named;

import org.eclipse.core.commands.IParameter;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchCommandConstants;

import com.make.equo.application.util.IConstants;
import com.make.equo.contribution.api.handler.CommandParameter;
import com.make.equo.contribution.api.handler.ParameterizedHandler;
import com.make.equo.ws.api.IEquoEventHandler;

public class OpenFolderHandler extends ParameterizedHandler {
	@Execute
	public void execute(@Named(IConstants.EQUO_WEBSOCKET_PARAMS_RESPONSE_ID) String idResponse, Shell shell,
			IEquoEventHandler eventHandler) {
		DirectoryDialog dialog = new DirectoryDialog(shell, SWT.OPEN);
		dialog.setText("Open Folder");
		String result = dialog.open();
		Map<String, Object> response = FileInfoHandler.fileInfo(result);
		eventHandler.send(idResponse, response);
	}

	@Override
	public String getCommandId() {
		return IWorkbenchCommandConstants.PROJECT_OPEN_PROJECT;
	}

	@Override
	public IParameter[] getParameters() {
		IParameter[] parameters = { new CommandParameter(IConstants.EQUO_WEBSOCKET_PARAMS_RESPONSE_ID, "Response Id") };
		return parameters;
	}
}
