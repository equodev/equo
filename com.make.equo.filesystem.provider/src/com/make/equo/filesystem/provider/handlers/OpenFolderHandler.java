package com.make.equo.filesystem.provider.handlers;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;

import com.google.gson.JsonObject;

public class OpenFolderHandler extends FileSystemHandler {
	@Override
	protected Map<String, Object> execute(JsonObject payload) {
		DirectoryDialog dialog = new DirectoryDialog(Display.getDefault().getShells()[0], SWT.OPEN);
		dialog.setText("Open Folder");
		String result = dialog.open();
		Map<String, Object> response = FileInfoHandler.fileInfo(result);
		return response;
	}

	@Override
	protected String getCommandName() {
		return "_OpenFolder";
	}
}
