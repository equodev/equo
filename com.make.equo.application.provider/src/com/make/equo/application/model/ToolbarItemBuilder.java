package com.make.equo.application.model;


import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuFactoryImpl;
import com.make.equo.application.util.IConstants;

public class ToolbarItemBuilder extends ItemBuilder {

	private MHandledToolItem toolItem;
	private ToolbarBuilder toolbarBuilder;
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
		this.setItemHandlerBuilder(new ToolbarItemHandlerBuilder(this));
		this.toolItem.getTransientData().put(IConstants.IS_AN_EQUO_MODEL_ELEMENT, true);
		this.toolItem.getTransientData().put(IConstants.EQUO_WEBSOCKET_USER_EMITTED_EVENT, userEvent);
		ItemBuilder toolbarItemBuilder = this.getItemHandlerBuilder().onClick(action, userEvent);
		return (ToolbarItemBuilder) toolbarItemBuilder;
	}

	public ToolbarBuilder addToolbar() {
		return new ToolbarBuilder(this.toolbarBuilder).addToolbar();
	}

	public ToolbarBuilder getToolbarBuilder() {
		return toolbarBuilder;
	}

	public MHandledToolItem getToolItem() {
		return toolItem;
	}

	@Override
	public OptionalViewBuilder getOptionalFieldBuilder() {
		return this.toolbarBuilder.getOptionalFieldBuilder();
	}

}
