package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;

import com.google.gson.JsonObject;

public abstract class AbstractEquoMenu implements IEquoMenu {
	private String title;
	private IEquoMenu parent;

	AbstractEquoMenu(IEquoMenu parent, String title) {
		this.parent = parent;
		setTitle(title);
	}

	protected IEquoMenu getParent() {
		return parent;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	abstract void implement(MenuBuilder menuBuilder);

	public String getTitle() {
		return this.title;
	}

	/**
	 * Gets an element of AbstractEquoMenu that represents an existing element of
	 * the Eclipse menu model
	 * 
	 * @param element The element of the Eclipse model to be represented
	 * @return
	 */
	static AbstractEquoMenu getElement(IEquoMenu parent, MMenuElement element) {
		return EquoMenu.getElement(parent, element);
	}

	public abstract JsonObject serialize();

	@Override
	public EquoMenu withMainMenu(String title) {
		return getParent().withMainMenu(title);
	}

	public EquoMenuItem addMenuItem(String title) {
		return ((AbstractEquoMenu) getParent()).addMenuItem(title);
	}

	public EquoMenu addMenu(String title) {
		return ((AbstractEquoMenu) getParent()).addMenu(title);
	}

	public EquoMenuItemSeparator addMenuItemSeparator() {
		return ((AbstractEquoMenu) getParent()).addMenuItemSeparator();
	}

	@Override
	public void setUpMenus() {
		getParent().setUpMenus();
	}

}
