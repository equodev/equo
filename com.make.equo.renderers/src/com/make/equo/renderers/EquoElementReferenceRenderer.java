package com.make.equo.renderers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.MContext;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPlaceholder;
import org.eclipse.e4.ui.workbench.IPresentationEngine;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.renderers.swt.ElementReferenceRenderer;
import org.eclipse.e4.ui.workbench.renderers.swt.WorkbenchRendererFactory;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class EquoElementReferenceRenderer extends ElementReferenceRenderer {

	@Inject
	@Named(WorkbenchRendererFactory.SHARED_ELEMENTS_STORE)
	private Map<MUIElement, Set<MPlaceholder>> renderedMap;

	@Inject
	private IPresentationEngine renderingEngine;
	
	@Override
	public Object createWidget(final MUIElement element, Object parent) {
		MPlaceholder ph = (MPlaceholder) element;
		final MUIElement ref = ph.getRef();
		ref.setCurSharedRef(ph);

		Set<MPlaceholder> renderedRefs = renderedMap.get(ref);
		if (renderedRefs == null) {
			renderedRefs = new HashSet<>();
			renderedMap.put(ref, renderedRefs);
		}

		if (!renderedRefs.contains(ph))
			renderedRefs.add(ph);

		Composite newComp = new Composite((Composite) parent, SWT.NONE);
//		GridDataFactory.fillDefaults().grab(true, true).applyTo(newComp);
		newComp.setLayout(new FillLayout());

		// if the placeholder is *not* in the currently active perspective
		// then don't re-parent the current view
		int phLoc = modelService.getElementLocation(ph);
		if (phLoc == EModelService.IN_ACTIVE_PERSPECTIVE
				|| phLoc == EModelService.IN_SHARED_AREA
				|| phLoc == EModelService.OUTSIDE_PERSPECTIVE) {
			Control refWidget = (Control) ref.getWidget();
			if (refWidget == null) {
				ref.setToBeRendered(true);
				refWidget = (Control) renderingEngine.createGui(ref, newComp,
						getContextForParent(ref));
			} else {
				if (refWidget.getParent() != newComp) {
					refWidget.setParent(newComp);
				}
			}

			if (ref instanceof MContext) {
				IEclipseContext context = ((MContext) ref).getContext();
				IEclipseContext newParentContext = getContext(ph);
				if (context.getParent() != newParentContext) {
					context.setParent(newParentContext);
				}
			}
		}

		return newComp;
	}

}
