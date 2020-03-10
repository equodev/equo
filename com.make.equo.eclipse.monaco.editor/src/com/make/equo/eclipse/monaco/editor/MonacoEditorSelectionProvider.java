package com.make.equo.eclipse.monaco.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;

public class MonacoEditorSelectionProvider implements ISelectionProvider {
	
	private List<ISelectionChangedListener> listeners = new ArrayList<>();
	
	private ISelection selection = new TextSelection(0,0);

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener arg0) {
		listeners.add(arg0);
	}

	@Override
	public ISelection getSelection() {
		return selection;
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener arg0) {
		listeners.remove(arg0);
	}

	@Override
	public void setSelection(ISelection arg0) {
		selection = arg0;
		for (ISelectionChangedListener l: listeners) {
			l.selectionChanged(new SelectionChangedEvent(this, selection));
		}
	}

}
