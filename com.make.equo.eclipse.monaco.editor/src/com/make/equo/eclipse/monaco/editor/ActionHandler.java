package com.make.equo.eclipse.monaco.editor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;

public class ActionHandler extends AbstractHandler {
	private boolean enable;
	private Runnable execution;
	private boolean alwaysEnabled;
	
	ActionHandler(ISelectionProvider selectionProvider, Runnable execution){
		this(selectionProvider, execution, false);
	}
	
	ActionHandler(ISelectionProvider selectionProvider, Runnable execution, boolean alwaysEnabled){
		if (!alwaysEnabled && selectionProvider != null)
			selectionProvider.addSelectionChangedListener(new MyListener());
		enable = false;
		this.execution = execution;
		this.alwaysEnabled = alwaysEnabled;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		execution.run();
		return null;
	}

	public void setEnabled(Boolean state) {
		enable = state;
	}

	@Override
	public boolean isEnabled() {
		return alwaysEnabled || enable;
	}

	private class MyListener implements ISelectionChangedListener {

		@Override
		public void selectionChanged(SelectionChangedEvent e) {
			enable = !e.getSelection().isEmpty();
		}

	}
}
