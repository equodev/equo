package com.make.equo.eclipse.renderers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.MUILabel;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarElement;
import org.eclipse.e4.ui.workbench.renderers.swt.ToolBarManagerRenderer;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.make.equo.eclipse.renderers.contributions.EquoRenderersContribution;
import com.make.equo.server.api.IEquoServer;
import com.make.equo.ws.api.EquoEventHandler;
import com.make.swtcef.Chromium;

public class ToolBarRenderer extends ToolBarManagerRenderer {

	private static final String EQUO_RENDERERS_URL = "http://equo_renderers";
	protected List<Map<String, String>> values;
	protected List<MToolBarElement> elementsTool;
	protected String namespace;

	@Inject
	private EquoEventHandler equoEventHandler;
	@Inject
	private IEquoServer equoProxyServer;

	@Override
	public Object createWidget(MUIElement toolBar, Object parent) {
		setTools(toolBar);
		namespace = "ToolBar" + Integer.toHexString(toolBar.hashCode());

		Composite mapComposite = (Composite) parent;
		mapComposite.setLayout(new GridLayout(1, false));

		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 45;

		Chromium browser = new Chromium(mapComposite, SWT.NONE);

		equoProxyServer.addUrl(EQUO_RENDERERS_URL);
		String renderersContributionPath = equoProxyServer.getEquoContributionPath() + EquoRenderersContribution.TYPE
				+ "/";
		String toolBarContributionUri = renderersContributionPath + "toolBarContribution.js";

		equoProxyServer.addCustomScript(EQUO_RENDERERS_URL, toolBarContributionUri);

		String renderersUri = EQUO_RENDERERS_URL + "/" + equoProxyServer.getEquoContributionPath()
				+ EquoRenderersContribution.TYPE;

		browser.setUrl(renderersUri + "?" + "namespace=" + namespace);

		browser.setLayoutData(data);

		webSocketSendMessage();
		webSocketOnMessage();
		return mapComposite;
	}

	public void setTools(MUIElement element) {
		elementsTool = new ArrayList<MToolBarElement>();
		values = new ArrayList<Map<String, String>>();
		List<MToolBarElement> children = ((MToolBar) element).getChildren();
		HashMap<String, String> hmap;
		// platform:/plugin/com.make.appExample/icons/sample.png
		String id;
		for (MToolBarElement e : children) {
			if (e instanceof MUILabel) {
				hmap = new HashMap<String, String>();
				hmap.put("iconURI", ((MUILabel) e).getIconURI());
				id = "Icon" + Integer.toHexString(e.hashCode());
				e.setElementId(id);
				hmap.put("id", id);
				values.add(hmap);
				elementsTool.add(e);
			}
		}
	}

	public void webSocketSendMessage() {
		equoEventHandler.send(namespace + "_icons", values);
	}

	public void webSocketOnMessage() {
		equoEventHandler.on(namespace + "_iconClicked", (JsonObject payload) -> {
			JsonElement value = payload.get("accion");
			String id = "";
			if (value != null) {
				id = value.getAsString();
			}
			runAccion(id, (JsonObject) payload.get("params"));
		});
	}

	public void runAccion(String id, JsonObject actionPayload) {
		Display defaultDisplay = Display.getDefault();

		defaultDisplay.syncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openInformation(defaultDisplay.getActiveShell(), "Action performed from JS",
						"Action performed from JS. Vamos Equo!");
			}
		});

		for (MToolBarElement e : elementsTool) {
			if (e.getElementId().equals(id)) {
				System.out.println("Action is " + id);
			}
		}
	}
}
