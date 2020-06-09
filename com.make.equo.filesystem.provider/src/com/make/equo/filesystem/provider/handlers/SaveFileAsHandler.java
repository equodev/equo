package com.make.equo.filesystem.provider.handlers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import com.google.gson.JsonObject;

public class SaveFileAsHandler extends SaveFileHandler {
	@Override
	protected Map<String, Object> execute(JsonObject payload) {
		FileDialog dialog = new FileDialog(Display.getDefault().getShells()[0], SWT.SAVE);
		dialog.setText("Save file");
		String result = dialog.open();
		if (result == null) {
			Map<String, Object> response = new HashMap<>();
			response.put("err", 2);
			return response;
		}
		Map<String, Object> response = writeFile(result, getContentParam(payload));
		return response;
	}

	@Override
	protected String getCommandName() {
		return "_SaveFileAs";
	}
}
