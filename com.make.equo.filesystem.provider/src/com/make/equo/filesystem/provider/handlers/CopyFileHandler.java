package com.make.equo.filesystem.provider.handlers;

import java.io.File;

import com.google.gson.JsonObject;
import com.make.equo.filesystem.provider.responses.ErrResponse;
import com.make.equo.filesystem.provider.responses.OkResponse;

public class CopyFileHandler extends FileSystemHandler {

	@Override
	protected Object execute(JsonObject payload) {
		File actualFile = new File(getPathParam(payload));
		File folderDest = new File(getContentParam(payload));
		if (equoFileSystem.copyFile(actualFile, folderDest)) {
			return new OkResponse();
		}
		return new ErrResponse();
	}

	@Override
	protected String getCommandName() {
		return "_CopyFile";
	}

}
