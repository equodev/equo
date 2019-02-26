package com.make.equo.renderers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.ui.css.swt.properties.custom.CSSPropertyMruVisibleSWTHandler;
import org.eclipse.e4.ui.internal.workbench.swt.CSSRenderingUtils;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MStackElement;
import org.eclipse.e4.ui.workbench.UIEvents.Placeholder;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.renderers.swt.LazyStackRenderer;
import org.eclipse.e4.ui.workbench.renderers.swt.SWTPartRenderer;
import org.eclipse.e4.ui.workbench.renderers.swt.StackRenderer;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.w3c.dom.css.CSSValue;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.make.equo.application.api.IEquoApplication;
import com.make.equo.server.api.IEquoServer;
import com.make.equo.ws.api.EquoEventHandler;
import com.make.swtcef.Chromium;

public class WebItemStackRenderer extends StackRenderer implements IEquoRenderer{

	/**
	 * Default default value for MRU behavior.
	 */
	public static final boolean MRU_DEFAULT = true;

	/**
	 * Key to control the default default value of the "most recently used" order
	 * enablement
	 */
	public static final String MRU_KEY_DEFAULT = "enableMRUDefault"; //$NON-NLS-1$

	/**
	 * Key to control the actual boolean preference of the "most recently used"
	 * order enablement
	 */
	public static final String MRU_KEY = "enableMRU"; //$NON-NLS-1$

	// Minimum characters in for stacks outside the shared area
	private static int MIN_VIEW_CHARS = 1;

	// Minimum characters in for stacks inside the shared area
	private static int MIN_EDITOR_CHARS = 15;

	@Inject
	@Preference(nodePath = "org.eclipse.e4.ui.workbench.renderers.swt")
	private IEclipsePreferences preferences;
	
	private String namespace;
	private MUIElement webItemStackRenderer;

	@Inject
	private EquoEventHandler equoEventHandler;
	@Inject
	private IEquoServer equoProxyServer;
	
	@Inject
	private IEquoApplication equoApplication;

	public void init(IEclipseContext context) {
		this.context = context;
		modelService = context.get(EModelService.class);
	}

	@Override
	public Object createWidget(MUIElement element, Object parent) {
		if (!(element instanceof MPartStack) || !(parent instanceof Composite))
			return null;
		
		int n = element.hashCode();
		this.namespace = "WebItemStackRenderer" + Integer.toHexString(n);
		this.webItemStackRenderer = element;

		Composite webItemStackRendererComposite = (Composite) parent;
		
		
		configureAndStartRenderProcess(webItemStackRendererComposite);
		
		return webItemStackRendererComposite;
		
////		De Albert
//		setMParts(element);
//		nameSpace = "PartStack" + Integer.toHexString(element.hashCode());
//
//		Composite mapComposite = (Composite) parent;
//		mapComposite.setLayout(new GridLayout(1, false));
//
//		GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
//		data.heightHint = 100;
//
//		browser = new Chromium(mapComposite, SWT.NONE);
//		File file = new File("part.html");
//		try {
//			browser.setUrl(file.toURL().toString());
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		}
//		browser.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
//
//		compositePart = new Composite(mapComposite, SWT.NONE);
//		compositePart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
//
//		layout = new StackLayout();
//		compositePart.setLayout(layout);
//		compositePart.layout();
//
//		webSocketSendMessage();
//		webSocketOnMessage();
//
//		return compositePart;

		//***** FIN ALBERT
		
		
//		if (!(element instanceof MPartStack) || !(parent instanceof Composite))
//			return null;
//
//		MPartStack pStack = (MPartStack) element;
//
//		Composite parentComposite = (Composite) parent;
//
//		// Ensure that all rendered PartStacks have an Id
//		if (element.getElementId() == null || element.getElementId().length() == 0) {
//			String generatedId = "PartStack@" + Integer.toHexString(element.hashCode()); //$NON-NLS-1$
//			element.setElementId(generatedId);
//		}
//
//		int styleOverride = getStyleOverride(pStack);
//		int style = styleOverride == -1 ? SWT.BORDER : styleOverride;
//		final CTabFolder tabFolder = new CTabFolder(parentComposite, style);
//		tabFolder.setMRUVisible(getMRUValue(tabFolder));
//
//		// Adjust the minimum chars based on the location
//		int location = modelService.getElementLocation(element);
//		if ((location & EModelService.IN_SHARED_AREA) != 0) {
//			tabFolder.setMinimumCharacters(MIN_EDITOR_CHARS);
//			tabFolder.setUnselectedCloseVisible(true);
//		} else {
//			tabFolder.setMinimumCharacters(MIN_VIEW_CHARS);
//			tabFolder.setUnselectedCloseVisible(false);
//		}
//
//		bindWidget(element, tabFolder); // ?? Do we need this ?
//
//		// Add a composite to manage the view's TB and Menu
////		addTopRight(tabFolder);
//
//		return tabFolder;
	}
	


//	@Override
//	protected void createTab(MElementContainer<MUIElement> me, MUIElement part) {
//	}

//	private boolean getInitialMRUValue(Control control) {
//		CSSRenderingUtils util = context.get(CSSRenderingUtils.class);
//		if (util == null) {
//			return getMRUValueFromPreferences();
//		}
//
//		CSSValue value = util.getCSSValue(control, "MPartStack", "swt-mru-visible"); //$NON-NLS-1$ //$NON-NLS-2$
//
//		if (value == null) {
//			value = util.getCSSValue(control, "MPartStack", "mru-visible"); //$NON-NLS-1$ //$NON-NLS-2$
//		}
//		if (value == null) {
//			return getMRUValueFromPreferences();
//		}
//		return Boolean.parseBoolean(value.getCssText());
//	}
//
//	private boolean getMRUValueFromPreferences() {
//		boolean initialMRUValue = preferences.getBoolean(MRU_KEY_DEFAULT, MRU_DEFAULT);
//		boolean actualValue = preferences.getBoolean(MRU_KEY, initialMRUValue);
//		return actualValue;
//	}
//
//	private boolean getMRUValue(Control control) {
//		if (CSSPropertyMruVisibleSWTHandler.isMRUControlledByCSS()) {
//			return getInitialMRUValue(control);
//		}
//		return getMRUValueFromPreferences();
//	}

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
	public EquoEventHandler getEquoEventHandler() {
		return equoEventHandler;
	}

	@Override
	public List<Map<String, String>> getEclipse4Model() {
		List<Map<String, String>> e4Model = new ArrayList<Map<String, String>>();
		
		List<MStackElement> children = ((MPartStack) webItemStackRenderer).getChildren();
		String id;
		for (MStackElement e : children) {
			if (e instanceof MPart || e instanceof Placeholder) {
				HashMap<String, String> hmap = new HashMap<String, String>();
				hmap.put("label", ((MPart) e).getLabel());
				hmap.put("visible", String.valueOf(e.isVisible()));
				hmap.put("closeable", String.valueOf(((MPart) e).isCloseable()));
				hmap.put("tooltip", ((MPart) e).getTooltip());
				hmap.put("iconURI", ((MPart) e).getIconURI());
				hmap.put("isDirty", String.valueOf(((MPart) e).isDirty()));
				id = "MPart" + Integer.toHexString(e.hashCode());
				e.setElementId(id);
				hmap.put("id", id);
				e4Model.add(hmap);
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
	public Chromium createBrowserComponent(Composite parent) {
		
//		Chromium.setCommandLine(new String[][] { new String[] { "proxy-server", "localhost:9896" },
//			new String[] { "ignore-certificate-errors", null },
//			new String[] { "allow-file-access-from-files", null }, new String[] { "disable-web-security", null },
//			new String[] { "enable-widevine-cdm", null }, new String[] { "proxy-bypass-list", "127.0.0.1" } });
		
		parent.setLayout(new GridLayout(1, false));
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
		data.heightHint = 100;
		Chromium browser = new Chromium(parent, SWT.NONE);
		browser.setLayoutData(data);
		
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
	public String getModelContributionPath() {
		return null;
	}
}
