package com.make.equo.monaco.handlers;

import com.make.equo.contribution.api.handler.ParameterizedHandler;
import com.make.equo.monaco.EquoMonacoEditor;

public abstract class EditorHandler extends ParameterizedHandler {
	protected static EquoMonacoEditor editor = null;

	public static void setActiveEditor(EquoMonacoEditor editor) {
		EditorHandler.editor = editor;
	}
}
