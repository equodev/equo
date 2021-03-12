package com.make.equo.filesystem.provider.handlers;

import java.io.File;

import com.google.gson.JsonObject;
import com.make.equo.filesystem.provider.responses.ErrResponse;
import com.make.equo.filesystem.provider.responses.OkResponse;

public class RenameFileHandler extends FileSystemHandler {
	@Override
	protected Object execute(JsonObject payload) {
		File actualFile = new File(getPathParam(payload));
		String newName = getContentParam(payload);
		if (equoFileSystem.renameFile(actualFile, newName)) {
			return new OkResponse();
		}
		return new ErrResponse();
	}

	@Override
	protected String getCommandName() {
		return "_RenameFile";
	}
}
