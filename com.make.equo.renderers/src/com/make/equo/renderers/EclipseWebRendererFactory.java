package com.make.equo.renderers;

import org.eclipse.e4.ui.internal.workbench.swt.AbstractPartRenderer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.workbench.renderers.swt.WorkbenchRendererFactory;

public class EclipseWebRendererFactory extends WorkbenchRendererFactory {

	protected MPartRenderer stackRenderer;
	protected String id;
	private ToolBarRenderer toolBarRenderer;

	@Override
	public AbstractPartRenderer getRenderer(MUIElement uiElement, Object parent) {

		if (uiElement instanceof MPartStack) {
			stackRenderer = new MPartRenderer();
			super.initRenderer(stackRenderer);

			return stackRenderer;

		} else if (uiElement instanceof MToolBar) {
			toolBarRenderer = new ToolBarRenderer();

			super.initRenderer(toolBarRenderer);
			return toolBarRenderer;
		}
		return super.getRenderer(uiElement, parent);
	}
}
