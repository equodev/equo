package com.make.equo.monaco;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.make.equo.ws.api.IEquoEventHandler;

@Component
public class EquoMonacoStandaloneEditor extends EquoMonacoEditor {

	public EquoMonacoStandaloneEditor() {
		super();
	}

	@Activate
	public void activate() {
		equoEventHandler.on("_createEditor", (JsonObject payload) -> {
			JsonElement jsonFilePath = payload.get("filePath");
			if (jsonFilePath != null) {
				String file = jsonFilePath.getAsString();
				Path filePath = FileSystems.getDefault().getPath(file);
				String content = "";
				try {
					content = Files.lines(filePath).collect(Collectors.joining("\n"));
				} catch (IOException e) {
				}
				handleCreateEditor(content, file);
			}
			handleCreateEditor("", "");
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
