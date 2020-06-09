package com.make.equo.filesystem.provider.handlers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import com.google.gson.JsonObject;

public class OpenFileHandler extends FileSystemHandler {
	@Override
	protected Map<String, Object> execute(JsonObject payload) {
		FileDialog dialog = new FileDialog(Display.getDefault().getShells()[0], SWT.OPEN);
		dialog.setText("Open file");
		String result = dialog.open();
		if (result == null) {
			Map<String, Object> response = new HashMap<>();
			response.put("err", 2);
			return response;
		}
		Map<String, Object> response = ReadFileHandler.readFile(result);
		return response;
	}

	@Override
	protected String getCommandName() {
		return "_OpenFile";
	}
}
