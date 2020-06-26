package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuItem;

import com.make.equo.application.util.IConstants;

public class EquoMenuItem extends AbstractEquoMenu {
	private Runnable runnable = null;
	private String shortcut = null;

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

	@Override
	void implement(MenuBuilder menuBuilder) {
		MenuItemBuilder itemBuilder = menuBuilder.addMenuItem(getTitle());
		if (runnable != null) {
			itemBuilder = itemBuilder.onClick(runnable);
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

			EquoMenuItem item = new EquoMenuItem(element.getLabel(), runnable, shortcut);
			return item;
		} else {
			return EquoMenuItemSeparator.getElement(element);
		}
	}

}
