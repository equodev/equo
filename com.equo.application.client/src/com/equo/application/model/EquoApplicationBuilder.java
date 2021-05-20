package com.equo.application.model;

import java.net.URISyntaxException;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.equo.application.api.IEquoApplication;
import com.equo.application.handlers.ParameterizedCommandRunnable;
import com.equo.application.impl.HandlerBuilder;
import com.equo.application.util.IConstants;
import com.equo.ws.api.IEquoEventHandler;
import com.equo.ws.api.JsonPayloadEquoRunnable;

@Component(service = EquoApplicationBuilder.class)
public class EquoApplicationBuilder {
	private static EquoApplicationBuilder currentBuilder;
	private static final String ECLIPSE_RCP_APP_ID = "org.eclipse.ui.ide.workbench";

	private MApplication mApplication;
	private MTrimmedWindow mWindow;
	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
	private ViewBuilder viewBuilder;
	private EquoApplicationModel equoApplicationModel;
	private String applicationName;
	private IEquoEventHandler equoEventHandler;
	private OptionalViewBuilder optionalViewBuilder;

	/**
	 * Builds an Equo application by wrapping up an existing web app.
	 * 
	 * @param url the website to be wrapped
	 * @return
	 */
	public OptionalViewBuilder wrap(String url) {
		optionalViewBuilder = this.getViewBuilder().withSingleView(url);
		return optionalViewBuilder;
	}

	/**
	 * Builds an Equo App with the UI starting point in the HTML file given by
	 * parameter.
	 * 
	 * @param baseHtmlFile path to HTML file, relative to 'resources' folder
	 * @return
	 * @throws URISyntaxException
	 */
	public OptionalViewBuilder withUI(String baseHtmlFile) throws URISyntaxException {
		optionalViewBuilder = this.getViewBuilder().withBaseHtml(baseHtmlFile);
		return optionalViewBuilder;
	}

	/**
	 * Configures the Equo application builder. This method is intended to be called
	 * by the Equo Framework, it should not be called by clients/users applications.
	 * 
	 * @param equoApplicationModel
	 * @param equoApp
	 * @return
	 */
	OptionalViewBuilder configure(EquoApplicationModel equoApplicationModel, IEquoApplication equoApp) {
		currentBuilder = this;
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

		if (!isAnEclipseBasedApp()) {
			configureEquoApp(appId);
			return this.viewBuilder.configureViewPart(this, equoApp);
		}
		return null;
	}

	private void configureEquoApp(String appId) {
		MMenu mainMenu = MenuFactoryImpl.eINSTANCE.createMenu();
		mainMenu.setElementId(appId + "." + "mainmenu");
		getmWindow().setMainMenu(mainMenu);

		createDefaultBindingContexts();

		MBindingTable mainWindowBindingTable = MCommandsFactory.INSTANCE.createBindingTable();
		mainWindowBindingTable.setBindingContext(getmApplication().getBindingContexts().get(0));
		mainWindowBindingTable.setElementId(IConstants.DEFAULT_BINDING_TABLE);

		addAppLevelCommands(getmApplication());

		getmApplication().getBindingTables().add(mainWindowBindingTable);
		configureJavascriptApis();
	}

	private void configureJavascriptApis() {
		equoEventHandler.on("_setMenu", (JsonPayloadEquoRunnable) payload -> {

			CustomDeserializer deserializer = new CustomDeserializer();
			deserializer.registerMenuType(EquoMenuItem.CLASSNAME, EquoMenuItem.class);
			deserializer.registerMenuType(EquoMenuItemSeparator.CLASSNAME, EquoMenuItemSeparator.class);

			Gson gson = new GsonBuilder().registerTypeAdapter(Menu.class, deserializer).create();
			gson.fromJson(payload, Menu.class).setApplicationMenu();
		});

		equoEventHandler.on("_getMenu", (JsonPayloadEquoRunnable) payload -> {
			equoEventHandler.send("_doGetMenu", Menu.getActiveMenu().serialize());
		});

		equoEventHandler.on("_addShortcut", (JsonPayloadEquoRunnable) payload -> {
			final String shortcut = payload.get("shortcut").getAsString();
			final String event = payload.get("event").getAsString();
			new GlobalShortcutBuilder(this, this.viewBuilder.getPart().getElementId(), null, event)
					.addGlobalShortcut(shortcut);
		});
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
	void setEquoEventHandler(IEquoEventHandler equoEventHandler) {
		this.equoEventHandler = equoEventHandler;
	}

	private boolean isAnEclipseBasedApp() {
		return ECLIPSE_RCP_APP_ID.equals(System.getProperty("eclipse.application"));
	}

	OptionalViewBuilder getOptionalViewBuilder() {
		return optionalViewBuilder;
	}

	static EquoApplicationBuilder getCurrentBuilder() {
		return currentBuilder;
	}

}
