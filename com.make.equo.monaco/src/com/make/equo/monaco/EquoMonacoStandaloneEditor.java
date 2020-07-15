package com.make.equo.monaco;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.make.equo.filesystem.api.IEquoFileSystem;
import com.make.equo.ws.api.IEquoEventHandler;

@Component
public class EquoMonacoStandaloneEditor {

	private IEquoEventHandler equoEventHandler;

	@Reference
	private IEquoFileSystem equoFileSystem;

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
				String filePath = jsonFilePath.getAsString();
				File file = new File(filePath);
				String content = equoFileSystem.readFile(new File(filePath));
				if (content != null) {
					new EquoMonacoEditor(equoEventHandler, equoFileSystem).initialize(content, file.getName(),
							filePath);
				} else {
					if (!file.exists()) {
						new EquoMonacoEditor(equoEventHandler, equoFileSystem).initialize("", file.getName(), filePath);
					}
				}
			} else {
				new EquoMonacoEditor(equoEventHandler, equoFileSystem).initialize("", "", "");
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
