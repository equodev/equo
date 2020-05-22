package com.make.equo.application.model;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MBindingContext;
import org.eclipse.e4.ui.model.application.commands.MBindingTable;
import org.eclipse.e4.ui.model.application.commands.MCommand;
import org.eclipse.e4.ui.model.application.commands.MCommandParameter;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.impl.MenuFactoryImpl;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.make.equo.application.api.IEquoApplication;
import com.make.equo.application.handlers.ParameterizedCommandRunnable;
import com.make.equo.application.handlers.filesystem.DeleteFileHandler;
import com.make.equo.application.handlers.filesystem.FileInfoHandler;
import com.make.equo.application.handlers.filesystem.MoveFileHandler;
import com.make.equo.application.handlers.filesystem.OpenFileHandler;
import com.make.equo.application.handlers.filesystem.OpenFolderHandler;
import com.make.equo.application.handlers.filesystem.ReadFileHandler;
import com.make.equo.application.handlers.filesystem.RenameFileHandler;
import com.make.equo.application.handlers.filesystem.SaveFileAsHandler;
import com.make.equo.application.handlers.filesystem.SaveFileHandler;
import com.make.equo.application.impl.HandlerBuilder;
import com.make.equo.application.util.IConstants;
import com.make.equo.contribution.api.IEquoContributionManager;
import com.make.equo.contribution.api.handler.ParameterizedHandler;
import com.make.equo.ws.api.IEquoEventHandler;

@Component(service = EquoApplicationBuilder.class)
public class EquoApplicationBuilder {

	private static final String ECLIPSE_RCP_APP_ID = "org.eclipse.ui.ide.workbench";

	private MApplication mApplication;
	private MTrimmedWindow mWindow;
	@Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
	private ViewBuilder viewBuilder;
	private EquoApplicationModel equoApplicationModel;
	private String applicationName;
	private IEquoEventHandler equoEventHandler;

	@Activate
	private void start() {
		System.setProperty("swt.chromium.args",
				"--proxy-server=localhost:9896;--ignore-certificate-errors;--allow-file-access-from-files;--disable-web-security;--enable-widevine-cdm;--proxy-bypass-list=127.0.0.1");
	}

	public OptionalViewBuilder webWrapper(String url) {
		return this.getViewBuilder().withSingleView(url);
	}

	public OptionalViewBuilder plainApp(String baseHtmlFile) throws URISyntaxException {
		return this.getViewBuilder().withBaseHtml(baseHtmlFile);
	}

	/**
	 * Configure the Equo application builder. This method is intended to be called
	 * by the Equo Framework, it should not be called by clients/users applications.
	 * 
	 * @param equoApplicationModel
	 * @param modelService
	 * @return
	 */
	OptionalViewBuilder configure(EquoApplicationModel equoApplicationModel, IEquoApplication equoApp) {
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

	private void listenForCommands() {
		equoEventHandler.on("_executeEclipseCommand", (JsonObject payload) -> {
			JsonElement commandAsJson = payload.get("commandId");
			String commandId = commandAsJson.getAsString();

			IEclipseContext eclipseContext = mApplication.getContext();
			ECommandService commandService = eclipseContext.get(ECommandService.class);
			Command command = commandService.getCommand(commandId);

			if (command != null) {
				Display.getDefault().asyncExec(() -> {
					Parameterization[] params = null;
					JsonElement responseId = payload.get(IConstants.EQUO_WEBSOCKET_PARAMS_RESPONSE_ID);
					if (responseId != null) {
						String[] parameters = { IConstants.EQUO_WEBSOCKET_PARAMS_FILE_PATH,
								IConstants.EQUO_WEBSOCKET_PARAMS_CONTENT };
						List<Parameterization> listParameters = new ArrayList<>();
						try {
							listParameters.add(new Parameterization(
									command.getParameter(IConstants.EQUO_WEBSOCKET_PARAMS_RESPONSE_ID),
									responseId.getAsString()));
							for (String idParameter : parameters) {
								JsonElement element = payload.get(idParameter);
								if (element != null && !element.isJsonNull()) {
									listParameters.add(new Parameterization(command.getParameter(idParameter),
											element.getAsString()));
								}
							}

							params = new Parameterization[listParameters.size()];
							listParameters.toArray(params);
						} catch (NotDefinedException e) {
							params = null;
							e.printStackTrace();
						}
					}
					ParameterizedCommand parametrizedCommand = new ParameterizedCommand(command, params);
					EHandlerService handlerService = eclipseContext.get(EHandlerService.class);
					handlerService.executeHandler(parametrizedCommand);
				});
			}
		});
	}

	private void registerGenericCommands() {
		ParameterizedHandler[] defaultHandlers = { new OpenFileHandler(), new OpenFolderHandler(),
				new DeleteFileHandler(), new SaveFileHandler(), new SaveFileAsHandler(), new FileInfoHandler(),
				new RenameFileHandler(), new MoveFileHandler(), new ReadFileHandler() };

		BundleContext bndContext = FrameworkUtil.getBundle(IEquoContributionManager.class).getBundleContext();
		ServiceReference<IEquoContributionManager> svcReference = bndContext
				.getServiceReference(IEquoContributionManager.class);
		IEquoContributionManager manager = bndContext.getService(svcReference);

		List<ParameterizedHandler> allHandlers = manager.getparameterizedHandlers();
		allHandlers.addAll(Arrays.asList(defaultHandlers));
		for (ParameterizedHandler handler : allHandlers) {
			createGenericCommand(mApplication, handler.getCommandId(), handler.getContributionUri(),
					handler.getParameters(), handler.getShortcut());
		}
	}

	private void configureEquoApp(String appId) {
		MMenu mainMenu = MenuFactoryImpl.eINSTANCE.createMenu();
		mainMenu.setElementId(appId + "." + "mainmenu");
		getmWindow().setMainMenu(mainMenu);

		createDefaultBindingContexts();

		MBindingTable mainWindowBindingTable = MCommandsFactory.INSTANCE.createBindingTable();
		mainWindowBindingTable.setBindingContext(getmApplication().getBindingContexts().get(0));
		mainWindowBindingTable.setElementId(IConstants.DEFAULT_BINDING_TABLE);

		getmApplication().getBindingTables().add(mainWindowBindingTable);

		addAppLevelCommands(getmApplication());
		listenForCommands();
	}

	private void addAppLevelCommands(MApplication mApplication) {
		createWebSocketTriggeredCommand(mApplication, IConstants.EQUO_WEBSOCKET_OPEN_BROWSER,
				IConstants.OPEN_BROWSER_COMMAND_CONTRIBUTION_URI);

		equoEventHandler.on("openBrowser", new ParameterizedCommandRunnable(IConstants.EQUO_WEBSOCKET_OPEN_BROWSER,
				getmApplication().getContext()));

		// if (action.equals(EXECUTE_ACTION_ID)) {
		// TODO call user application code with the class passed as param

		registerGenericCommands();
		createOpenBrowserAsWindowCommand(mApplication, IConstants.EQUO_OPEN_BROWSER_AS_WINDOW,
				IConstants.OPEN_BROWSER_AS_WINDOW_COMMAND_CONTRIBUTION_URI);
		createOpenBrowserAsSidePartCommand(mApplication, IConstants.EQUO_OPEN_BROWSER_AS_SIDE_PART,
				IConstants.OPEN_BROWSER_AS_SIDE_PART_COMMAND_CONTRIBUTION_URI);
		createUpdateBrowserCommand(mApplication, IConstants.EQUO_WEBSOCKET_UPDATE_BROWSER,
				IConstants.UPDATE_BROWSER_CONTRIBUTION_URI);

		equoEventHandler.on("updateBrowser", new ParameterizedCommandRunnable(IConstants.EQUO_WEBSOCKET_UPDATE_BROWSER,
				getmApplication().getContext()));
	}

	private void createGenericCommand(MApplication mApplication, String commandId, String commandContributionUri,
			IParameter[] parameters, String shortcut) {
		HandlerBuilder handlerBuilder = new HandlerBuilder(mApplication, commandId, commandContributionUri) {
			@Override
			protected Runnable getRunnable() {
				return null;
			}

			@Override
			protected List<MCommandParameter> createCommandParameters() {
				List<MCommandParameter> list = new ArrayList<>();
				for (IParameter parameter : parameters) {
					list.add(createCommandParameter(parameter.getId(), parameter.getName(), parameter.isOptional()));
				}
				return list;
			}

		};
		MCommand newCommand = handlerBuilder.createCommandAndHandler(commandId);
		new GlobalShortcutBuilder(this).addGlobalShortcutToExistingCommand(newCommand, shortcut);
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

}
