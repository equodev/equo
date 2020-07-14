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

	public EquoMenuItem(String title) {
		setTitle(title);
	}

	public EquoMenuItem(String title, Runnable runnable) {
		this(title);
		setRunnable(runnable);
	}

	public EquoMenuItem(String title, Runnable runnable, String shortcut) {
		this(title, runnable);
		setShortcut(shortcut);
	}

	public void setRunnable(Runnable runnable) {
		this.runnable = runnable;
	}

	public void setShortcut(String shortcut) {
		this.shortcut = shortcut;
	}

	public void setAction(String action) {
		this.action = action;
		this.runnable = () -> {};
	}

	@Override
	void implement(MenuBuilder menuBuilder) {
		MenuItemBuilder itemBuilder = menuBuilder.addMenuItem(getTitle());
		if (runnable != null) {
			itemBuilder = itemBuilder.onClick(runnable, action);
			if (shortcut != null) {
				itemBuilder.addShortcut(shortcut);
			}
		}
	}

	static AbstractEquoMenu getElement(MMenuElement element) {
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

			EquoMenuItem item = new EquoMenuItem(element.getLabel(), runnable, shortcut);
			item.setAction(action);
			return item;
		} else {
			return EquoMenuItemSeparator.getElement(element);
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
