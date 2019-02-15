package com.make.equo.renderers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

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

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.make.equo.server.api.IEquoServer;
import com.make.equo.ws.api.EquoEventHandler;
import com.make.swtcef.Chromium;

public class ToolBarRenderer extends ToolBarManagerRenderer implements IEquoRenderer {

	private static final String ECLIPSE_IDE_IDS_TO_COMMANDS_PROPERTIES = "eclipseIDEIdsToCommands.properties";
	private String namespace;
	private MUIElement toolBar;

	@Inject
	private EquoEventHandler equoEventHandler;
	@Inject
	private IEquoServer equoProxyServer;

	@Override
	public Object createWidget(MUIElement toolBar, Object parent) {
		this.namespace = "ToolBar" + Integer.toHexString(toolBar.hashCode());
		this.toolBar = toolBar;

		Composite toolBarComposite = (Composite) parent;

		configureAndStartRenderProcess(toolBarComposite);

		return toolBarComposite;
	}

	@Override
	public IEquoServer getEquoProxyServer() {
		return equoProxyServer;
	}

	@Override
	public String getNamespace() {
		return namespace;
	}

	@Override
	public EquoEventHandler getEquoEventHandler() {
		return equoEventHandler;
	}

	@Override
	public List<String> getJsFileNamesForRendering() {
		return Lists.newArrayList("renderers/toolBarRenderer.js");
	}

	@Override
	public Chromium createBrowserComponent(Composite toolBarParent) {
		toolBarParent.setLayout(new GridLayout(1, false));
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 45;
		Chromium browser = new Chromium(toolBarParent, SWT.NONE);
		browser.setLayoutData(data);
//		ToolBar toolBar = new ToolBar(toolBarParent, SWT.NONE);
//
//		RowLayout rowLayout = new RowLayout();
//
//		rowLayout.marginLeft = 0;
//		rowLayout.marginTop = 0;
//		rowLayout.marginRight = 0;
//		rowLayout.marginBottom = 0;
//		rowLayout.spacing = 1;
//
//		toolBar.setLayout(rowLayout);
//
//		Chromium browser = new Chromium(toolBar, SWT.NONE);
//		browser.setLayoutData(new RowData());
//		browser.setLayoutData(data);

		return browser;
	}

	@Override
	public void onActionPerformedOnElement() {
		equoEventHandler.on(namespace + "_itemClicked", (JsonObject payload) -> {
			JsonElement value = payload.get("accion");
			String id = "";
			if (value != null) {
				id = value.getAsString();
			}
			runAccion(id, (JsonObject) payload.get("params"));
		});
	}

	@Override
	public List<Map<String, String>> getEclipse4Model() {
		Optional<Properties> knownEclipseIdsToCommands = getKnownEclipseIdsToCommands();
		List<Map<String, String>> e4Model = new ArrayList<Map<String, String>>();
		List<MToolBarElement> toolBarElements = ((MToolBar) toolBar).getChildren();
		for (MToolBarElement e : toolBarElements) {
			if (e instanceof MUILabel) {
				HashMap<String, String> elementModel = new HashMap<String, String>();
				String elementId = e.getElementId();
//				elementModel.put("id", elementId);
				elementModel.put("iconURI", ((MUILabel) e).getIconURI());

				System.out.println("el id es " + elementId);

				if (knownEclipseIdsToCommands.isPresent()) {
					Properties properties = knownEclipseIdsToCommands.get();
					if (properties.containsKey(elementId)) {
						String commandId = properties.getProperty(elementId);
						elementModel.put("id", commandId);
					}
				}

				e4Model.add(elementModel);
			}
		}
		return e4Model;
	}

	private void runAccion(String id, JsonObject actionPayload) {
		Display defaultDisplay = Display.getDefault();

		defaultDisplay.syncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openInformation(defaultDisplay.getActiveShell(), "Action performed from JS",
						"Action performed from JS. Vamos Equo!");
			}
		});
	}

	private Optional<Properties> getKnownEclipseIdsToCommands() {
		Properties prop = new Properties();
		InputStream input = null;

		try {

			String filename = ECLIPSE_IDE_IDS_TO_COMMANDS_PROPERTIES;
			input = ToolBarRenderer.class.getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				System.out.println("Sorry, unable to find " + filename);
				return Optional.empty();
			}
			prop.load(input);
			return Optional.of(prop);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return Optional.empty();
	}
}
