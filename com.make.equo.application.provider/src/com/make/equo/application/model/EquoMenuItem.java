package com.make.equo.application.model;

public class EquoMenuItem extends AbstractEquoMenu {
	private Runnable runnable = null;
	private String shortcut = null;

	public EquoMenuItem(String title) {
		setTitle(title);
	}

	public EquoMenuItem(String title, Runnable runnable) {
		this(title);
		this.runnable = runnable;
	}

	public EquoMenuItem(String title, Runnable runnable, String shortcut) {
		this(title, runnable);
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

}
