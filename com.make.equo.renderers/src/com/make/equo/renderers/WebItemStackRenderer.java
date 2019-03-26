package com.make.equo.renderers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MStackElement;
import org.eclipse.e4.ui.workbench.IPresentationEngine;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.renderers.swt.LazyStackRenderer;
import org.eclipse.e4.ui.workbench.renderers.swt.WorkbenchRendererFactory;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.make.equo.application.api.IEquoApplication;
import com.make.equo.server.api.IEquoServer;
import com.make.equo.ws.api.IEquoEventHandler;
import com.make.swtcef.Chromium;

public class WebItemStackRenderer extends LazyStackRenderer implements IEquoRenderer {

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

	@Inject
	private IPresentationEngine engine;

	@Inject
	private EPartService partServiceImpl;

	@Inject
	private EModelService modelService;
	
	@Inject
	@Named(WorkbenchRendererFactory.SHARED_ELEMENTS_STORE)
	Map<MUIElement, Set<MPlaceholder>> renderedMap;

	@Inject
	IEventBroker eventBroker;

	@PostConstruct
	public void init() {
		super.init(eventBroker);
	}

	@Override
	public Object createWidget(MUIElement element, Object parent) {
		if (!(element instanceof MPartStack) || !(parent instanceof Composite)) {
			return null;
		}

		this.namespace = "WebItemStackRenderer@" + Integer.toHexString(element.hashCode());
		partStacks.put(this.namespace, element);

		Composite container = (Composite) parent;
		Composite stackComposite = new Composite(container, SWT.BORDER);
		GridLayoutFactory.fillDefaults().applyTo(stackComposite);
		
		Composite webItemStackRendererComposite = new Composite(stackComposite, SWT.NONE);
		webItemStackRendererComposite.setLayout(new FillLayout());
		GridDataFactory.fillDefaults().grab(true, false).hint(200, 50).applyTo(webItemStackRendererComposite);
//		GridDataFactory.fillDefaults().grab(true, true).applyTo(webItemStackRendererComposite);
		
		Composite partStackingComposite = new Composite(stackComposite, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(partStackingComposite);
		partStackingComposite.setLayout(new StackLayout());
		
		configureAndStartRenderProcess(webItemStackRendererComposite);

		return partStackingComposite;
	}

	@Override
	public Object getUIContainer(MUIElement element) {
		if (element instanceof MPart || element instanceof MPlaceholder) {
			if (element.getParent() != null)
				if (element.getParent().getWidget() instanceof Composite) {
					return ((Composite) element.getParent().getWidget()).getChildren()[1];
				}
			else {
				Object value = element.getTransientData().get(IPresentationEngine.RENDERING_PARENT_KEY);
				if (value != null) {
					return value;
				}
			}
		}
		return super.getUIContainer(element);
	}
	
	@Override
	public void bindWidget(MUIElement me, Object widget) {
		
		Composite actualStackWidget = null;
		
		if (widget instanceof Widget) {
			((Widget) widget).setData(OWNING_ME, me);

			// Set up the CSS Styling parameters; id & class
			setCSSInfo(me, widget);

			// Ensure that disposed widgets are unbound form the model
			Widget swtWidget = (Widget) widget;
			swtWidget.addDisposeListener(e -> {
				MUIElement element = (MUIElement) e.widget
						.getData(OWNING_ME);
				if (element != null)
					unbindWidget(element);
			});
		}
		
		if (widget instanceof Composite) {
			actualStackWidget = ((Composite) widget).getParent();
		}

		// Create a bi-directional link between the widget and the model
		me.setWidget(actualStackWidget == null ? widget : actualStackWidget);
	}
	
	@Override
	public void childRendered(MElementContainer<MUIElement> parentElement, MUIElement element) {
		super.childRendered(parentElement, element);

		if (!(((MUIElement) parentElement) instanceof MPartStack) || !(element instanceof MStackElement))
			return;

		
		
		if (parentElement.getSelectedElement() == element) {
			Composite stackComposite = (Composite) getUIContainer(element);
			StackLayout layout = (StackLayout) stackComposite.getLayout();
			layout.topControl = (Control) element.getWidget();
			stackComposite.layout();
		}
		
		createTab(parentElement, element);
	}

	@Override
	public void hookControllerLogic(final MUIElement me) {
		super.hookControllerLogic(me);
	}

	/**
	 * Closes the part that's backed by the given widget.
	 *
	 * @param widget the part that owns this widget
	 * @param check  <tt>true</tt> if the part should be checked to see if it has
	 *               been defined as being not closeable for users, <tt>false</tt>
	 *               if this check should not be performed
	 * @return <tt>true</tt> if the part was closed, <tt>false</tt> otherwise
	 */
	private boolean closePart(MUIElement uiElement, boolean check, String namespace) {
		MPart part = (MPart) ((uiElement instanceof MPart) ? uiElement : ((MPlaceholder) uiElement).getRef());
		if (!check && !isClosable(part)) {
			return false;
		}

		IEclipseContext partContext = part.getContext();
		IEclipseContext parentContext = getContextForParent(part);
		// a part may not have a context if it hasn't been rendered
		IEclipseContext context = partContext == null ? parentContext : partContext;
		// ask user to save if necessary and close part if it is not dirty
		EPartService partService = context.get(EPartService.class);
		if (partService.savePart(part, true)) {
			equoEventHandler.send(namespace + "_proceed");
			partService.hidePart(part);
			return true;
		}
		// the user has canceled the save operation, so the part is not closed
		return false;
	}

	protected boolean isClosable(MPart part) {
		// if it's a shared part check its current ref
		if (part.getCurSharedRef() != null) {
			return !(part.getCurSharedRef().getTags().contains(IPresentationEngine.NO_CLOSE));
		}

		return part.isCloseable();
	}

	@Override
	protected void showTab(MUIElement element) {
		super.showTab(element);

		// an invisible element won't have the correct widget hierarchy
		if (!element.isVisible()) {
			return;
		}

		final Composite stackComposite = (Composite) getUIContainer(element);
		createTab(element.getParent(), element);

		Control ctrl = (Control) element.getWidget();
		Control tabCtrl = null;

		if (ctrl != null && ctrl.getParent() != stackComposite) {
			ctrl.setParent(stackComposite);
			setTopControl(ctrl);
		} else if (ctrl != null) {
			setTopControl(ctrl);
		} else if (ctrl == null) {
			tabCtrl = (Control) engine.createGui(element);
			setTopControl(tabCtrl);
		}

		// Ensure that the newly selected control is correctly sized
		if (tabCtrl != null && tabCtrl instanceof Composite) {
			Composite ctiComp = (Composite) tabCtrl;
			// see bug 461573: call below is still needed to make view
			// descriptions visible after unhiding the view with changed bounds
			ctiComp.layout(false, true);
		}
	}
	
	private void setTopControl(Control ctrl) {
		if (ctrl instanceof Composite) {
			Composite parent = ((Composite) ctrl).getParent();
			StackLayout layout = (StackLayout) parent.getLayout();
			layout.topControl = ctrl;
			parent.layout(false, true);
		}
	}

	@Override
	protected void createTab(MElementContainer<MUIElement> stack, MUIElement element) {

		// an invisible element won't have the correct widget hierarchy
		if (!element.isVisible()) {
			return;
		}

		String namespace = "";
		if (stack != null) {
			for (Entry<String, MUIElement> entry : partStacks.entrySet()) {
				if (entry.getValue() == stack) {
					namespace = entry.getKey();
					break;
				}
			}
		} else {
			namespace = this.namespace;
		}

		MPart part = null;
		if (element instanceof MPart)
			part = (MPart) element;
		else if (element instanceof MPlaceholder) {
			part = (MPart) ((MPlaceholder) element).getRef();
			if (part != null) {
				part.setCurSharedRef((MPlaceholder) element);
			}
		}
		
		if (part != null && part.getLabel() != null) {
			Object selectedElement = stack.getSelectedElement();
			boolean isSelected = false;
			if (selectedElement != null) {
				if (selectedElement instanceof MPart && selectedElement == part) {
					isSelected = true;
				} else if (selectedElement instanceof MPlaceholder) {
					MPart selectedPart = (MPart) ((MPlaceholder) selectedElement).getRef();
					if (selectedPart == selectedElement) {
						isSelected = true;
					}
				}
			}
			
			Map<String, String> partModel = createPartTab(part, isSelected);
			equoEventHandler.send(namespace + "_addTab", partModel);
		}
	}

	private Map<String, String> createPartTab(MPart mPart, boolean isSelected) {
		HashMap<String, String> partTab = new HashMap<String, String>();
		partTab.put("label", mPart.getLabel());
		partTab.put("visible", Boolean.toString(mPart.isVisible()));
		partTab.put("closeable", Boolean.toString(mPart.isCloseable()));
		partTab.put("tooltip", mPart.getTooltip());
		partTab.put("iconURI", mPart.getIconURI());
		partTab.put("isDirty", Boolean.toString(mPart.isDirty()));
		partTab.put("isSelected", Boolean.toString(isSelected));
		partTab.put("id", mPart.getElementId());
		return partTab;
	}

	@Override
	public void onActionPerformedOnElement() {
		equoEventHandler.on(namespace + "_tabClicked", (JsonObject payload) -> {
			JsonElement currentNamespace = payload.get("namespace");
			String namespace = currentNamespace.getAsString();
			JsonElement idValue = payload.get("partId");
			JsonElement labelValue = payload.get("partLabel");
			String id = "";
			String label = "";
			if (idValue != null) {
				id = idValue.getAsString();
			}
			if (labelValue != null) {
				label = labelValue.getAsString();
			}
			JsonElement close = payload.get("close");
			if (close != null) {
				closeTab(id, label, namespace);
			} else {
				selectTab(id, label, namespace);
			}
		});
	}

	private void closeTab(String id, String label, String namespace) {
		Display defaultDisplay = Display.getDefault();

		defaultDisplay.syncExec(new Runnable() {
			@Override
			public void run() {
				MPart partToClose = null;
				if ("org.eclipse.e4.ui.compatibility.editor".equals(id)) {
					List<MPart> editorParts = modelService.findElements(partStacks.get(namespace), id, MPart.class, null,
							EModelService.OUTSIDE_PERSPECTIVE | EModelService.IN_ACTIVE_PERSPECTIVE
									| EModelService.IN_SHARED_AREA);
					for (MPart editorPart : editorParts) {
						if (label.equals(editorPart.getLabel())) {
							partToClose = editorPart;
							break;
						}
					}
				} else {
					partToClose = partServiceImpl.findPart(id);
				}
				if (partToClose != null) {
					closePart(partToClose, false, namespace);
				}
			}
		});
	}

	private void selectTab(String id, String label, String namespace) {
		Display defaultDisplay = Display.getDefault();

		defaultDisplay.syncExec(new Runnable() {
			@Override
			public void run() {
				MPart partToShow = null;
				MPartStack partStack = (MPartStack) partStacks.get(namespace);
				if ("org.eclipse.e4.ui.compatibility.editor".equals(id)) {
					List<MPart> editorParts = modelService.findElements(partStack, id, MPart.class, null,
							EModelService.OUTSIDE_PERSPECTIVE | EModelService.IN_ACTIVE_PERSPECTIVE
									| EModelService.IN_SHARED_AREA);
					for (MPart editorPart : editorParts) {
						if (label.equals(editorPart.getLabel())) {
							partToShow = editorPart;
							break;
						}
					}
				} else {
					partToShow = partServiceImpl.findPart(id);
				}
				partStack.setSelectedElement(partServiceImpl.showPart(partToShow, EPartService.PartState.ACTIVATE).getCurSharedRef());
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
	public Chromium createBrowserComponent(Composite webItemStackRendererComposite) {
		Chromium.setCommandLine(new String[][] { new String[] { "proxy-server", "localhost:9896" },
				new String[] { "ignore-certificate-errors", null },
				new String[] { "allow-file-access-from-files", null }, new String[] { "disable-web-security", null },
				new String[] { "enable-widevine-cdm", null }, new String[] { "proxy-bypass-list", "127.0.0.1" } });

		// The browser stays 1 level above other parts
		Chromium browser = new Chromium(webItemStackRendererComposite, SWT.NONE);
//		GridDataFactory.fillDefaults().grab(true, false).hint(200, 50).applyTo(browser);

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

//	@SuppressWarnings("unchecked")
//	@Inject
//	@Optional
//	private void subscribeTopicTransientDataChanged(
//			@UIEventTopic(UIEvents.ApplicationElement.TOPIC_TRANSIENTDATA) org.osgi.service.event.Event event) {
//		Object changedElement = event.getProperty(UIEvents.EventTags.ELEMENT);
//
//		if (!(changedElement instanceof MPart))
//			return;
//
//		String key;
//		if (UIEvents.isREMOVE(event)) {
//			key = ((Entry<String, Object>) event.getProperty(UIEvents.EventTags.OLD_VALUE)).getKey();
//		} else {
//			key = ((Entry<String, Object>) event.getProperty(UIEvents.EventTags.NEW_VALUE)).getKey();
//		}
//
//		if (!IPresentationEngine.OVERRIDE_ICON_IMAGE_KEY.equals(key)
//				&& !IPresentationEngine.OVERRIDE_TITLE_TOOL_TIP_KEY.equals(key))
//			return;
//
//		MPart part = (MPart) changedElement;
//		List<CTabItem> itemsToSet = getItemsToSet(part);
//		for (CTabItem item : itemsToSet) {
//			if (key.equals(IPresentationEngine.OVERRIDE_ICON_IMAGE_KEY)) {
//				item.setImage(getImage(part));
//			} else if (key.equals(IPresentationEngine.OVERRIDE_TITLE_TOOL_TIP_KEY)) {
//				String newTip = getToolTip(part);
//				item.setToolTipText(getToolTip(newTip));
//			}
//		}
//	}
//
//	/**
//	 * Handles changes in tags
//	 *
//	 * @param event
//	 */
//	@Inject
//	@Optional
//	private void subscribeTopicTagsChanged(@UIEventTopic(UIEvents.ApplicationElement.TOPIC_TAGS) Event event) {
//
//		if (tabStateHandler == null) {
//			tabStateHandler = new TabStateHandler();
//		}
//		tabStateHandler.handleEvent(event);
//
//		Object changedObj = event.getProperty(EventTags.ELEMENT);
//
//		if (!(changedObj instanceof MPart))
//			return;
//
//		final MPart part = (MPart) changedObj;
//		CTabItem item = findItemForPart(part);
//		if (item == null || item.isDisposed())
//			return;
//
//		if (UIEvents.isADD(event)) {
//			if (UIEvents.contains(event, UIEvents.EventTags.NEW_VALUE, IPresentationEngine.ADORNMENT_PIN)) {
//				item.setImage(getImage(part));
//			}
//		} else if (UIEvents.isREMOVE(event)) {
//			if (UIEvents.contains(event, UIEvents.EventTags.OLD_VALUE, IPresentationEngine.ADORNMENT_PIN)) {
//				item.setImage(getImage(part));
//			}
//		}
//	}
//
//	@Inject
//	@Optional
//	private void subscribeTopicChildrenChanged(@UIEventTopic(UIEvents.ElementContainer.TOPIC_CHILDREN) Event event) {
//
//		Object changedObj = event.getProperty(UIEvents.EventTags.ELEMENT);
//		// only interested in changes to toolbars
//		if (!(changedObj instanceof MToolBar)) {
//			return;
//		}
//
//		MUIElement container = modelService.getContainer((MUIElement) changedObj);
//		// check if this is a part's toolbar
//		if (container instanceof MPart) {
//			MElementContainer<?> parent = ((MPart) container).getParent();
//			// only relayout if this part is the selected element and we
//			// actually rendered this element
//			if (parent instanceof MPartStack && parent.getSelectedElement() == container
//					&& parent.getRenderer() == WebItemStackRenderer.this) {
//				Object widget = parent.getWidget();
//				if (widget instanceof CTabFolder) {
//					adjustTopRight((CTabFolder) widget);
//				}
//			}
//		}
//	}
//
//	@Inject
//	@Optional
//	private void subscribeTopicUILabelChanged(@UIEventTopic(UIEvents.UILabel.TOPIC_ALL) Event event) {
//		MUIElement element = (MUIElement) event.getProperty(UIEvents.EventTags.ELEMENT);
//		if (!(element instanceof MPart))
//			return;
//
//		MPart part = (MPart) element;
//
//		String attName = (String) event.getProperty(UIEvents.EventTags.ATTNAME);
//		Object newValue = event.getProperty(UIEvents.EventTags.NEW_VALUE);
//
//		// is this a direct child of the stack?
//		if (element.getParent() != null && element.getParent().getRenderer() == WebItemStackRenderer.this) {
//			CTabItem cti = findItemForPart(part);
//			if (cti != null) {
//				updateTab(cti, part, attName, newValue);
//			}
//			return;
//		}
//
//		// Do we have any stacks with place holders for the element
//		// that's changed?
//		MWindow win = modelService.getTopLevelWindowFor(part);
//		List<MPlaceholder> refs = modelService.findElements(win, null, MPlaceholder.class, null);
//		if (refs != null) {
//			for (MPlaceholder ref : refs) {
//				if (ref.getRef() != part)
//					continue;
//
//				MElementContainer<MUIElement> refParent = ref.getParent();
//				// can be null, see bug 328296
//				if (refParent != null && refParent.getRenderer() instanceof StackRenderer) {
//					CTabItem cti = findItemForPart(ref, refParent);
//					if (cti != null) {
//						updateTab(cti, part, attName, newValue);
//					}
//				}
//			}
//		}
//	}
//
//	@Inject
//	@Optional
//	private void subscribeTopicDirtyChanged(@UIEventTopic(UIEvents.Dirtyable.TOPIC_DIRTY) Event event) {
//		Object objElement = event.getProperty(UIEvents.EventTags.ELEMENT);
//
//		// Ensure that this event is for a MMenuItem
//		if (!(objElement instanceof MPart)) {
//			return;
//		}
//
//		// Extract the data bits
//		MPart part = (MPart) objElement;
//
//		updatePartTab(event, part);
//	}
//
//	@Inject
//	@Optional
//	private void subscribeTopicClosablePartChanged(@UIEventTopic(UIEvents.Part.TOPIC_CLOSEABLE) Event event) {
//		updateClosableTab(event);
//	}
//
//	@Inject
//	@Optional
//	private void subscribeTopicClosablePlaceholderChanged(
//			@UIEventTopic(UIEvents.Placeholder.TOPIC_CLOSEABLE) Event event) {
//		updateClosableTab(event);
//	}
//
//	private void updateClosableTab(Event event) {
//		Object element = event.getProperty(UIEvents.EventTags.ELEMENT);
//
//		MPart part = null;
//		if (element instanceof MPart) {
//			part = (MPart) element;
//		} else if (element instanceof MPlaceholder) {
//			MUIElement ref = ((MPlaceholder) element).getRef();
//			if (ref instanceof MPart) {
//				part = (MPart) ref;
//			}
//		}
//
//		if (part == null) {
//			return;
//		}
//
//		updatePartTab(event, part);
//	}
//
//	private void updatePartTab(Event event, MPart part) {
//		String attName = (String) event.getProperty(UIEvents.EventTags.ATTNAME);
//		Object newValue = event.getProperty(UIEvents.EventTags.NEW_VALUE);
//
//		// Is the part directly under the stack?
//		MElementContainer<MUIElement> parent = part.getParent();
//		if (parent != null && parent.getRenderer() == WebItemStackRenderer.this) {
//			CTabItem cti = findItemForPart(part, parent);
//			if (cti != null) {
//				updateTab(cti, part, attName, newValue);
//			}
//			return;
//		}
//
//		// Do we have any stacks with place holders for the element
//		// that's changed?
//		Set<MPlaceholder> refs = renderedMap.get(part);
//		if (refs != null) {
//			for (MPlaceholder ref : refs) {
//				MElementContainer<MUIElement> refParent = ref.getParent();
//				if (refParent.getRenderer() instanceof StackRenderer) {
//					CTabItem cti = findItemForPart(ref, refParent);
//					if (cti != null) {
//						updateTab(cti, part, attName, newValue);
//					}
//				}
//			}
//		}
//	}
//
//	@Inject
//	@Optional
//	private void subscribeTopicVisibleChanged(@UIEventTopic(UIEvents.UIElement.TOPIC_VISIBLE) Event event) {
//		shouldViewMenuBeRendered(event);
//	}
//
//	@Inject
//	@Optional
//	private void subscribeTopicToBeRenderedChanged(@UIEventTopic(UIEvents.UIElement.TOPIC_TOBERENDERED) Event event) {
//		shouldViewMenuBeRendered(event);
//	}
//
//	/**
//	 * An event handler for listening to changes to the state of view menus and
//	 * its child menu items. Depending on what state these items are in, the
//	 * view menu should or should not be rendered in the tab folder.
//	 */
//	private void shouldViewMenuBeRendered(Event event) {
//		Object objElement = event.getProperty(UIEvents.EventTags.ELEMENT);
//
//		// Ensure that this event is for a MMenuItem
//		if (!(objElement instanceof MMenuElement)) {
//			return;
//		}
//
//		// Ensure that it's a View part's menu
//		MMenuElement menuModel = (MMenuElement) objElement;
//		MUIElement menuParent = modelService.getContainer(menuModel);
//		if (!(menuParent instanceof MPart))
//			return;
//
//		MPart element = (MPart) menuParent;
//		MUIElement parentElement = element.getParent();
//		if (parentElement == null) {
//			MPlaceholder placeholder = element.getCurSharedRef();
//			if (placeholder == null) {
//				return;
//			}
//
//			parentElement = placeholder.getParent();
//			if (parentElement == null) {
//				return;
//			}
//		}
//
//		Object widget = parentElement.getWidget();
//		if (widget instanceof CTabFolder) {
//			adjustTopRight((CTabFolder) widget);
//		}
//	}
//
//	@Inject
//	@Optional
//	private void subscribeTopicActivateChanged(@UIEventTopic(UIEvents.UILifeCycle.ACTIVATE) Event event) {
//		// Manages CSS styling based on active part changes
//		MUIElement changed = (MUIElement) event.getProperty(UIEvents.EventTags.ELEMENT);
//		if (!(changed instanceof MPart)) {
//			return;
//		}
//
//		MPart newActivePart = (MPart) changed;
//		MUIElement partParent = newActivePart.getParent();
//		if (partParent == null && newActivePart.getCurSharedRef() != null) {
//			partParent = newActivePart.getCurSharedRef().getParent();
//		}
//
//		// Skip sash containers
//		while (partParent != null && partParent instanceof MPartSashContainer) {
//			partParent = partParent.getParent();
//		}
//
//		// Ensure the stack of a split part gets updated when one
//		// of its internal parts gets activated
//		if (partParent instanceof MCompositePart) {
//			partParent = partParent.getParent();
//		}
//
//		MPartStack pStack = (MPartStack) (partParent instanceof MPartStack ? partParent : null);
//
//		List<String> tags = new ArrayList<>();
//		tags.add(CSSConstants.CSS_ACTIVE_CLASS);
//		List<MUIElement> activeElements = modelService.findElements(modelService.getTopLevelWindowFor(newActivePart),
//				null, MUIElement.class, tags);
//		for (MUIElement element : activeElements) {
//			if (element instanceof MPartStack && element != pStack) {
//				styleElement(element, false);
//			} else if (element instanceof MPart && element != newActivePart) {
//				styleElement(element, false);
//			}
//		}
//
//		if (pStack != null) {
//			styleElement(pStack, true);
//		}
//		styleElement(newActivePart, true);
//	}
//
//	@Inject
//	@Optional
//	private void subscribeTopicSelectedelementChanged(
//			@UIEventTopic(UIEvents.ElementContainer.TOPIC_SELECTEDELEMENT) Event event) {
//		if (tabStateHandler == null) {
//			tabStateHandler = new TabStateHandler();
//		}
//		tabStateHandler.handleEvent(event);
//	}
//
//	@Override
//	protected boolean requiresFocus(MPart element) {
//		MUIElement inStack = element.getCurSharedRef() != null ? element.getCurSharedRef() : element;
//		if (inStack.getParent() != null && inStack.getParent().getTransientData().containsKey(INHIBIT_FOCUS)) {
//			inStack.getParent().getTransientData().remove(INHIBIT_FOCUS);
//			return false;
//		}
//
//		return super.requiresFocus(element);
//	}
//	protected void updateTab(CTabItem cti, MPart part, String attName, Object newValue) {
//		switch (attName) {
//		case UIEvents.UILabel.LABEL:
//		case UIEvents.UILabel.LOCALIZED_LABEL:
//			String newName = (String) newValue;
//			cti.setText(getLabel(part, newName));
//			break;
//		case UIEvents.Dirtyable.DIRTY:
//			cti.setText(getLabel(part, part.getLocalizedLabel()));
//		case UIEvents.UILabel.ICONURI:
//			cti.setImage(getImage(part));
//			break;
//		case UIEvents.UILabel.TOOLTIP:
//		case UIEvents.UILabel.LOCALIZED_TOOLTIP:
//			String newTTip = (String) newValue;
//			cti.setToolTipText(getToolTip(newTTip));
//			break;
//		case UIEvents.Part.CLOSEABLE:
//			Boolean closeableState = (Boolean) newValue;
//			cti.setShowClose(closeableState.booleanValue());
//			break;
//		default:
//			break;
//		}
//	}
//
//	private String getToolTip(String newToolTip) {
//		return newToolTip == null || newToolTip.length() == 0 ? null : LegacyActionTools.escapeMnemonics(newToolTip);
//	}
//
//	@PreDestroy
//	public void contextDisposed() {
//		super.contextDisposed(eventBroker);
//	}
//
//	private String getLabel(MUILabel itemPart, String newName) {
//		if (newName == null) {
//			newName = ""; //$NON-NLS-1$
//		} else {
//			newName = LegacyActionTools.escapeMnemonics(newName);
//		}
//
//		if (itemPart instanceof MDirtyable && ((MDirtyable) itemPart).isDirty()) {
//			newName = '*' + newName;
//		}
//		return newName;
//	}
//

}
