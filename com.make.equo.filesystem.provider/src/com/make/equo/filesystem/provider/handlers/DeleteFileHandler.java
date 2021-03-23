package com.make.equo.filesystem.provider.handlers;

import java.io.File;

import com.google.gson.JsonObject;
import com.make.equo.filesystem.provider.responses.ErrResponse;
import com.make.equo.filesystem.provider.responses.OkResponse;

public class DeleteFileHandler extends FileSystemHandler {
	@Override
	protected String getCommandName() {
		return "_DeleteFile";
	}

	@Override
	protected Object execute(JsonObject payload) {
		if (equoFileSystem.deleteFile(new File(getPathParam(payload))))
			return new OkResponse();
		return new ErrResponse();
	}

}