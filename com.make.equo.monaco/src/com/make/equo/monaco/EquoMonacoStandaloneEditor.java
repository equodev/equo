package com.make.equo.monaco;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.make.equo.ws.api.IEquoEventHandler;

@Component
public class EquoMonacoStandaloneEditor {

	private IEquoEventHandler equoEventHandler;

	public EquoMonacoStandaloneEditor() {
		super();
	}

	@Activate
	public void activate() {
		attendEditorCreation();
		attendLspConfig();
	}

	private void attendLspConfig() {
		equoEventHandler.on("_addLspServer", (JsonObject payload) -> {
			List<String> executionParameters = getListFromJsonArray(payload.getAsJsonArray("executionParameters"));
			List<String> extensions = getListFromJsonArray(payload.getAsJsonArray("extensions"));
			EquoMonacoEditor.addLspServer(executionParameters, extensions);
		});

		equoEventHandler.on("_addLspWsServer", (JsonObject payload) -> {
			String fullServerPath = payload.get("fullServerPath").getAsString();
			List<String> extensions = getListFromJsonArray(payload.getAsJsonArray("extensions"));
			EquoMonacoEditor.addLspWsServer(fullServerPath, extensions);
		});

		equoEventHandler.on("_removeLspServer", (JsonObject payload) -> {
			List<String> extensions = getListFromJsonArray(payload.getAsJsonArray("extensions"));
			EquoMonacoEditor.removeLspServer(extensions);
		});
	}

	protected List<String> getListFromJsonArray(JsonArray array) {
		List<String> list = new ArrayList<>();
		for (JsonElement elem : array) {
			list.add(elem.getAsString());
		}
		return list;
	}

	private void attendEditorCreation() {
		equoEventHandler.on("_createEditor", (JsonObject payload) -> {
			JsonElement jsonFilePath = payload.get("filePath");
			if (jsonFilePath != null) {
				String fileString = jsonFilePath.getAsString();
				File file = new File(fileString);
				Path filePath = FileSystems.getDefault().getPath(fileString);
				String content = "";
				try {
					content = Files.lines(filePath).collect(Collectors.joining("\n"));
					new EquoMonacoEditor(equoEventHandler).initialize(content, file.getName(), fileString);
				} catch (IOException e) {
				}
			} else {
				new EquoMonacoEditor(equoEventHandler).initialize("", "", "");
			}
		});
	}

	@Reference
	public void setEquoEventHandler(IEquoEventHandler handler) {
		equoEventHandler = handler;
	}

	public void unsetEquoEventHandler(IEquoEventHandler handler) {
		equoEventHandler = null;
	}

}
