package com.make.equo.monaco.handlers;

import org.eclipse.core.commands.IParameter;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.ui.IWorkbenchCommandConstants;

public class FindHandler extends EditorHandler {
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