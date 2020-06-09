package com.make.equo.filesystem.provider.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;

public class FileInfoHandler extends FileSystemHandler {
	@Override
	protected Map<String, Object> execute(JsonObject payload) {
		return fileInfo(getPathParam(payload));
	}

	public static Map<String, Object> fileInfo(String path) {
		Map<String, Object> response = new HashMap<>();
		if (path == null) {
			response.put("err", 1);
			return response;
		}

		File file = new File(path);
		response = getItem(file);

		if (file.isDirectory()) {
			List<Map<String, Object>> children = new ArrayList<>();
			for (File f : file.listFiles()) {
				children.add(getItem(f));
			}
			response.put("children", children);
		}
		return response;
	}

	private static Map<String, Object> getItem(File file) {
		Map<String, Object> response = new HashMap<>();
		response.put("exists", file.exists());
		response.put("path", file.getAbsolutePath());
		response.put("name", file.getName());
		response.put("read", file.canRead());
		response.put("write", file.canWrite());
		response.put("execute", file.canExecute());
		response.put("isDirectory", file.isDirectory());
		return response;
	}

	@Override
	protected String getCommandName() {
		return "_FileInfo";
	}
}
