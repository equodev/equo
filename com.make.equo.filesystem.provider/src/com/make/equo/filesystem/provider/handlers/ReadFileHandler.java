package com.make.equo.filesystem.provider.handlers;

import java.io.File;

import com.google.gson.JsonObject;
import com.make.equo.filesystem.api.IEquoFileSystem;
import com.make.equo.filesystem.provider.responses.ContentResponse;
import com.make.equo.filesystem.provider.responses.ErrResponse;

public class ReadFileHandler extends FileSystemHandler {
	@Override
	protected Object execute(JsonObject payload) {
		File file = new File(getPathParam(payload));
		return readFile(file, equoFileSystem);
	}

	protected static Object readFile(File file, IEquoFileSystem equoFileSystem) {
		String content = equoFileSystem.readFile(file);
		if (content == null) {
			return new ErrResponse();
		}
		ContentResponse response = new ContentResponse(content);
		return response;
	}

	@Override
	protected String getCommandName() {
		return "_ReadFile";
	}
}
