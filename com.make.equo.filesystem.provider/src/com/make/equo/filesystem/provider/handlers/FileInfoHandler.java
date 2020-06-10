package com.make.equo.filesystem.provider.handlers;

import java.io.File;

import com.google.gson.JsonObject;

public class FileInfoHandler extends FileSystemHandler {
	@Override
	protected Object execute(JsonObject payload) {
		return equoFileSystem.fileInfo(new File(getPathParam(payload)));
	}

	@Override
	protected String getCommandName() {
		return "_FileInfo";
	}
}
