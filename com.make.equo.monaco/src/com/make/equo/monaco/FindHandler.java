package com.make.equo.monaco;

import org.eclipse.core.commands.IParameter;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.ui.IWorkbenchCommandConstants;

import com.make.equo.contribution.api.handler.ParameterizedHandler;

public class FindHandler extends ParameterizedHandler {
	private static EquoMonacoEditor editor;

	public FindHandler setEditor(EquoMonacoEditor editor) {
		FindHandler.editor = editor;
		return this;
	}

	@Execute
	public void execute() {
		editor.find();
	}

	@Override
	public String getCommandId() {
		return IWorkbenchCommandConstants.EDIT_FIND_AND_REPLACE;
	}

	@Override
	public IParameter[] getParameters() {
		return null;
	}

	@Override
	public String getShortcut() {
		return "M1+F";
	}

}