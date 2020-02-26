package com.make.equo.application.model;


import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuFactoryImpl;

public class ToolbarItemBuilder {

	private MHandledToolItem toolItem;
	private ToolbarBuilder toolbarBuilder;
	private ToolbarItemHandlerBuilder toolbarItemHandlerBuilder;
	private String iconId;
	private String text;

	ToolbarItemBuilder(ToolbarBuilder toolbarBuilder) {
		this.toolbarBuilder = toolbarBuilder;
	}

	ToolbarItemBuilder(ToolbarItemBuilder toolbarItemBuilder) {
		this.toolItem = toolbarItemBuilder.toolItem;
		this.toolbarBuilder = toolbarItemBuilder.toolbarBuilder;
	}

	public ToolbarItemBuilder addToolItem(String iconId, String text) {
		this.iconId = iconId;
		this.text = text;
		this.toolItem = createToolItem();
		return new ToolbarItemBuilder(this);
	}

	// hardcoded
	public MHandledToolItem createToolItem() {
		MHandledToolItem newToolItem = MenuFactoryImpl.eINSTANCE.createHandledToolItem();
		newToolItem.setIconURI(this.iconId);
		String itemId = toolbarBuilder.getToolbar().getElementId() + "." + text.replace(" ", "_").replaceAll("\\s+", "").toLowerCase();
		newToolItem.setElementId(itemId);
		newToolItem.setTooltip(text);
		newToolItem.setVisible(true);
		toolbarBuilder.getToolbar().getChildren().add(newToolItem);
		return newToolItem;
	}

	public ToolbarItemBuilder onClick(Runnable action) {
		return onClick(action, null);
	}

	public ToolbarItemBuilder onClick(Runnable action, String userEvent) {
		this.toolbarItemHandlerBuilder = new ToolbarItemHandlerBuilder(this);
		ToolbarItemBuilder toolbarItemBuilder = toolbarItemHandlerBuilder.onClick(action, userEvent);
		return toolbarItemBuilder;
	}
	
	public ToolbarBuilder addToolbar() {
		return new ToolbarBuilder(this.toolbarBuilder).addToolbar();
	}
	
	public ToolbarBuilder withToolbar() {
		return new ToolbarBuilder(this.toolbarBuilder.getOptionalFieldBuilder(),this.toolbarBuilder.getParent());
	}
	
	public MenuBuilder withMainMenu(String label) {
		return new MenuBuilder(toolbarBuilder.getOptionalFieldBuilder()).addMenu(label);
	}
	
	public ToolbarItemBuilder addShortcut(String keySequence) {
		if (toolbarItemHandlerBuilder != null) {
			return toolbarItemHandlerBuilder.addShortcut(keySequence);
		}
		// log that there is no menu item handler -> no onClick method was called.
		return this;
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
