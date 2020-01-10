package com.make.equo.eclipse.monaco.editor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.make.equo.monaco.EquoMonacoEditor;
import com.make.equo.monaco.EquoMonacoEditorBuilder;

public class MonacoEditorPart extends EditorPart {

	private EquoMonacoEditor editor;

	@Override
	public void doSave(IProgressMonitor monitor) {

		editor.getContents((editorContents) -> {
			IEditorInput input = getEditorInput();
			if (input instanceof FileEditorInput) {
				Display.getDefault().syncExec(() -> {
					try {
						((FileEditorInput) input).getFile().setContents(
								new ByteArrayInputStream(editorContents.getBytes(Charset.forName("UTF-8"))), true,
								false, monitor);
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});

			}
		});

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
		return true;
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

				try {
					BundleContext bndContext = FrameworkUtil.getBundle(EquoMonacoEditorBuilder.class)
							.getBundleContext();
					ServiceReference<EquoMonacoEditorBuilder> svcReference = bndContext
							.getServiceReference(EquoMonacoEditorBuilder.class);
					EquoMonacoEditorBuilder builder = bndContext.getService(svcReference);
					editor = builder.withParent(parent).withStyle(parent.getStyle()).withContents(textContent)
							.withFileName(fileInput.getURI().toString()).create();

				} catch (Exception e) {
					System.out.println("Couldn't retrieve model injector");
				}
			} catch (CoreException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	@Override
	public void setFocus() {

	}

}
