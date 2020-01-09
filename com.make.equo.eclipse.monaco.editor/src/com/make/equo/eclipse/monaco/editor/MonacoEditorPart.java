package com.make.equo.eclipse.monaco.editor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

import com.make.swtcef.Chromium;

public class MonacoEditorPart extends EditorPart {

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setInput(input);
		setSite(site);

	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		IEditorInput input = getEditorInput();
		setPartName(input.getName());
		if (input instanceof FileEditorInput) {
			FileEditorInput fileInput = (FileEditorInput) input;
			setTitleToolTip(fileInput.getPath().toString());

			try {
				InputStream contents = fileInput.getFile().getContents();
				InputStreamReader isReader = new InputStreamReader(contents);
				BufferedReader reader = new BufferedReader(isReader);
				StringBuffer sb = new StringBuffer();
				String str;
				while ((str = reader.readLine()) != null) {
					sb.append(str);
				}
				String textContent = sb.toString();
				// con este textContent hay que abrir el editor
				Chromium browser = new Chromium(parent, parent.getStyle());
				browser.setUrl("equomonaco");
			} catch (CoreException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
