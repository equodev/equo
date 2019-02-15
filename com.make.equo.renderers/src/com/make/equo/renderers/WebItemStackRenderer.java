package com.make.equo.renderers;

import javax.inject.Inject;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.ui.css.swt.properties.custom.CSSPropertyMruVisibleSWTHandler;
import org.eclipse.e4.ui.internal.workbench.swt.CSSRenderingUtils;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.renderers.swt.LazyStackRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.w3c.dom.css.CSSValue;

public class WebItemStackRenderer extends LazyStackRenderer {

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

	public void init(IEclipseContext context) {
		this.context = context;
		modelService = context.get(EModelService.class);
	}

	@Override
	public Object createWidget(MUIElement element, Object parent) {
		if (!(element instanceof MPartStack) || !(parent instanceof Composite))
			return null;

		MPartStack pStack = (MPartStack) element;

		Composite parentComposite = (Composite) parent;

		// Ensure that all rendered PartStacks have an Id
		if (element.getElementId() == null || element.getElementId().length() == 0) {
			String generatedId = "PartStack@" + Integer.toHexString(element.hashCode()); //$NON-NLS-1$
			element.setElementId(generatedId);
		}

		int styleOverride = getStyleOverride(pStack);
		int style = styleOverride == -1 ? SWT.BORDER : styleOverride;
		final CTabFolder tabFolder = new CTabFolder(parentComposite, style);
		tabFolder.setMRUVisible(getMRUValue(tabFolder));

		// Adjust the minimum chars based on the location
		int location = modelService.getElementLocation(element);
		if ((location & EModelService.IN_SHARED_AREA) != 0) {
			tabFolder.setMinimumCharacters(MIN_EDITOR_CHARS);
			tabFolder.setUnselectedCloseVisible(true);
		} else {
			tabFolder.setMinimumCharacters(MIN_VIEW_CHARS);
			tabFolder.setUnselectedCloseVisible(false);
		}

		bindWidget(element, tabFolder); // ?? Do we need this ?

		// Add a composite to manage the view's TB and Menu
//		addTopRight(tabFolder);

		return tabFolder;
	}

	@Override
	protected void createTab(MElementContainer<MUIElement> me, MUIElement part) {
	}

	private boolean getInitialMRUValue(Control control) {
		CSSRenderingUtils util = context.get(CSSRenderingUtils.class);
		if (util == null) {
			return getMRUValueFromPreferences();
		}

		CSSValue value = util.getCSSValue(control, "MPartStack", "swt-mru-visible"); //$NON-NLS-1$ //$NON-NLS-2$

		if (value == null) {
			value = util.getCSSValue(control, "MPartStack", "mru-visible"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (value == null) {
			return getMRUValueFromPreferences();
		}
		return Boolean.parseBoolean(value.getCssText());
	}

	private boolean getMRUValueFromPreferences() {
		boolean initialMRUValue = preferences.getBoolean(MRU_KEY_DEFAULT, MRU_DEFAULT);
		boolean actualValue = preferences.getBoolean(MRU_KEY, initialMRUValue);
		return actualValue;
	}

	private boolean getMRUValue(Control control) {
		if (CSSPropertyMruVisibleSWTHandler.isMRUControlledByCSS()) {
			return getInitialMRUValue(control);
		}
		return getMRUValueFromPreferences();
	}
}
