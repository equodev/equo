package com.make.equo.filesystem.provider.handlers;

import java.io.File;

import com.google.gson.JsonObject;
import com.make.equo.filesystem.provider.responses.ContentResponse;
import com.make.equo.filesystem.provider.responses.ErrResponse;

public class MoveFileHandler extends FileSystemHandler {

	@Override
	protected Object execute(JsonObject payload) {
		File actualFile = new File(getPathParam(payload));
		File folderDest = new File(getContentParam(payload));
		String response = equoFileSystem.moveFile(actualFile, folderDest);
		if (response != null) {
			return new ContentResponse(response);
		}
		return new ErrResponse();
	}

	@Override
	protected String getCommandName() {
		return "_MoveFile";
	}

}
