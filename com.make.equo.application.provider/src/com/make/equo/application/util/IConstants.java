package com.make.equo.application.util;

public interface IConstants {

	public static final String EQUO_APPLICATION_ID = "com.make.equo.application";

	public static final String EQUO_APP_PREFIX = "com.make.equo.app";

	public static final String MAIN_WINDOW_ID = "com.make.equo.trimmedwindow.mainpage";

	public static final String MAIN_PART_ID = "com.make.equo.part.main";

	public static final String MAIN_URL_KEY = "mainUrl";

	public static final String EQUO_HANDLER_PREFIX = "com.make.equo.handler";

	public static final String DEFAULT_HANDLER_IMPL_ID = EQUO_HANDLER_PREFIX + ".default";

	public static final String COMMAND_SUFFIX = ".command";

	public static final String HANDLER_SUFFIX = ".handler";

	public static final String PARAMETER_SUFFIX = ".parameter";

	public static final String RUNNABLE_OBJECT = "runnable";

	public static final String DEFAULT_BINDING_TABLE = "com.make.equo.application.bindingtable.default";

	public static final String DIALOGS_AND_WINDOWS_BINDING_CONTEXT = "org.eclipse.ui.contexts.dialogAndWindow";

	public static final String WINDOWS_BINDING_CONTEXT = "org.eclipse.ui.contexts.window";

	public static final String DIALOGS_BINDING_CONTEXT = "org.eclipse.ui.contexts.dialog";

	public static final String USER_KEY_BINDING_TAG = "type:user";

	public static final String COMMAND_ID_PARAMETER = "commandId";

	public static final String CUSTOM_SCRIPTS = "customScripts";

	public static final String APPLICATION_NAME_PARAMETER = "applicationName";

	public static final String EQUO_WEBSOCKET_PREFIX = "com.make.equo.websocket";

	public static final String EQUO_WEBSOCKET_OPEN_BROWSER = EQUO_WEBSOCKET_PREFIX + ".openbrowser";

	public static final String PARAMETERIZED_COMMAND_CONTRIBUTION_URI = "bundleclass://com.make.equo.application.provider/com.make.equo.application.handlers.ParameterizedCommandHandler";

	public static final String OPEN_BROWSER_COMMAND_CONTRIBUTION_URI = "bundleclass://com.make.equo.application.provider/com.make.equo.application.handlers.OpenBrowserCommandHandler";

	public static final String SINGLE_PART_CONTRIBUTION_URI = "bundleclass://com.make.equo.application.provider/com.make.equo.application.parts.SinglePagePart";

	public static final String EQUO_OPEN_BROWSER_AS_WINDOW = EQUO_APPLICATION_ID + ".openbrowseraswindow";

	public static final String OPEN_BROWSER_AS_WINDOW_COMMAND_CONTRIBUTION_URI = "bundleclass://com.make.equo.application.provider/com.make.equo.application.handlers.OpenBrowserAsWindowCommandHandler";

	public static final String EQUO_OPEN_BROWSER_AS_SIDE_PART = EQUO_APPLICATION_ID + ".openbrowserassidepart";

	public static final String OPEN_BROWSER_AS_SIDE_PART_COMMAND_CONTRIBUTION_URI = "bundleclass://com.make.equo.application.provider/com.make.equo.application.handlers.OpenBrowserAsSidePartCommandHandler";

	public static final String EQUO_BROWSER_WINDOW_NAME = EQUO_APPLICATION_ID + ".browser.window.name";

	public static final String EQUO_BROWSER_PART_NAME = EQUO_APPLICATION_ID + ".browser.part.name";

	public static final String EQUO_BROWSER_PART_POSITION = EQUO_APPLICATION_ID + ".browser.part.position";

	public static final String EQUO_WEBSOCKET_UPDATE_BROWSER = EQUO_WEBSOCKET_PREFIX + ".updatebrowser";

	public static final String UPDATE_BROWSER_CONTRIBUTION_URI = "bundleclass://com.make.equo.application.provider/com.make.equo.application.handlers.UpdateBrowserCommandHandler";

	public static final String EQUO_PARTSTACK_ID = EQUO_APPLICATION_ID + ".partstack";

	public static final String EQUO_PART_IN_PARTSTACK_ID = EQUO_PARTSTACK_ID + ".part";

	public static final String EQUO_BROWSER_IN_PARTSTACK_ID = EQUO_PART_IN_PARTSTACK_ID + ".browser";

	public static final String EQUO_WEBSOCKET_USER_EMITTED_EVENT = EQUO_WEBSOCKET_PREFIX + ".useremittedevent";
	
	public static final String DEFAULT_EXIT_LABEL = "Exit";
	
	public static final String DEFAULT_ABOUT_LABEL = "About";
	
	public static final String DEFAULT_PREFERENCES_LABEL = "Preferences";

	public static final String MAC_OSX = "mac";

	public static final String IS_AN_EQUO_MODEL_ELEMENT = "isAnEquoModelElement";

}
