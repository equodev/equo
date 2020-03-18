package com.make.equo.eclipse.monaco.editor;

import org.eclipse.jface.action.Action;

public class EditorAction extends Action {

	private Runnable execution;

	public EditorAction(Runnable execution) {
		this.execution = execution;
		setEnabled(false);
	}

	@Override
	public void run() {
		execution.run();
	}

}
