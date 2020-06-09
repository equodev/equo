package com.make.equo.filesystem.provider.handlers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

public class NewFolderHandler extends FileSystemHandler {
	@Override
	protected Map<String, Object> execute(JsonObject payload) {
		Map<String, Object> response = new HashMap<>();
		File newFolder = new File(getPathParam(payload));
		if (newFolder.mkdirs()) {
			response.put("ok", "ok");
		} else {
			response.put("err", 1);
		}
		return response;
	}

	@Override
	protected String getCommandName() {
		return "_NewFolder";
	}
}
