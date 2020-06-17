package com.make.equo.filesystem.provider.handlers;

import java.io.File;

import com.google.gson.JsonObject;
import com.make.equo.filesystem.provider.responses.ErrResponse;

public class OpenFolderHandler extends FileSystemHandler {
	@Override
	protected Object execute(JsonObject payload) {
		File folder = equoFileSystem.openFolder();
		if (folder == null) {
			return new ErrResponse();
		}
		return equoFileSystem.fileInfo(folder);
	}

	@Override
	protected String getCommandName() {
		return "_OpenFolder";
	}
}
