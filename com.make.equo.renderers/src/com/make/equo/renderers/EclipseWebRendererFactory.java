package com.make.equo.renderers;

import org.eclipse.e4.ui.internal.workbench.swt.AbstractPartRenderer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.workbench.renderers.swt.WorkbenchRendererFactory;

public class EclipseWebRendererFactory extends WorkbenchRendererFactory {

//	protected MPartRenderer stackRenderer;
	protected String id;
	private ToolBarRenderer toolBarRenderer;
	private WebItemStackRenderer stackRenderer;

	@Override
	public AbstractPartRenderer getRenderer(MUIElement uiElement, Object parent) {

		if (uiElement instanceof MToolBar) {
			if (toolBarRenderer == null) {
				toolBarRenderer = new ToolBarRenderer();
				super.initRenderer(toolBarRenderer);
			}
			return toolBarRenderer;
		} else if (uiElement instanceof MPartStack) {
			if (stackRenderer == null) {
				stackRenderer = new WebItemStackRenderer();
				super.initRenderer(stackRenderer);
			}
			return stackRenderer;
		}
		return super.getRenderer(uiElement, parent);
	}
}
