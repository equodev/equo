package com.make.equo.renderers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.MUILabel;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimBar;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBarElement;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
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

	private static Chromium browser;
	private static String namespace;

	private static final String ECLIPSE_MAIN_TOOLBAR_ID = "org.eclipse.ui.main.toolbar";
//	private static final String ECLIPSE_IDE_IDS_TO_COMMANDS_PROPERTIES = "eclipseIDEIdsToCommands.properties";
//	private static final List<String> toolBarsBlackList = Lists.newArrayList("group.file",
//			"org.eclipse.ui.workbench.file", "additions", "org.eclipse.debug.ui.launchActionSet",
//			"org.eclipse.jdt.ui.JavaElementCreationActionSet", "org.eclipse.search.searchActionSet", "group.nav",
//			"org.eclipse.ui.workbench.navigate", "group.editor", "group.help", "org.eclipse.ui.workbench.help");
//	private MToolBar mToolBar;

	@Inject
	private static EquoEventHandler equoEventHandler;
	@Inject
	private static IEquoServer equoProxyServer;

	@Inject
	private static MApplication mApplication;

	@Inject
	EModelService modelService;

	@Override
	public Object createWidget(MUIElement mUIToolbar, Object parent) {
		if (!(mUIToolbar instanceof MToolBar) || !(parent instanceof Composite)) {
			return null;
		}

		if (belongsToEclipseMainTrimBar(mUIToolbar)) {
			return null;
//			Composite toolBarComposite = (Composite) parent;
//			if (!hasStartedRenderProcess()) {
//				if (namespace == null) {
//					namespace = "ToolBar" + Integer.toHexString(mUIToolbar.hashCode());
//				}
//				configureAndStartRenderProcess(toolBarComposite);
//			} else {
//				sendToolBarModelToRender((MToolBar) mUIToolbar);
//			}
//			return toolBarComposite;
		}
//		if (parent2)
//			if (isAnIDEToolBar(mUIToolbar)) {
//				return null;
//			}
//
//		if (isEquoMainToolBar(mUIToolbar)) {
//			this.namespace = "ToolBar" + Integer.toHexString(mUIToolbar.hashCode());
//			this.mToolBar = (MToolBar) mUIToolbar;
//			this.mToolBar.setToBeRendered(true);
//
//			return null;
//
////			Composite toolBarComposite = (Composite) parent;
////
////			configureAndStartRenderProcess(toolBarComposite);
////			
////			this.mToolBar.setToBeRendered(false);
////
////			return toolBarComposite;
//
//		}
		return null;
	}

	private void sendToolBarModelToRender(MToolBar mUIToolbar) {
		List<Map<String, String>> e4Model = new ArrayList<Map<String, String>>();

		List<MToolBarElement> toolBarElements = mUIToolbar.getChildren();

		List<MToolBarElement> toolBarElementsToRendered = toolBarElements.stream().filter(toolItem -> {
			return toolItem.isToBeRendered() && toolItem.isVisible();
		}).collect(Collectors.toList());

		for (MToolBarElement mToolBarElement : toolBarElementsToRendered) {
			if (mToolBarElement instanceof MUILabel) {
				HashMap<String, String> elementModel = new HashMap<String, String>();
				String elementId = mToolBarElement.getElementId();
				elementModel.put("id", elementId);
				e4Model.add(elementModel);
			}
		}
		sendEclipse4Model(e4Model);
	}

	private boolean belongsToEclipseMainTrimBar(MUIElement mUIToolbar) {
		MElementContainer<?> parentMToolbar = mUIToolbar.getParent();
		return (parentMToolbar instanceof MTrimBar)
				&& ECLIPSE_MAIN_TOOLBAR_ID.equals(((MTrimBar) parentMToolbar).getElementId());
	}

//	private boolean isEquoMainToolBar(MUIElement mUIToolbar) {
//		return EQUO_MAIN_TOOLBAR_ID.equals(mUIToolbar.getElementId());
//	}

//	private boolean isAnIDEToolBar(MUIElement mUIToolbar) {
//		return toolBarsBlackList.contains(mUIToolbar.getElementId());
//	}

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
		Chromium.setCommandLine(new String[][] { new String[] { "proxy-server", "localhost:9896" },
				new String[] { "ignore-certificate-errors", null },
				new String[] { "allow-file-access-from-files", null }, new String[] { "disable-web-security", null },
				new String[] { "enable-widevine-cdm", null }, new String[] { "proxy-bypass-list", "127.0.0.1" } });

		toolBarParent.setLayout(new GridLayout(1, false));
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 45;
		browser = new Chromium(toolBarParent, SWT.NONE);
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

//	@Override
//	public boolean hasStartedRenderProcess() {
//		return browser != null && !browser.isDisposed();
//	}

	@Override
	public void onActionPerformedOnElement() {
		getEquoEventHandler().on(namespace + "_itemClicked", (JsonObject payload) -> {
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
		return Collections.emptyList();
//		Optional<Properties> knownEclipseIdsToCommands = getKnownEclipseIdsToCommands();
//		Properties idsToCommands = knownEclipseIdsToCommands.get();
//		List<Map<String, String>> e4Model = new ArrayList<Map<String, String>>();
//
//		List<MToolBarElement> orderedItemsFromEclipseToolbars = getOrderedItemsFromEclipseToolbars();
//		for (MToolBarElement mToolBarElement : orderedItemsFromEclipseToolbars) {
//			if (mToolBarElement instanceof MUILabel) {
//				HashMap<String, String> elementModel = new HashMap<String, String>();
//				String elementId = mToolBarElement.getElementId();
//
//				if (idsToCommands.containsKey(elementId)) {
//					mToolBar.getChildren().add(mToolBarElement);
//					String commandId = idsToCommands.getProperty(elementId);
//					elementModel.put("id", commandId);
//					e4Model.add(elementModel);
//				}
//			}
//		}
//		return e4Model;
	}

//	private List<MToolBarElement> getOrderedItemsFromEclipseToolbars() {
//		List<MToolBarElement> result = new ArrayList<MToolBarElement>();
//		for (String toolBarId : toolBarsBlackList) {
//			List<MToolBar> mToolBars = modelService.findElements(mApplication, toolBarId, MToolBar.class,
//					Collections.emptyList());
//
//			for (MToolBar mToolBar : mToolBars) {
//				List<MToolBarElement> toolBarElements = mToolBar.getChildren();
//
//				List<MToolBarElement> toolBarElementsToRendered = toolBarElements.stream().filter(toolItem -> {
//					return toolItem.isToBeRendered() && toolItem.isVisible();
//				}).collect(Collectors.toList());
//
//				result.addAll(toolBarElementsToRendered);
//			}
//		}
//		return result;
//	}

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

//	private Optional<Properties> getKnownEclipseIdsToCommands() {
//		Properties prop = new Properties();
//		InputStream input = null;
//		try {
//			String filename = ECLIPSE_IDE_IDS_TO_COMMANDS_PROPERTIES;
//			input = ToolBarRenderer.class.getClassLoader().getResourceAsStream(filename);
//			if (input == null) {
//				System.out.println("Sorry, unable to find " + filename);
//				return Optional.empty();
//			}
//			prop.load(input);
//			return Optional.of(prop);
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		} finally {
//			if (input != null) {
//				try {
//					input.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		return Optional.empty();
//	}
}
