package com.make.equo.monaco;

import org.eclipse.core.commands.IParameter;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.ui.IWorkbenchCommandConstants;

import com.make.equo.contribution.api.handler.ParameterizedHandler;

public class FindHandler extends ParameterizedHandler {
	private EquoMonacoEditor editor;

	public FindHandler(EquoMonacoEditor editor) {
		this.editor = editor;
	}

	@Execute
	public void execute() {
		editor.find();
	}

	@Override
	protected String getCommandId() {
		return IWorkbenchCommandConstants.EDIT_FIND_AND_REPLACE;
	}

	@Override
	protected String getCategoryName() {
		return "someCategory";
	}

	@Override
	protected IParameter[] getParameters() {
		return null;
	}

}