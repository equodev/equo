package com.make.equo.filesystem.provider.handlers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

public class SaveFileHandler extends FileSystemHandler {
	@Override
	protected Map<String, Object> execute(JsonObject payload) {
		Map<String, Object> response = writeFile(getPathParam(payload), getContentParam(payload));
		return response;
	}

	@Override
	protected String getCommandName() {
		return "_SaveFile";
	}

	protected static Map<String, Object> writeFile(String path, String content) {
		Map<String, Object> response = new HashMap<>();
		PrintWriter writer;
		try {
			writer = new PrintWriter(new File(path));
			writer.print(content);
			writer.close();
			response.put("ok", "ok");
		} catch (FileNotFoundException e) {
			response.put("err", 1);
		}
		return response;
	}

}
