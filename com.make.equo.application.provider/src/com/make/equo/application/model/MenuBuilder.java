package com.make.equo.application.model;

import java.util.List;

import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuFactoryImpl;

import com.make.equo.application.util.IConstants;

public class MenuBuilder {

	private OptionalViewBuilder optionalFieldBuilder;
	private MMenu parentMenu;
	private MMenu menu;

	MenuBuilder(OptionalViewBuilder optionalFieldBuilder) {
		this.parentMenu = optionalFieldBuilder.getMainMenu();
		if (this.parentMenu == null) {
			this.parentMenu = optionalFieldBuilder.getEquoApplicationBuilder().getmWindow().getMainMenu();
		}
		this.optionalFieldBuilder = optionalFieldBuilder;
	}

	MenuBuilder(MenuBuilder menuBuilder) {
		this.parentMenu = menuBuilder.menu;
		this.menu = menuBuilder.menu;
		this.optionalFieldBuilder = menuBuilder.optionalFieldBuilder;
	}

	public MenuBuilder addMenu(String label) {
		for (MMenuElement children : parentMenu.getChildren()) {
			// If already exists a menu with this label, return that one
			if (children instanceof MMenu && children.getLabel().equals(label)) {
				this.menu = (MMenu) children;
				return new MenuBuilder(this);
			}
		}
		menu = createMenu(parentMenu, label);
		return new MenuBuilder(this);
	}

	public MenuBuilder remove() {
		if (menu == null) {
			List<MMenuElement> children = parentMenu.getChildren();
			for (MMenuElement menu : children) {
				removeRecursively(menu);
			}
			children.clear();
		} else {
			removeRecursively(menu);
			parentMenu.getChildren().remove(menu);
		}
		return new MenuBuilder(this);
	}

	private void removeShortcutFromItem(MMenuElement element) {
		if (element instanceof MHandledMenuItem) {
			MHandledMenuItem item = (MHandledMenuItem) element;
			Object shortcut = item.getTransientData().get(IConstants.ITEM_SHORTCUT);
			if (shortcut != null) {
				optionalFieldBuilder.removeShortcut((String) shortcut);
			}
		}
	}

	private void removeRecursively(MMenuElement element) {
		if (element instanceof MMenu) {
			MMenu menu = (MMenu) element;
			List<MMenuElement> childrens = menu.getChildren();
			for (MMenuElement children : childrens) {
				removeShortcutFromItem(children);
				removeRecursively(children);
				children.setVisible(false);
			}
			childrens.clear();
		}
		removeShortcutFromItem(element);
		element.setVisible(false);
	}

	public MenuBuilder removeChildren(String label) {
		List<MMenuElement> childrens = menu.getChildren();
		MMenuElement itemToDelete = null;
		for (MMenuElement children : childrens) {
			if (children.getLabel().equals(label)) {
				removeRecursively(children);
				itemToDelete = children;
				break;
			}
		}
		if (itemToDelete != null) {
			childrens.remove(itemToDelete);
		}
		return new MenuBuilder(this);
	}

	private MMenu createMenu(MMenu parentMenu, String menuLabel) {
		MMenu newMenu = MenuFactoryImpl.eINSTANCE.createMenu();
		newMenu.setElementId(parentMenu.getElementId() + "." + menuLabel.replaceAll("\\s+", "").toLowerCase());
		newMenu.setLabel(menuLabel);
		parentMenu.getChildren().add(newMenu);
		return newMenu;
	}

	public MenuItemBuilder addMenuItem(String label) {
		return new MenuItemBuilder(this).addMenuItem(label);
	}

	public MenuItemBuilder onBeforeExit(String label, Runnable runnable) {
		return new MenuItemBuilder(this).onBeforeExit(label, runnable);
	}

	public MenuItemBuilder onBeforeExit(Runnable runnable) {
		return new MenuItemBuilder(this).onBeforeExit(runnable);
	}

	public MenuItemBuilder onPreferences(String label, Runnable runnable) {
		return new MenuItemBuilder(this).onPreferences(label, runnable);
	}

	public MenuItemBuilder onPreferences(Runnable runnable) {
		return new MenuItemBuilder(this).onPreferences(runnable);
	}

	public MenuItemBuilder onAbout(String label, Runnable runnable) {
		return new MenuItemBuilder(this).onAbout(label, runnable);
	}

	public MenuItemBuilder onAbout(Runnable runnable) {
		return new MenuItemBuilder(this).onAbout(runnable);
	}

	public MenuItemBuilder addFullScreenModeMenuItem(String menuItemLabel) {
		return new MenuItemBuilder(this).addFullScreenModeMenuItem(menuItemLabel);
	}

	OptionalViewBuilder getOptionalFieldBuilder() {
		return optionalFieldBuilder;
	}

	MMenu getParentMenu() {
		return parentMenu;
	}

	MMenu getMenu() {
		return menu;
	}
}
