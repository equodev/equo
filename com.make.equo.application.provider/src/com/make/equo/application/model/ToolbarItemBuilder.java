package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuFactoryImpl;
import com.make.equo.application.util.IConstants;

public class ToolbarItemBuilder extends ItemBuilder {

	private ToolbarBuilder toolbarBuilder;
	private String iconId;
	private String text;

	ToolbarItemBuilder(ToolbarBuilder toolbarBuilder) {
		super(toolbarBuilder.getOptionalFieldBuilder());
		this.toolbarBuilder = toolbarBuilder;
	}

	ToolbarItemBuilder(ToolbarItemBuilder toolbarItemBuilder) {
		super(toolbarItemBuilder.getOptionalFieldBuilder());
		this.setItem(toolbarItemBuilder.getItem());
		this.toolbarBuilder = toolbarItemBuilder.toolbarBuilder;
	}

	public ToolbarItemBuilder addToolItem(String iconId, String text) {
		this.iconId = iconId;
		this.text = text;
		this.setItem(createToolItem());
		return new ToolbarItemBuilder(this);
	}

	public MHandledToolItem createToolItem() {
		MHandledToolItem newToolItem = MenuFactoryImpl.eINSTANCE.createHandledToolItem();
		newToolItem.setIconURI(this.iconId);
		String itemId = toolbarBuilder.getToolbar().getElementId() + "."
				+ text.replace(" ", "_").replaceAll("\\s+", "").toLowerCase();
		newToolItem.setElementId(itemId);
		newToolItem.setTooltip(text);
		newToolItem.setVisible(true);
		toolbarBuilder.getToolbar().getChildren().add(newToolItem);
		return newToolItem;
	}

	public ToolbarItemBuilder onClick(Runnable action) {
		return (ToolbarItemBuilder) onClick(action, null);
	}

	public ToolbarBuilder addToolbar() {
		return new ToolbarBuilder(this.toolbarBuilder).addToolbar();
	}

	public ToolbarBuilder getToolbarBuilder() {
		return toolbarBuilder;
	}
}
