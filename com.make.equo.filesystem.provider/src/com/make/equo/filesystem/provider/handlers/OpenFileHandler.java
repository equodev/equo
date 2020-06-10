package com.make.equo.filesystem.provider.handlers;

import java.io.File;

import com.google.gson.JsonObject;
import com.make.equo.filesystem.provider.responses.ErrResponse;

public class OpenFileHandler extends FileSystemHandler {
	@Override
	protected Object execute(JsonObject payload) {
		File file = equoFileSystem.openFile();
		if (file == null) {
			return new ErrResponse();
		}
		return ReadFileHandler.readFile(file, equoFileSystem);
	}

	@Override
	protected String getCommandName() {
		return "_OpenFile";
	}
}
