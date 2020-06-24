package com.make.equo.application.model;

public abstract class AbstractEquoMenu {
	private String title;

	public void setTitle(String title) {
		this.title = title;
	}
	
	abstract void implement(MenuBuilder menuBuilder);

	public String getTitle() {
		return this.title;
	}
}
