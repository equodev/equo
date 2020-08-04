package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuItem;

import com.make.equo.application.util.IConstants;

import com.google.gson.JsonObject;

public class EquoMenuItem extends AbstractEquoMenu {
	private Runnable runnable = null;
	private String action = null;
	private String shortcut = null;
	public static final String CLASSNAME = "EquoMenuItem";

	EquoMenuItem(IEquoMenu parent, String title) {
		super(parent, title);
	}

	public void onClick(Runnable runnable) {
		this.runnable = runnable;
	}

	public void withShortcut(String shortcut) {
		this.shortcut = shortcut;
	}

	public void onClick(String action) {
		this.action = action;
	}

	@Override
	void implement(MenuBuilder menuBuilder) {
		MenuItemBuilder itemBuilder = menuBuilder.addMenuItem(getTitle());
		if (runnable != null || action != null) {
			itemBuilder = itemBuilder.onClick(runnable, action);
			if (shortcut != null) {
				itemBuilder.addShortcut(shortcut);
			}
		}
	}

	static AbstractEquoMenu getElement(IEquoMenu parent, MMenuElement element) {
		if (element instanceof MMenuItem) {
			MMenuItem menuItem = ((MMenuItem) element);

			String shortcut = null;
			Object shortcutObject = menuItem.getTransientData().get(IConstants.ITEM_SHORTCUT);
			if (shortcutObject != null) {
				shortcut = (String) shortcutObject;
			}

			Runnable runnable = null;
			Object runnableObject = menuItem.getTransientData().get(IConstants.ITEM_RUNNABLE);
			if (runnableObject != null) {
				runnable = (Runnable) runnableObject;
			}

			String action = null;
			Object actionObject = menuItem.getTransientData().get(IConstants.ITEM_ACTION);
			if (actionObject != null) {
				action = (String) actionObject;
			}

			EquoMenuItem item = new EquoMenuItem(parent, element.getLabel());
			item.onClick(runnable);
			item.withShortcut(shortcut);
			item.onClick(action);
			return item;
		} else {
			return EquoMenuItemSeparator.getElement(parent, element);
		}
	}

	@Override
	public JsonObject serialize() {
		JsonObject jOb = new JsonObject();
		jOb.addProperty("type", CLASSNAME);
		jOb.addProperty("title", getTitle());
		
		if (shortcut != null)
			jOb.addProperty("shortcut", shortcut);

		if (action != null)
			jOb.addProperty("action", action);
		return jOb;
	}

}
