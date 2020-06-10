package com.make.equo.filesystem.provider.handlers;

import java.io.File;

import com.google.gson.JsonObject;
import com.make.equo.filesystem.provider.responses.ErrResponse;

public class SaveFileAsHandler extends SaveFileHandler {
	@Override
	protected Object execute(JsonObject payload) {
		String content = getContentParam(payload);
		File file = equoFileSystem.saveFileAs(content);
		if (file == null) {
			return new ErrResponse();
		}
		return equoFileSystem.fileInfo(file);
	}

	@Override
	protected String getCommandName() {
		return "_SaveFileAs";
	}
}
