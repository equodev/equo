package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;
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
		this.iconId = toolbarItemBuilder.getIcon();
		this.text = toolbarItemBuilder.getTooltip();
		this.setItem(toolbarItemBuilder.getItem());
		this.toolbarBuilder = toolbarItemBuilder.toolbarBuilder;
	}

	public ToolbarItemBuilder addToolItem(String iconId, String text) {
		String actualIcon = this.iconId;
		String actualText = this.text;
		MHandledItem actualItem = this.getItem();
		this.iconId = iconId;
		this.text = text;
		this.setItem(createToolItem());
		ToolbarItemBuilder newItemBuilder = new ToolbarItemBuilder(this);
		this.iconId = actualIcon;
		this.text = actualText;
		this.setItem(actualItem);
		return newItemBuilder;
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
	@Override
	public ToolbarItemBuilder addShortcut(String keySequence) {
		return (ToolbarItemBuilder)super.addShortcut(keySequence);
	}

	public ToolbarBuilder addToolbar() {
		return new ToolbarBuilder(this.toolbarBuilder).addToolbar();
	}

	ToolbarBuilder getToolbarBuilder() {
		return toolbarBuilder;
	}

	String getTooltip() {
		return text;
	}

	String getIcon() {
		return iconId;
	}
	
	
	
	
}
