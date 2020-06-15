package com.make.equo.filesystem.provider.handlers;

import java.io.File;

import com.google.gson.JsonObject;

public class MoveFileHandler extends FileSystemHandler {

	@Override
	protected Object execute(JsonObject payload) {
		File actualFile = new File(getPathParam(payload));
		File folderDest = new File(getNewPathParam(payload));
		return equoFileSystem.moveFile(actualFile, folderDest);
	}

	@Override
	protected String getCommandName() {
		return "_MoveFile";
	}

}
