package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimBar;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MToolBar;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuFactoryImpl;

public class ToolbarBuilder {

	private OptionalViewBuilder optionalFieldBuilder;
	private MToolBar toolbar;
	private MTrimmedWindow parent;

	ToolbarBuilder(OptionalViewBuilder optionalFieldBuilder, MTrimmedWindow window) {
		this.parent = window;
		this.toolbar = optionalFieldBuilder.getToolbar();
		this.optionalFieldBuilder = optionalFieldBuilder;
	}

	ToolbarBuilder(ToolbarBuilder toolbarBuilder) {
		this.toolbar = toolbarBuilder.toolbar;
		this.parent = toolbarBuilder.parent;
		this.optionalFieldBuilder = toolbarBuilder.optionalFieldBuilder;
	}

	public ToolbarBuilder addToolbar() {
		this.toolbar = createToolbar();
		return new ToolbarBuilder(this);
	}

	public MToolBar createToolbar() {
		MTrimBar trimbar = MBasicFactory.INSTANCE.createTrimBar();
		MToolBar newToolbar = MenuFactoryImpl.eINSTANCE.createToolBar();
		newToolbar.setOnTop(true);
		newToolbar.setVisible(true);
		newToolbar.setElementId("com.make.equo.main.toolbar");
		this.parent.getTrimBars().add(trimbar);
		this.parent.getTrimBars().get(0).getChildren().add(newToolbar);
		return newToolbar;
	}

	OptionalViewBuilder getOptionalFieldBuilder() {
		return optionalFieldBuilder;
	}

	public MTrimmedWindow getParent() {
		return parent;
	}

	public MToolBar getToolbar() {
		return toolbar;
	}

	// faltan configuraciones especiales de la toolbar, posicion por ejemplo

}
