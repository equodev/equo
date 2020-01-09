package com.make.equo.monaco;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.make.equo.ws.api.IEquoEventHandler;

@Component
public class EquoMonacoApi {

	@Reference
	private IEquoEventHandler equoEventHandler;
	
	public void createEditor(String contents, String language) {
		Map<String, String> editorData = new HashMap<String, String>();
		editorData.put("text", contents);
		editorData.put("language", language);
		equoEventHandler.send("_createEditor", editorData);
	}
	
}
