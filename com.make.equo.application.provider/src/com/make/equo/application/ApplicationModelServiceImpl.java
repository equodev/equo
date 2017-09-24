package com.make.equo.application;

import java.util.NoSuchElementException;

import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.osgi.service.component.annotations.Component;

import com.make.equo.application.api.ApplicationModelService;
import com.make.equo.application.parts.MainPagePart;

@Component
public class ApplicationModelServiceImpl implements ApplicationModelService {

	private MTrimmedWindow mWindowTemp;
	private MTrimmedWindow mainWindow;

	@Override
	public void initializeAppModel(String name) {
		mWindowTemp = MBasicFactory.INSTANCE.createTrimmedWindow();
		mWindowTemp.setX(42);
		mWindowTemp.setY(80);
		mWindowTemp.setHeight(563);
		mWindowTemp.setWidth(900);
		mWindowTemp.setElementId("com.make.equo.trimmedwindow.mainpage");
		mWindowTemp.setLabel(name);
//		application.getChildren().add(mWindow);
	}

	@Override
	public void setMainWindowUrl(String url) {
		MPart mainPart = MBasicFactory.INSTANCE.createPart();
		mainPart.setElementId(MainPagePart.ID);
		mainPart.setContributionURI("bundleclass://com.make.equo.application.provider/com.make.equo.application.parts.MainPagePart");
		mainPart.getProperties().put(MainPagePart.MAIN_URL_KEY, url);
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
}
