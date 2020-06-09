package com.make.equo.filesystem.provider.handlers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

public class MoveFileHandler extends FileSystemHandler {

	public static Map<String, Object> moveFile(File oldFile, File fileDest) {
		Map<String, Object> response = new HashMap<>();
		if (fileDest.exists()) {
			response.put("err", 2);
			return response;
		}
		fileDest.getParentFile().mkdirs();

		boolean result = oldFile.renameTo(fileDest);
		if (result)
			response.put("ok", "ok");
		else
			response.put("err", 1);
		return response;
	}

	@Override
	protected Map<String, Object> execute(JsonObject payload) {
		File oldFile = new File(getPathParam(payload));
		File fileDest = new File(new File(getNewPathParam(payload)), oldFile.getName());
		return moveFile(oldFile, fileDest);
	}

	@Override
	protected String getCommandName() {
		return "_MoveFile";
	}

}
