package com.make.equo.renderers;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.contributions.IContributionFactory;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.IPresentationEngine;
import org.eclipse.e4.ui.workbench.renderers.swt.ContributedPartRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;

public class EquoContributedPartRenderer extends ContributedPartRenderer {

	@Override
	public Object createWidget(final MUIElement element, Object parent) {
		if (!(element instanceof MPart) || !(parent instanceof Composite)) {
			return null;
		}

		// retrieve context for this part
		final MPart part = (MPart) element;
		IEclipseContext localContext = part.getContext();
		Widget parentWidget = (Widget) parent;
		// retrieve existing Composite, e.g., for the e4 compatibility case
		Composite partComposite = localContext.getLocal(Composite.class);

		// does the part already have a composite in its contexts?
		if (partComposite == null) {

			final Composite newComposite = new Composite((Composite) parentWidget, SWT.NONE) {

				/**
				 * Field to determine whether we are currently in the midst of
				 * granting focus to the part.
				 */
				private boolean beingFocused = false;

				@Override
				public boolean setFocus() {
					if (!beingFocused) {
						try {
							// we are currently asking the part to take focus
							beingFocused = true;

							// delegate an attempt to set the focus here to the
							// part's implementation (if there is one)
							Object object = part.getObject();
							if (object != null && isEnabled()) {
								IPresentationEngine pe = part.getContext().get(IPresentationEngine.class);
								pe.focusGui(part);
								return true;
							}
							return super.setFocus();
						} finally {
							// we are done, unset our flag
							beingFocused = false;
						}
					}

					// already being focused, likely some strange recursive
					// call,
					// just return
					return true;
				}
			};
			newComposite.setLayout(new FillLayout(SWT.VERTICAL));

			partComposite = newComposite;
		}
		super.bindWidget(element, partComposite);
		localContext.set(Composite.class, partComposite);

		IContributionFactory contributionFactory = localContext.get(IContributionFactory.class);
		Object newPart = contributionFactory.create(part.getContributionURI(), localContext);
		part.setObject(newPart);

		return partComposite;
	}
	
}
