package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;

import com.google.gson.JsonObject;

public abstract class AbstractEquoMenu {
	private String title;

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
	static AbstractEquoMenu getElement(MMenuElement element) {
		return EquoMenu.getElement(element);
	}

	public abstract JsonObject serialize();
}
