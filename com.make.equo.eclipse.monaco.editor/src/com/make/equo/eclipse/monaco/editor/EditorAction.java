package com.make.equo.eclipse.monaco.editor;

import java.util.concurrent.Callable;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;

public class EditorAction extends Action {

	private Runnable execution;
	private Callable<Boolean> callableIsEnabled;

	public EditorAction(Runnable execution) {
		this(execution, null, null);
	}

	public EditorAction(Runnable execution, ISelectionProvider selectionProvider) {
		this(execution, selectionProvider, null);
	}

	public EditorAction(Runnable execution, ISelectionProvider selectionProvider, Callable<Boolean> callableIsEnabled) {
		this.execution = execution;
		this.callableIsEnabled = callableIsEnabled;
		setEnabled(false);
		if (selectionProvider != null)
			selectionProvider.addSelectionChangedListener(new SelectionListener());
	}

	@Override
	public boolean isEnabled() {
		if (callableIsEnabled != null)
			try {
				return callableIsEnabled.call();
			} catch (Exception e) {
				e.printStackTrace();
			}
		return super.isEnabled();
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
