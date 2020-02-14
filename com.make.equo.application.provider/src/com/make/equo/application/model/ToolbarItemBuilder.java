package com.make.equo.application.model;


import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuFactoryImpl;

public class ToolbarItemBuilder {

	private MHandledToolItem toolItem;
	private ToolbarBuilder toolbarBuilder;
	private ToolbarItemHandlerBuilder toolbarItemHandlerBuilder;
	private String iconURI;
	private String tooltip;

	ToolbarItemBuilder(ToolbarBuilder toolbarBuilder) {
		this.toolbarBuilder = toolbarBuilder;
	}

	ToolbarItemBuilder(ToolbarItemBuilder toolbarItemBuilder) {
		this.toolItem = toolbarItemBuilder.toolItem;
		this.toolbarBuilder = toolbarItemBuilder.toolbarBuilder;
	}

	public ToolbarItemBuilder addToolItem(String iconURI, String tooltip) {
		this.iconURI = iconURI;
		this.tooltip = tooltip;
		this.toolItem = createToolItem();
		return new ToolbarItemBuilder(this);
	}

	// hardcoded
	public MHandledToolItem createToolItem() {
		MHandledToolItem newToolItem = MenuFactoryImpl.eINSTANCE.createHandledToolItem();
		newToolItem.setIconURI(this.iconURI);
		newToolItem.setElementId("org.eclipse.ui.file.save");
		newToolItem.setTooltip(this.tooltip);
		newToolItem.setVisible(true);

		return newToolItem;
	}
	

	public ToolbarItemBuilder onClick(Runnable action) {
		return onClick(action, null);
	}

	public ToolbarItemBuilder onClick(Runnable action, String usrEvent) {
		this.toolbarItemHandlerBuilder = new ToolbarItemHandlerBuilder(this);
		ToolbarItemBuilder toolbarItemBuilder = toolbarItemHandlerBuilder.onClick(action, usrEvent);
		return toolbarItemBuilder;
	}

	public ToolbarBuilder getToolbarBuilder() {
		return toolbarBuilder;
	}

	public MHandledToolItem getToolItem() {
		return toolItem;
	}
	
	public EquoApplicationBuilder start() {
		return toolbarBuilder.getOptionalFieldBuilder().start();
	}

}
