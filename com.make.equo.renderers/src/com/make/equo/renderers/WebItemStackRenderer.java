package com.make.equo.renderers;

import static com.make.equo.renderers.util.IRendererConstants.PARTSTACK_RENDERER_NAME;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.internal.workbench.swt.CSSConstants;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.MUILabel;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.model.application.ui.basic.MCompositePart;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartSashContainer;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MStackElement;
import org.eclipse.e4.ui.workbench.IPresentationEngine;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.renderers.swt.LazyStackRenderer;
import org.eclipse.e4.ui.workbench.renderers.swt.WorkbenchRendererFactory;
import org.eclipse.jface.action.LegacyActionTools;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.chromium.Browser;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;
import org.osgi.service.event.Event;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.make.equo.application.api.IEquoApplication;
import com.make.equo.server.api.IEquoServer;
import com.make.equo.ws.api.IEquoEventHandler;

public class WebItemStackRenderer extends LazyStackRenderer implements IEquoRenderer {

	private String namespace;

	// Bi-directional Maps to reduce times getting namespaces
	private Map<String, MUIElement> namespacesToPartStacks = new HashMap<String, MUIElement>();
	private Map<MUIElement, String> partStacksToNamespaces = new HashMap<MUIElement, String>();

	// Map to store MPart references from their hashcodes as strings
	@Inject
	@Named(EclipseWebRendererFactory.HASHED_PARTS)
	private Map<String, MPart> hashedParts = new HashMap<String, MPart>();

	@Inject
	@Named(WorkbenchRendererFactory.SHARED_ELEMENTS_STORE)
	Map<MUIElement, Set<MPlaceholder>> renderedMap;

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
		namespacesToPartStacks.put(this.namespace, element);
		partStacksToNamespaces.put(element, this.namespace);

		Composite container = (Composite) parent;
		Composite stackComposite = new Composite(container, SWT.BORDER);
		GridLayoutFactory.fillDefaults().applyTo(stackComposite);

		Composite webItemStackRendererComposite = new Composite(stackComposite, SWT.NONE);
		webItemStackRendererComposite.setLayout(new FillLayout());
		GridDataFactory.fillDefaults().grab(true, false).hint(200, 50).applyTo(webItemStackRendererComposite);

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
				} else {
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
				MUIElement element = (MUIElement) e.widget.getData(OWNING_ME);
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
			equoEventHandler.send(namespace + "_proceedClose");
			partService.hidePart(part);
			if (part.getTags().contains(EPartService.REMOVE_ON_HIDE_TAG)) {
				hashedParts.remove(Integer.toString(part.hashCode()));
			}
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

		String namespace = partStacksToNamespaces.get(stack);

		// this would mean we haven't rendered a browser for this stack
		if (namespace == null) {
			return;
		}

		// an invisible element won't have the correct widget hierarchy
		if (!element.isVisible()) {
			return;
		}

		MPart part = null;
		if (element instanceof MPart) {
			part = (MPart) element;
			hashedParts.put(Integer.toString(element.hashCode()), (MPart) element);
		} else if (element instanceof MPlaceholder) {
			part = (MPart) ((MPlaceholder) element).getRef();
			hashedParts.put(Integer.toString(part.hashCode()), part);
		}

		if (part != null && part instanceof MPlaceholder) {
			part.setCurSharedRef((MPlaceholder) element);
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
		partTab.put("name", Integer.toString(mPart.hashCode()));
		return partTab;
	}

	@Override
	public void onActionPerformedOnElement() {
		equoEventHandler.on(namespace + "_tabClicked", (JsonObject payload) -> {
			JsonElement currentNamespace = payload.get("namespace");
			String namespace = currentNamespace.getAsString();
			JsonElement nameValue = payload.get("partName");

			String partName = "";
			if (nameValue != null) {
				partName = nameValue.getAsString();
			}
			JsonElement close = payload.get("close");
			if (close != null) {
				closeTab(partName, namespace);
			} else {
				selectTab(partName, namespace);
			}
		});
	}

	private void closeTab(String partName, String namespace) {
		Display defaultDisplay = Display.getDefault();

		defaultDisplay.syncExec(new Runnable() {
			@Override
			public void run() {
				MPart partToClose = hashedParts.get(partName);
				if (partToClose != null) {
					closePart(partToClose, false, namespace);
				}
			}
		});
	}

	private void selectTab(String partName, String namespace) {
		Display defaultDisplay = Display.getDefault();

		defaultDisplay.syncExec(new Runnable() {
			@Override
			public void run() {
				MPart partToShow = hashedParts.get(partName);
				MPartStack partStack = (MPartStack) namespacesToPartStacks.get(namespace);

				if (partToShow != null) {
					partStack.setSelectedElement(
							partServiceImpl.showPart(partToShow, EPartService.PartState.ACTIVATE).getCurSharedRef());
				}
			}
		});
	}

	@Override
	public List<Map<String, String>> getEclipse4Model(String namespace) {
		List<Map<String, String>> e4Model = new ArrayList<Map<String, String>>();

		if (!namespacesToPartStacks.containsKey(namespace)) {
			// TODO throw a custom exception
		}

		MUIElement muiElement = namespacesToPartStacks.get(namespace);

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
				MPart mPart = (MPart) ref;
				e4Model.add(createPartTab(mPart,
						selected != null && Objects.equals(selected.getElementId(), mPart.getElementId())));
			}
		}
		return e4Model;
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
	public Browser createBrowserComponent(Composite webItemStackRendererComposite) {
//		Chromium.setCommandLine(new String[][] { new String[] { "proxy-server", "localhost:9896" },
//				new String[] { "ignore-certificate-errors", null },
//				new String[] { "allow-file-access-from-files", null }, new String[] { "disable-web-security", null },
//				new String[] { "enable-widevine-cdm", null }, new String[] { "proxy-bypass-list", "127.0.0.1" } });

		// The browser stays 1 level above other parts
		Browser browser = new Browser(webItemStackRendererComposite, SWT.NONE);
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
	public String getEquoRendererName() {
		return PARTSTACK_RENDERER_NAME;
	}

	@Override
	public void processContents(MElementContainer<MUIElement> stack) {
		// Lazy Loading: here we only process the contents through childAdded,
		// we specifically do not render them
		IPresentationEngine renderer = context.get(IPresentationEngine.class);

		for (MUIElement element : stack.getChildren()) {
			if (element instanceof MPart) {
				hashedParts.put(Integer.toString(element.hashCode()), (MPart) element);
			} else if (element instanceof MPlaceholder) {
				MPart part = (MPart) ((MPlaceholder) element).getRef();
				hashedParts.put(Integer.toString(part.hashCode()), part);
			}

			if (!element.isToBeRendered() || !element.isVisible()) {
				continue;
			}

			boolean lazy = true;

			// Special case: we also render any placeholder that refers to
			// an *existing* part, this doesn't break lazy loading since the
			// part is already there...see bug 378138 for details
			if (element instanceof MPlaceholder) {
				MPlaceholder ph = (MPlaceholder) element;
				if (ph.getRef() instanceof MPart && ph.getRef().getWidget() != null) {
					lazy = false;
				}
			}

			if (lazy) {
				createTab(stack, element);
			} else {
				renderer.createGui(element);
			}
		}
	}

	@Inject
	@Optional
	private void subscribeTopicUILabelChanged(@UIEventTopic(UIEvents.UILabel.TOPIC_ALL) Event event) {
		MUIElement element = (MUIElement) event.getProperty(UIEvents.EventTags.ELEMENT);
		if (!(element instanceof MPart))
			return;

		MPart part = (MPart) element;

		String attName = (String) event.getProperty(UIEvents.EventTags.ATTNAME);
		Object newValue = event.getProperty(UIEvents.EventTags.NEW_VALUE);

		// is this a direct child of the stack?
		MElementContainer<MUIElement> parent = part.getParent();
		if (parent != null && parent.getRenderer() instanceof WebItemStackRenderer) {
			String namespace = partStacksToNamespaces.get(parent);
			if (namespace != null) {
				updateTab(namespace, part, attName, newValue);
			}
			return;
		}

		// Do we have any stacks with place holders for the element
		// that's changed?
		Set<MPlaceholder> refs = renderedMap.get(part);
		if (refs != null) {
			for (MPlaceholder ref : refs) {
				MElementContainer<MUIElement> refParent = ref.getParent();
				if (refParent != null && refParent.getRenderer() instanceof WebItemStackRenderer) {
					String namespace = partStacksToNamespaces.get(refParent);
					if (namespace != null) {
						updateTab(namespace, part, attName, newValue);
					}
				}
			}
		}
	}

	@Inject
	@Optional
	private void subscribeTopicDirtyChanged(@UIEventTopic(UIEvents.Dirtyable.TOPIC_DIRTY) Event event) {
		Object objElement = event.getProperty(UIEvents.EventTags.ELEMENT);

		// Ensure that this event is for a MMenuItem
		if (!(objElement instanceof MPart)) {
			return;
		}

		// Extract the data bits
		MPart part = (MPart) objElement;

		updatePartTab(event, part);
	}

	@Inject
	@Optional
	private void subscribeTopicClosablePartChanged(@UIEventTopic(UIEvents.Part.TOPIC_CLOSEABLE) Event event) {
		updateClosableTab(event);
	}

	@Inject
	@Optional
	private void subscribeTopicClosablePlaceholderChanged(
			@UIEventTopic(UIEvents.Placeholder.TOPIC_CLOSEABLE) Event event) {
		updateClosableTab(event);
	}

	private void updateClosableTab(Event event) {
		Object element = event.getProperty(UIEvents.EventTags.ELEMENT);

		MPart part = null;
		if (element instanceof MPart) {
			part = (MPart) element;
		} else if (element instanceof MPlaceholder) {
			MUIElement ref = ((MPlaceholder) element).getRef();
			if (ref instanceof MPart) {
				part = (MPart) ref;
			}
		}

		if (part == null) {
			return;
		}

		updatePartTab(event, part);
	}

	private void updatePartTab(Event event, MPart part) {
		String attName = (String) event.getProperty(UIEvents.EventTags.ATTNAME);
		Object newValue = event.getProperty(UIEvents.EventTags.NEW_VALUE);

		// Is the part directly under the stack?
		MElementContainer<MUIElement> parent = part.getParent();
		if (parent != null && parent.getRenderer() instanceof WebItemStackRenderer) {
			String namespace = partStacksToNamespaces.get(parent);
			if (namespace != null) {
				updateTab(namespace, part, attName, newValue);
			}
			return;
		}

		// Do we have any stacks with place holders for the element
		// that's changed?
		Set<MPlaceholder> refs = renderedMap.get(part);
		if (refs != null) {
			for (MPlaceholder ref : refs) {
				MElementContainer<MUIElement> refParent = ref.getParent();
				if (refParent != null && refParent.getRenderer() instanceof WebItemStackRenderer) {
					String namespace = partStacksToNamespaces.get(refParent);
					if (namespace != null) {
						updateTab(namespace, part, attName, newValue);
					}
				}
			}
		}
	}

	@Inject
	@Optional
	private void subscribeTopicActivateChanged(@UIEventTopic(UIEvents.UILifeCycle.ACTIVATE) Event event) {
		// Manages CSS styling based on active part changes
		MUIElement changed = (MUIElement) event.getProperty(UIEvents.EventTags.ELEMENT);
		if (!(changed instanceof MPart)) {
			return;
		}

		MPart newActivePart = (MPart) changed;
		MUIElement partParent = newActivePart.getParent();
		if (partParent == null && newActivePart.getCurSharedRef() != null) {
			partParent = newActivePart.getCurSharedRef().getParent();
		}

		// Skip sash containers
		while (partParent != null && partParent instanceof MPartSashContainer) {
			partParent = partParent.getParent();
		}

		// Ensure the stack of a split part gets updated when one
		// of its internal parts gets activated
		if (partParent instanceof MCompositePart) {
			partParent = partParent.getParent();
		}

		MPartStack pStack = (MPartStack) (partParent instanceof MPartStack ? partParent : null);

		List<String> tags = new ArrayList<>();
		tags.add(CSSConstants.CSS_ACTIVE_CLASS);
		List<MUIElement> activeElements = modelService.findElements(modelService.getTopLevelWindowFor(newActivePart),
				null, MUIElement.class, tags);
		for (MUIElement element : activeElements) {
			if (element instanceof MPartStack && element != pStack) {
				styleElement(element, false);
			} else if (element instanceof MPart && element != newActivePart) {
				styleElement(element, false);
			}
		}

		styleElement(newActivePart, true);
	}

	protected void updateTab(String namespace, MPart part, String attName, Object newValue) {
		Map<String, String> changes = new HashMap<String, String>();
		changes.put("name", Integer.toString(part.hashCode()));
		switch (attName) {
		case UIEvents.UILabel.LABEL:
		case UIEvents.UILabel.LOCALIZED_LABEL:
			String newName = (String) newValue;
			changes.put("label", getLabel(part, newName));
			break;
		case UIEvents.Dirtyable.DIRTY:
			changes.put("label", getLabel(part, part.getLocalizedLabel()));
		case UIEvents.UILabel.ICONURI:
//			cti.setImage(getImage(part));
			break;
		case UIEvents.UILabel.TOOLTIP:
		case UIEvents.UILabel.LOCALIZED_TOOLTIP:
			String newTTip = (String) newValue;
			changes.put("tooltip", getToolTip(newTTip));
			break;
		case UIEvents.Part.CLOSEABLE:
			Boolean closeableState = (Boolean) newValue;
			changes.put("closeable", closeableState.toString());
			break;
		default:
			break;
		}
		equoEventHandler.send(namespace + "_updateTab", changes);
	}

	private String getToolTip(String newToolTip) {
		return newToolTip == null || newToolTip.length() == 0 ? null : LegacyActionTools.escapeMnemonics(newToolTip);
	}

	@PreDestroy
	public void contextDisposed() {
		super.contextDisposed(eventBroker);
	}

	private String getLabel(MUILabel itemPart, String newName) {
		if (newName == null) {
			newName = ""; //$NON-NLS-1$
		} else {
			newName = LegacyActionTools.escapeMnemonics(newName);
		}

		if (itemPart instanceof MDirtyable && ((MDirtyable) itemPart).isDirty()) {
			newName = '*' + newName;
		}
		return newName;
	}

}
