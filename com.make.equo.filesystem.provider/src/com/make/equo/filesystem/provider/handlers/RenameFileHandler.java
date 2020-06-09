package com.make.equo.filesystem.provider.handlers;

import java.io.File;
import java.util.Map;

import com.google.gson.JsonObject;

public class RenameFileHandler extends FileSystemHandler {
	@Override
	protected Map<String, Object> execute(JsonObject payload) {
		File oldFile = new File(getPathParam(payload));
		File fileDest = new File(oldFile.getParentFile(), getNewPathParam(payload));
		Map<String, Object> response = MoveFileHandler.moveFile(oldFile, fileDest);
		return response;
	}

	@Override
	protected String getCommandName() {
		return "_RenameFile";
	}
}
