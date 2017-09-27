package com.make.equo.application;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MHandler;
import org.eclipse.e4.ui.model.application.commands.impl.CommandsFactoryImpl;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuFactoryImpl;
import org.osgi.service.component.annotations.Component;

import com.make.equo.application.api.ApplicationModelService;
import com.make.equo.application.model.IMenuHandler;
import com.make.equo.application.util.IConstants;

@Component
public class ApplicationModelServiceImpl implements ApplicationModelService {

	private MTrimmedWindow mWindowTemp;
	private MTrimmedWindow mainWindow;
	private Map<String, MCommand> commands;
	private Map<String, MMenu> menus;
	
	private String appId;

	@Override
	public void initializeAppModel(String name) {
		this.appId = IConstants.EQUO_APP_PREFIX + "." + name.trim().toLowerCase();
		this.commands = new HashMap<>();
		this.menus = new HashMap<>();
		mWindowTemp = MBasicFactory.INSTANCE.createTrimmedWindow();
		mWindowTemp.setX(42);
		mWindowTemp.setY(80);
		mWindowTemp.setHeight(563);
		mWindowTemp.setWidth(900);
		mWindowTemp.setElementId(IConstants.MAIN_WINDOW_ID);
		mWindowTemp.setLabel(name);
		MMenu mainMenu = MenuFactoryImpl.eINSTANCE.createMenu();
		mainMenu.setElementId(appId + "." + "mainmenu");
		menus.put(mainMenu.getElementId(), mainMenu);
		mWindowTemp.setMainMenu(mainMenu);
//		application.getChildren().add(mWindow);
	}

	@Override
	public void setMainWindowUrl(String url) {
		MPart mainPart = MBasicFactory.INSTANCE.createPart();
		mainPart.setElementId(IConstants.MAIN_PART_ID);
		mainPart.setContributionURI("bundleclass://com.make.equo.application.provider/com.make.equo.application.parts.MainPagePart");
		mainPart.getProperties().put(IConstants.MAIN_URL_KEY, url);
		mWindowTemp.getChildren().add(mainPart);
	}

	@Override
	public void buildModelApp() {
		mainWindow = mWindowTemp;
		mainWindow.setToBeRendered(true);
		mainWindow.setVisible(true);
	}
	
	@Override
	public MWindow getMainWindow() {
		if (mainWindow == null) {
			throw new NoSuchElementException("The main window has not been constructed. Be sure to construct it before calling this method.");
		}
		return mainWindow;
	}

	@Override
	public String addMenu(String menuLabel) {
		MMenu mainMenu = mWindowTemp.getMainMenu();
		MMenu newMenu = MenuFactoryImpl.eINSTANCE.createMenu();
		newMenu.setElementId(mainMenu.getElementId() + "." + menuLabel.trim().toLowerCase());
		newMenu.setLabel(menuLabel);
		mainMenu.getChildren().add(newMenu);
		menus.put(newMenu.getElementId(), newMenu);
		return newMenu.getElementId();
	}

	@Override
	public String addMenuItem(String parentId, String menuItemlabel) {
		MHandledMenuItem newMenuItem = MenuFactoryImpl.eINSTANCE.createHandledMenuItem();
		MMenu parentMenu = menus.get(parentId);
		String menuItemId = parentMenu.getElementId() + "." + menuItemlabel.trim().toLowerCase();
		newMenuItem.setElementId(menuItemId);
		newMenuItem.setLabel(menuItemlabel);
		MCommand newCommand = createNewCommand(parentMenu.getLabel() + "." + menuItemlabel);
		newMenuItem.setCommand(newCommand);
		parentMenu.getChildren().add(newMenuItem);
		return menuItemId;
	}

	private MCommand createNewCommand(String suffix) {
		String commandId = IConstants.EQUO_COMMAND_PREFIX + "." + suffix.trim().toLowerCase();
		if (this.commands.containsKey(commandId)) {
			return this.commands.get(commandId);
		} else {
			MCommand newCommand = CommandsFactoryImpl.eINSTANCE.createCommand();
			newCommand.setElementId(commandId);
			commands.put(commandId, newCommand);
			return newCommand;
		}
	}

	@Override
	public void addHandler(String commandSufix, IMenuHandler menuHanlder) {
		MCommand command = createNewCommand(commandSufix);
		MHandler newHandler = CommandsFactoryImpl.eINSTANCE.createHandler();
		newHandler.setElementId(IConstants.EQUO_HANDLER_PREFIX + "." + commandSufix.trim().toLowerCase());
		newHandler.setCommand(command);
		newHandler.setContributionURI("bundleclass://com.make.equo.application.provider/com.make.equo.application.handlers.DefaultHandler");
//		newHandler.getTransientData().put(IConstants.DEFAULT_HANDLER_IMPL_ID, menuHandler);
	}

	@Override
	public String addMenu(String parentId, String menuLabel) {
		MMenu parentMenu = menus.get(parentId);
		MMenu newMenu = MenuFactoryImpl.eINSTANCE.createMenu();
		newMenu.setElementId(parentMenu.getElementId() + "." + menuLabel.trim().toLowerCase());
		newMenu.setLabel(menuLabel);
		parentMenu.getChildren().add(newMenu);
		menus.put(newMenu.getElementId(), newMenu);
		return newMenu.getElementId();
	}
}
