package com.make.equo.filesystem.provider.handlers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

public class DeleteFileHandler extends FileSystemHandler {
	@Override
	protected String getCommandName() {
		return "_DeleteFile";
	}

	@Override
	protected Map<String, Object> execute(JsonObject payload) {
		Map<String, Object> response = new HashMap<>();
		boolean result = new File(getPathParam(payload)).delete();
		if (result)
			response.put("ok", "ok");
		else
			response.put("err", 1);
		return response;
	}

}