package com.make.equo.application.model;

import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;

public class EquoMenuItemSeparator extends AbstractEquoMenu {

	@Override
	void implement(MenuBuilder menuBuilder) {
		new MenuItemSeparatorBuilder(menuBuilder).addMenuItemSeparator();
	}

	static AbstractEquoMenu getElement(MMenuElement element) {
		return new EquoMenuItemSeparator();
	}

}
