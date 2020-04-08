package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuFactoryImpl;
import com.make.equo.application.util.IConstants;

public class ToolbarItemBuilder extends ItemBuilder {

	private ToolbarBuilder toolbarBuilder;

	ToolbarItemBuilder(ToolbarBuilder toolbarBuilder) {
		super(toolbarBuilder.getOptionalFieldBuilder());
		this.toolbarBuilder = toolbarBuilder;
	}

	public ToolbarItemBuilder(OptionalViewBuilder optionalFieldBuilder, MHandledItem item,
			ToolbarBuilder toolbarBuilder) {
		super(optionalFieldBuilder);
		this.setItem(item);
		this.toolbarBuilder = toolbarBuilder;
	}

	public ToolbarItemBuilder addToolItem(String iconId, String text) {

		return new ToolbarItemBuilder(this.getOptionalFieldBuilder(), createToolItem(iconId, text),
				this.toolbarBuilder);
	}

	private MHandledToolItem createToolItem(String iconId, String text) {
		MHandledToolItem newToolItem = MenuFactoryImpl.eINSTANCE.createHandledToolItem();
		newToolItem.setIconURI(iconId);
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
		return (ToolbarItemBuilder) super.addShortcut(keySequence);
	}

	public ToolbarBuilder addToolbar() {
		return new ToolbarBuilder(this.toolbarBuilder).addToolbar();
	}

	ToolbarBuilder getToolbarBuilder() {
		return toolbarBuilder;
	}
}
