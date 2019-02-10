package com.make.equo.renderers;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MStackElement;
import org.eclipse.e4.ui.workbench.renderers.swt.SWTPartRenderer;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.make.equo.ws.api.EquoEventHandler;
import com.make.swtcef.Chromium;

public class MPartRenderer extends SWTPartRenderer {
	private List<HashMap> values;
	private List<MPart> parts;
	StackLayout layout;
	Composite compositePart;
	Control active;
	String nameSpace;
	Chromium browser;

	@Inject
	private EquoEventHandler equoEventHandler;

	@Override
	public void processContents(MElementContainer<MUIElement> container) {
		super.processContents(container);
		layout.topControl = (Control) parts.get(0).getWidget();
		compositePart.layout();
		active = layout.topControl;
	}

	@Override
	public Object createWidget(MUIElement element, Object parent) {
		setMParts(element);
		nameSpace = "PartStack" + Integer.toHexString(element.hashCode());

		Composite mapComposite = (Composite) parent;
		mapComposite.setLayout(new GridLayout(1, false));

		GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
		data.heightHint = 100;

		browser = new Chromium(mapComposite, SWT.NONE);
		File file = new File("part.html");
		try {
			browser.setUrl(file.toURL().toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		browser.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		compositePart = new Composite(mapComposite, SWT.NONE);
		compositePart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		layout = new StackLayout();
		compositePart.setLayout(layout);
		compositePart.layout();

		webSocketSendMessage();
		webSocketOnMessage();

		return compositePart;

	}

	public void setMParts(MUIElement element) {
		List<MStackElement> children = ((MPartStack) element).getChildren();
		values = new ArrayList<HashMap>();
		parts = new ArrayList<MPart>();
		String id;
		for (MStackElement e : children) {
			if (e instanceof MPart) {
				HashMap<String, Object> hmap = new HashMap<String, Object>();
				hmap.put("label", ((MPart) e).getLabel());
				hmap.put("visible", e.isVisible());
				hmap.put("closeable", ((MPart) e).isCloseable());
				hmap.put("tooltip", ((MPart) e).getTooltip());
				hmap.put("iconURI", ((MPart) e).getIconURI());
				hmap.put("isDirty", ((MPart) e).isDirty());
				id = "MPart" + Integer.toHexString(e.hashCode());
				e.setElementId(id);
				hmap.put("id", id);
				values.add(hmap);
				parts.add((MPart) e);

			}

		}
	}

	public void webSocketSendMessage() {
		equoEventHandler.send(nameSpace + "_parts", values);
	}

	public void webSocketOnMessage() {

		equoEventHandler.on(nameSpace + "_tabClicked", (JsonObject payload) -> {
			JsonElement value = payload.get("val");
			String id = "";
			if (value != null) {
				id = value.getAsString();
			}
			setVisibleTabs(id);

		});
		equoEventHandler.on(nameSpace + "_systemBarClicked", (JsonObject payload) -> {
			JsonElement value = payload.get("accion");
			String title = "";
			if (value != null) {
				title = value.getAsString();
			}
			setVisibleTabs(title);

		});
	}

	public void setVisibleTabs(String id) {

		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				if (id.equals("#minimize")) {

				} else if (id.equals("#maximiza")) {

				} else {
					for (MPart e : parts) {
						if (e.getElementId().equals(id)) {
							layout.topControl = (Control) e.getWidget();
							compositePart.layout();
							active = (Control) e.getWidget();
							break;

						}
					}
				}
			}
		});
	}

	public void setParts(List<MPart> parts, List<HashMap> values) {
		this.parts = parts;
		this.values = values;
	}

}
