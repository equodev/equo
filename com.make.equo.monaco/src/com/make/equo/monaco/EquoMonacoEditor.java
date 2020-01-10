package com.make.equo.monaco;

import static com.make.equo.monaco.util.IMonacoConstants.EQUO_MONACO_CONTRIBUTION_NAME;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;

import com.google.gson.JsonObject;
import com.make.equo.ws.api.IEquoEventHandler;
import com.make.equo.ws.api.IEquoRunnable;
import com.make.swtcef.Chromium;

public class EquoMonacoEditor {

	private Chromium browser;
	private IEquoEventHandler equoEventHandler;
	
	public EquoMonacoEditor(Composite parent, int style, IEquoEventHandler handler, String contents, String language) {
		this.equoEventHandler = handler;
		browser = new Chromium(parent, style);
		browser.setUrl("http://" + EQUO_MONACO_CONTRIBUTION_NAME);
		createEditor(contents, language);
	}

	private void createEditor(String contents, String language) {
		equoEventHandler.on("_doCreateEditor",
				(IEquoRunnable<Void>) runnable -> handleCreateEditor(contents, language));
	}

	private void handleCreateEditor(String contents, String language) {
		Map<String, String> editorData = new HashMap<String, String>();
		editorData.put("text", contents);
		editorData.put("language", language);
		equoEventHandler.send("_createEditor", editorData);
	}
	
	public void getContents(IEquoRunnable<String> runnable) {
		equoEventHandler.on("_doGetContents", (JsonObject contents) -> {
			runnable.run(contents.get("contents").getAsString());
		});
		equoEventHandler.send("_getContents");
	}

}
