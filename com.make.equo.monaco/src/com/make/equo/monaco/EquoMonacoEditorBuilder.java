package com.make.equo.monaco;

import org.eclipse.swt.widgets.Composite;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

import com.google.gson.JsonObject;
import com.make.equo.ws.api.IEquoEventHandler;

@Component (service = EquoMonacoEditorBuilder.class, scope = ServiceScope.PROTOTYPE)
public class EquoMonacoEditorBuilder {

	@Reference
	private IEquoEventHandler equoEventHandler;
		
//	private int id;
	private Composite parent;
	private int style;
	private String contents;
	private String language;
	
	public EquoMonacoEditorBuilder() {
//		this.id = this.hashCode();
		this.style = -1;
		this.contents = "";
		this.language = "";
	}
	
	public EquoMonacoEditorBuilder withParent(Composite parent) {
		this.parent = parent;
		return this;
	}
	
	public EquoMonacoEditorBuilder withStyle(int style) {
		this.style = style;
		return this;
	}
	
	public EquoMonacoEditor create() {
		if (style == -1) {
			style = parent.getStyle();
		}
		return new EquoMonacoEditor(parent, style, equoEventHandler, contents, language);
	}
	
//	public static void createEditor(String contents, String language) {
//		equoEventHandler.on("_doCreateEditor", (IEquoRunnable<Void>) runnable -> handleCreateEditor(contents, language));
//	}
//	
//	private static void handleCreateEditor(String contents, String language) {
//		Map<String, String> editorData = new HashMap<String, String>();
//		editorData.put("text", contents);
//		editorData.put("language", language);
//		equoEventHandler.send("_createEditor", editorData);
//	}
	
	public static String getContents() {
//		equoEventHandler.on("_doSave", (JsonObject contents) -> {
//			try {
//				return handleGetContents(contents);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		});
//		equoEventHandler.send("_getContents");
		return null;
	}
	
//	private static String handleGetContents(JsonObject contentsJson) throws Exception {
//		return contentsJson.get("contents").getAsString();
//	}
	
}
