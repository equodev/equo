package com.make.equo.filesystem.provider.handlers;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

public class ReadFileHandler extends FileSystemHandler {
	@Override
	protected Map<String, Object> execute(JsonObject payload) {
		Map<String, Object> response = readFile(getPathParam(payload));
		return response;
	}

	@Override
	protected String getCommandName() {
		return "_ReadFile";
	}

	public static Map<String, Object> readFile(String path) {
		Map<String, Object> response = new HashMap<>();
		Path filePath = FileSystems.getDefault().getPath(path);
		try {
			String content = Files.lines(filePath).collect(Collectors.joining("\n"));
			response.put("content", content);
		} catch (IOException e) {
			response.put("err", 1);
		}
		return response;
	}
}
