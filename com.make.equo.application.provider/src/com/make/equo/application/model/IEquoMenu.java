package com.make.equo.application.model;

public interface IEquoMenu {
	/**
	 * Creates a root menu, visible in the bar.
	 * 
	 * @param title the menu title.
	 * @return the EquoMenu instance.
	 */
	public EquoMenu withMainMenu(String title);

	/**
	 * Sets the application menu to the current app menu.
	 */
	public void setApplicationMenu();
}
