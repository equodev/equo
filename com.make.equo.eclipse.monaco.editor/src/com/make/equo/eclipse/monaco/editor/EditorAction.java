package com.make.equo.eclipse.monaco.editor;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;

public class EditorAction extends Action {

	private Runnable execution;

	public EditorAction(Runnable execution) {
		this(execution, null);
	}

	public EditorAction(Runnable execution, ISelectionProvider selectionProvider) {
		this.execution = execution;
		setEnabled(false);
		if (selectionProvider != null)
			selectionProvider.addSelectionChangedListener(new SelectionListener());
	}

	@Override
	public void run() {
		execution.run();
	}

	private class SelectionListener implements ISelectionChangedListener {

		@Override
		public void selectionChanged(SelectionChangedEvent e) {
			setEnabled(!e.getSelection().isEmpty());
		}

	}

}
