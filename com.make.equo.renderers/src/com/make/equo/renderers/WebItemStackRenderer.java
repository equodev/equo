package com.make.equo.renderers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MStackElement;
import org.eclipse.e4.ui.workbench.renderers.swt.SWTPartRenderer;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.make.equo.application.api.IEquoApplication;
import com.make.equo.server.api.IEquoServer;
import com.make.equo.ws.api.IEquoEventHandler;
import com.make.swtcef.Chromium;

public class WebItemStackRenderer extends SWTPartRenderer implements IEquoRenderer {

	static final String EQUO_RENDERERS_NAME = "part_stack";
	static final String EQUO_RENDERERS_URL = EQUO_RENDERERS_URL_PREFIX + EQUO_RENDERERS_NAME
			+ EQUO_RENDERERS_URL_SUFFIX;

	private String namespace;
	private static final Map<String, MUIElement> partStacks = new HashMap<String, MUIElement>();

	@Inject
	private IEquoEventHandler equoEventHandler;
	@Inject
	private IEquoServer equoProxyServer;

	@Inject
	private IEquoApplication equoApplication;

	@Override
	public Object createWidget(MUIElement element, Object parent) {
		if (!(element instanceof MPartStack) || !(parent instanceof Composite)) {
			return null;
		}

		this.namespace = "WebItemStackRenderer" + Integer.toHexString(element.hashCode());
		partStacks.put(this.namespace, element);

		Composite container = (Composite) parent;
		Composite webItemStackRendererComposite = new Composite(container, SWT.BORDER);
		GridLayoutFactory.fillDefaults().applyTo(webItemStackRendererComposite);

		configureAndStartRenderProcess(webItemStackRendererComposite);

		return webItemStackRendererComposite;
	}

	@Override
	public void childRendered(MElementContainer<MUIElement> parentElement, MUIElement element) {
		super.childRendered(parentElement, element);
		if (element.getWidget() instanceof Control) {
			GridDataFactory.fillDefaults().grab(true, true).applyTo((Control) element.getWidget());
		}
	}

	@Override
	public void onActionPerformedOnElement() {
		equoEventHandler.on(namespace + "_tabClicked", (JsonObject payload) -> {
			JsonElement value = payload.get("accion");
			String id = "";
			if (value != null) {
				id = value.getAsString();
			}
			runAccion(id, (JsonObject) payload.get("params"));
		});
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

	@Override
	public List<Map<String, String>> getEclipse4Model(String namespace) {
		List<Map<String, String>> e4Model = new ArrayList<Map<String, String>>();

		if (!partStacks.containsKey(namespace)) {
			// TODO throw a custom exception
		}

		MUIElement muiElement = partStacks.get(namespace);

		MStackElement selected = ((MPartStack) muiElement).getSelectedElement();

		List<MStackElement> children = ((MPartStack) muiElement).getChildren();
		for (MStackElement e : children) {
//			if ((e instanceof MPart) || (e instanceof MPlaceholder)) {

			MUIElement ref = null;
			if (e instanceof MPlaceholder) {
				MPlaceholder placeholder = (MPlaceholder) e;
				ref = placeholder.getRef();
			} else if (e instanceof MPart) {
				ref = e;
			}
			if (ref != null) {
				HashMap<String, String> partStackModel = new HashMap<String, String>();
				MPart mPart = (MPart) ref;
				partStackModel.put("label", mPart.getLabel());
				partStackModel.put("visible", Boolean.toString(mPart.isVisible()));
				partStackModel.put("closeable", Boolean.toString(mPart.isCloseable()));
				partStackModel.put("tooltip", mPart.getTooltip());
				partStackModel.put("iconURI", mPart.getIconURI());
				partStackModel.put("isDirty", Boolean.toString(mPart.isDirty()));
				partStackModel.put("isSelected", Boolean
						.toString(selected != null && Objects.equals(selected.getElementId(), mPart.getElementId())));
				partStackModel.put("id", mPart.getElementId());
				e4Model.add(partStackModel);
			}
		}
		return e4Model;
	}

	@Override
	public List<String> getJsFileNamesForRendering() {
		return Lists.newArrayList("renderers/webItemStackRenderer.js");
	}

	@Override
	public String getNamespace() {
		return namespace;
	}

	@Override
	public IEquoServer getEquoProxyServer() {
		return equoProxyServer;
	}

	@Override
	public Chromium createBrowserComponent(Composite toolBarParent) {
		Chromium.setCommandLine(new String[][] { new String[] { "proxy-server", "localhost:9896" },
				new String[] { "ignore-certificate-errors", null },
				new String[] { "allow-file-access-from-files", null }, new String[] { "disable-web-security", null },
				new String[] { "enable-widevine-cdm", null }, new String[] { "proxy-bypass-list", "127.0.0.1" } });

//		GridLayoutFactory.fillDefaults().applyTo(toolBarParent);
		Chromium browser = new Chromium(toolBarParent, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, false).hint(200, 50).applyTo(browser);

		return browser;
	}

	@Override
	public List<String> getFrameworkContributionJSONFileNames() {
		return new ArrayList<>();
	}

	@Override
	public IEquoApplication getEquoApplication() {
		return equoApplication;
	}

	@Override
	public IEquoEventHandler getEquoEventHandler() {
		return equoEventHandler;
	}

	@Override
	public String getModelContributionPath() {
		return "contributions/partStack/";
	}

	@Override
	public String getEquoRendererURL() {
		return EQUO_RENDERERS_URL;
	}

}
