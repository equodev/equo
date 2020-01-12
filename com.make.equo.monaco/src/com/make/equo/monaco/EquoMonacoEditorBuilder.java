package com.make.equo.monaco;

import org.eclipse.swt.widgets.Composite;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

import com.make.equo.ws.api.IEquoEventHandler;

@Component(service = EquoMonacoEditorBuilder.class, scope = ServiceScope.PROTOTYPE)
public class EquoMonacoEditorBuilder {

	@Reference
	private IEquoEventHandler equoEventHandler;

//	private int id;
	private Composite parent;
	private int style;
	private String contents;
	private String fileName;

	public EquoMonacoEditorBuilder() {
//		this.id = this.hashCode();
		this.style = -1;
		this.contents = "";
		this.fileName = "";
	}

	public EquoMonacoEditorBuilder withParent(Composite parent) {
		this.parent = parent;
		return this;
	}

	public EquoMonacoEditorBuilder withContents(String contents) {
		this.contents = contents;
		return this;
	}

	public EquoMonacoEditorBuilder withFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public EquoMonacoEditorBuilder withStyle(int style) {
		this.style = style;
		return this;
	}

	public EquoMonacoEditor create() {
		if (style == -1) {
			style = parent.getStyle();
		}
		return new EquoMonacoEditor(parent, style, equoEventHandler, contents, fileName);
	}

}
