package com.make.equo.application.model;

import java.util.List;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MBindingContext;
import org.eclipse.e4.ui.model.application.commands.MBindingTable;
import org.eclipse.e4.ui.model.application.commands.MCommandParameter;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuFactoryImpl;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.google.common.collect.Lists;
import com.make.equo.application.EquoApplicationModel;
import com.make.equo.application.handlers.ParameterizedCommandRunnable;
import com.make.equo.application.impl.HandlerBuilder;
import com.make.equo.application.util.IConstants;
import com.make.equo.ws.api.EquoEventHandler;

@Component(service = EquoApplicationBuilder.class)
public class EquoApplicationBuilder {

	private MApplication mApplication;
	private MTrimmedWindow mWindow;
	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
	private ViewBuilder viewBuilder;
	private EquoApplicationModel equoApplicationModel;
	private String applicationName;
	private EquoEventHandler equoEventHandler;

	public OptionalViewBuilder withSingleView(String url) {
		return this.getViewBuilder().withSingleView(url);
	}

	/**
	 * Configure the Equo application builder. This method is intended to be called
	 * by the Equo Framework, it should not be called by clients/users applications.
	 * 
	 * @param equoApplicationModel
	 * @return
	 */
	OptionalViewBuilder configure(EquoApplicationModel equoApplicationModel) {
		this.equoApplicationModel = equoApplicationModel;
		this.mApplication = this.equoApplicationModel.getMainApplication();
		this.mWindow = (MTrimmedWindow) getmApplication().getChildren().get(0);
		this.applicationName = System.getProperty("appName");
		String appId;
		if (applicationName != null) {
			appId = IConstants.EQUO_APP_PREFIX + "." + applicationName.trim().toLowerCase();
			getmWindow().setLabel(applicationName);
		} else {
			appId = IConstants.EQUO_APP_PREFIX;
		}
		MMenu mainMenu = MenuFactoryImpl.eINSTANCE.createMenu();
		mainMenu.setElementId(appId + "." + "mainmenu");
		getmWindow().setMainMenu(mainMenu);

		createDefaultBindingContexts();

		MBindingTable mainWindowBindingTable = MCommandsFactory.INSTANCE.createBindingTable();
		mainWindowBindingTable.setBindingContext(getmApplication().getBindingContexts().get(0));
		mainWindowBindingTable.setElementId(IConstants.DEFAULT_BINDING_TABLE);

		addAppLevelCommands(getmApplication());

		getmApplication().getBindingTables().add(mainWindowBindingTable);

		return this.viewBuilder.configureViewPart(this);
	}

	private void addAppLevelCommands(MApplication mApplication) {
		createWebSocketTriggeredCommand(mApplication, IConstants.EQUO_WEBSOCKET_OPEN_BROWSER,
				IConstants.OPEN_BROWSER_COMMAND_CONTRIBUTION_URI);

		equoEventHandler.on("openBrowser", new ParameterizedCommandRunnable(IConstants.EQUO_WEBSOCKET_OPEN_BROWSER,
				getmApplication().getContext()));

		// if (action.equals(EXECUTE_ACTION_ID)) {
		// TODO call user application code with the class passed as param

		createOpenBrowserAsWindowCommand(mApplication, IConstants.EQUO_OPEN_BROWSER_AS_WINDOW,
				IConstants.OPEN_BROWSER_AS_WINDOW_COMMAND_CONTRIBUTION_URI);
		createOpenBrowserAsSidePartCommand(mApplication, IConstants.EQUO_OPEN_BROWSER_AS_SIDE_PART,
				IConstants.OPEN_BROWSER_AS_SIDE_PART_COMMAND_CONTRIBUTION_URI);
		createUpdateBrowserCommand(mApplication, IConstants.EQUO_WEBSOCKET_UPDATE_BROWSER,
				IConstants.UPDATE_BROWSER_CONTRIBUTION_URI);

		equoEventHandler.on("updateBrowser", new ParameterizedCommandRunnable(IConstants.EQUO_WEBSOCKET_UPDATE_BROWSER,
				getmApplication().getContext()));
	}

	private void createUpdateBrowserCommand(MApplication mApplication, String commandId,
			String commandContributionUri) {
		HandlerBuilder handlerBuilder = new HandlerBuilder(mApplication, commandId, commandContributionUri) {
			@Override
			protected Runnable getRunnable() {
				return null;
			}

			@Override
			protected List<MCommandParameter> createCommandParameters() {
				MCommandParameter partNameCommandParameter = createCommandParameter(IConstants.EQUO_BROWSER_PART_NAME,
						"Part Name", true);
				return Lists.newArrayList(partNameCommandParameter);
			}

		};
		handlerBuilder.createCommandAndHandler(commandId);
	}

	private void createOpenBrowserAsSidePartCommand(MApplication mApplication, String commandId,
			String commandContributionUri) {
		HandlerBuilder handlerBuilder = new HandlerBuilder(mApplication, commandId, commandContributionUri) {
			@Override
			protected Runnable getRunnable() {
				return null;
			}

			@Override
			protected List<MCommandParameter> createCommandParameters() {
				MCommandParameter partNameCommandParameter = createCommandParameter(IConstants.EQUO_BROWSER_PART_NAME,
						"Part Name", true);
				MCommandParameter partPositionCommandParameter = createCommandParameter(
						IConstants.EQUO_BROWSER_PART_POSITION, "Part Position", true);
				return Lists.newArrayList(partNameCommandParameter, partPositionCommandParameter);
			}

		};
		handlerBuilder.createCommandAndHandler(commandId);
	}

	private void createOpenBrowserAsWindowCommand(MApplication mApplication, String commandId,
			String commandContributionUri) {
		HandlerBuilder handlerBuilder = new HandlerBuilder(mApplication, commandId, commandContributionUri) {
			@Override
			protected Runnable getRunnable() {
				return null;
			}

			@Override
			protected List<MCommandParameter> createCommandParameters() {
				MCommandParameter windowNameCommandParameter = createCommandParameter(
						IConstants.EQUO_BROWSER_WINDOW_NAME, "Browser Window Name", true);
				return Lists.newArrayList(windowNameCommandParameter);
			}
		};
		handlerBuilder.createCommandAndHandler(commandId);
	}

	private void createWebSocketTriggeredCommand(MApplication mApplication, String commandId,
			String commandContributionUri) {
		HandlerBuilder handlerBuilder = new HandlerBuilder(mApplication, commandId, commandContributionUri) {
			@Override
			protected Runnable getRunnable() {
				return null;
			}
		};
		handlerBuilder.createCommandAndHandler(commandId);
	}

	private void createDefaultBindingContexts() {
		MBindingContext windowAndDialogBindingContext = MCommandsFactory.INSTANCE.createBindingContext();
		windowAndDialogBindingContext.setElementId(IConstants.DIALOGS_AND_WINDOWS_BINDING_CONTEXT);
		windowAndDialogBindingContext.setName("Dialogs and Windows Binding Context");

		MBindingContext windowBindingContext = MCommandsFactory.INSTANCE.createBindingContext();
		windowBindingContext.setElementId(IConstants.WINDOWS_BINDING_CONTEXT);
		windowBindingContext.setName("Windows Binding Context");

		MBindingContext dialogBindingContext = MCommandsFactory.INSTANCE.createBindingContext();
		dialogBindingContext.setElementId(IConstants.DIALOGS_BINDING_CONTEXT);
		dialogBindingContext.setName("Dialogs Binding Context");

		// keep this order, the order in which they are added is important.
		getmApplication().getBindingContexts().add(windowAndDialogBindingContext);
		getmApplication().getBindingContexts().add(windowBindingContext);
		getmApplication().getBindingContexts().add(dialogBindingContext);
	}

	protected ViewBuilder getViewBuilder() {
		return viewBuilder;
	}

	MApplication getmApplication() {
		return mApplication;
	}

	MTrimmedWindow getmWindow() {
		return mWindow;
	}

	@Reference(cardinality = ReferenceCardinality.MANDATORY)
	void setEquoEventHandler(EquoEventHandler equoEventHandler) {
		this.equoEventHandler = equoEventHandler;
	}

}
