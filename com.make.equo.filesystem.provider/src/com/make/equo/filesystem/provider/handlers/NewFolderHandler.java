package com.make.equo.filesystem.provider.handlers;

import java.io.File;

import com.google.gson.JsonObject;
import com.make.equo.filesystem.provider.responses.ErrResponse;
import com.make.equo.filesystem.provider.responses.OkResponse;

public class NewFolderHandler extends FileSystemHandler {
	@Override
	protected Object execute(JsonObject payload) {
		File newFolder = new File(getPathParam(payload));
		if (equoFileSystem.newFolder(newFolder)) {
			return new OkResponse();
		}
		return new ErrResponse();
	}

	@Override
	protected String getCommandName() {
		return "_NewFolder";
	}
}
