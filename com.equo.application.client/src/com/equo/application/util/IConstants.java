/****************************************************************************
**
** Copyright (C) 2021 Equo
**
** This file is part of Equo Framework.
**
** Commercial License Usage
** Licensees holding valid commercial Equo licenses may use this file in
** accordance with the commercial license agreement provided with the
** Software or, alternatively, in accordance with the terms contained in
** a written agreement between you and Equo. For licensing terms
** and conditions see https://www.equoplatform.com/terms.
**
** GNU General Public License Usage
** Alternatively, this file may be used under the terms of the GNU
** General Public License version 3 as published by the Free Software
** Foundation. Please review the following
** information to ensure the GNU General Public License requirements will
** be met: https://www.gnu.org/licenses/gpl-3.0.html.
**
****************************************************************************/

package com.equo.application.util;

/**
 * Equo constants.
 */
public interface IConstants {

  public static final String EQUO_APPLICATION_ID = "com.equo.application";

  public static final String EQUO_APP_PREFIX = "com.equo.app";

  public static final String MAIN_WINDOW_ID = "com.equo.trimmedwindow.mainpage";

  public static final String MAIN_PART_ID = "com.equo.part.main";

  public static final String MAIN_URL_KEY = "mainUrl";

  public static final String MAIN_URL_WS_PORT = "equoCommPort";

  public static final String EQUO_HANDLER_PREFIX = "com.equo.handler";

  public static final String DEFAULT_HANDLER_IMPL_ID = EQUO_HANDLER_PREFIX + ".default";

  public static final String COMMAND_SUFFIX = ".command";

  public static final String HANDLER_SUFFIX = ".handler";

  public static final String PARAMETER_SUFFIX = ".parameter";

  public static final String RUNNABLE_OBJECT = "runnable";

  public static final String DEFAULT_BINDING_TABLE = "com.equo.application.bindingtable.default";

  public static final String DIALOGS_AND_WINDOWS_BINDING_CONTEXT =
      "org.eclipse.ui.contexts.dialogAndWindow";

  public static final String WINDOWS_BINDING_CONTEXT = "org.eclipse.ui.contexts.window";

  public static final String DIALOGS_BINDING_CONTEXT = "org.eclipse.ui.contexts.dialog";

  public static final String USER_KEY_BINDING_TAG = "type:user";

  public static final String COMMAND_ID_PARAMETER = "commandId";

  public static final String CUSTOM_SCRIPTS = "customScripts";

  public static final String APPLICATION_NAME_PARAMETER = "applicationName";

  public static final String EQUO_WEBSOCKET_PREFIX = "com.equo.websocket";

  public static final String EQUO_WEBSOCKET_OPEN_BROWSER = EQUO_WEBSOCKET_PREFIX + ".openbrowser";

  public static final String PARAMETERIZED_COMMAND_CONTRIBUTION_URI = "bundleclass://"
      + "com.equo.application.client/com.equo.application.handlers.ParameterizedCommandHandler";

  public static final String OPEN_BROWSER_COMMAND_CONTRIBUTION_URI = "bundleclass://"
      + "com.equo.application.client/com.equo.application.handlers.OpenBrowserCommandHandler";

  public static final String SINGLE_PART_CONTRIBUTION_URI =
      "bundleclass://" + "com.equo.application.client/com.equo.application.parts.SinglePagePart";

  public static final String EQUO_OPEN_BROWSER_AS_WINDOW =
      EQUO_APPLICATION_ID + ".openbrowseraswindow";

  public static final String OPEN_BROWSER_AS_WINDOW_COMMAND_CONTRIBUTION_URI =
      "bundleclass://com.equo.application.client/"
          + "com.equo.application.handlers.OpenBrowserAsWindowCommandHandler";

  public static final String EQUO_OPEN_BROWSER_AS_SIDE_PART =
      EQUO_APPLICATION_ID + ".openbrowserassidepart";

  public static final String OPEN_BROWSER_AS_SIDE_PART_COMMAND_CONTRIBUTION_URI =
      "bundleclass://com.equo.application.client/"
          + "com.equo.application.handlers.OpenBrowserAsSidePartCommandHandler";

  public static final String EQUO_BROWSER_WINDOW_NAME =
      EQUO_APPLICATION_ID + ".browser.window.name";

  public static final String EQUO_BROWSER_PART_NAME = EQUO_APPLICATION_ID + ".browser.part.name";

  public static final String EQUO_BROWSER_PART_POSITION =
      EQUO_APPLICATION_ID + ".browser.part.position";

  public static final String EQUO_WEBSOCKET_UPDATE_BROWSER =
      EQUO_WEBSOCKET_PREFIX + ".updatebrowser";

  public static final String UPDATE_BROWSER_CONTRIBUTION_URI = "bundleclass://"
      + "com.equo.application.client/com.equo.application.handlers.UpdateBrowserCommandHandler";

  public static final String EQUO_PARTSTACK_ID = EQUO_APPLICATION_ID + ".partstack";

  public static final String EQUO_PART_IN_PARTSTACK_ID = EQUO_PARTSTACK_ID + ".part";

  public static final String EQUO_BROWSER_IN_PARTSTACK_ID = EQUO_PART_IN_PARTSTACK_ID + ".browser";

  public static final String EQUO_WEBSOCKET_USER_EMITTED_EVENT =
      EQUO_WEBSOCKET_PREFIX + ".useremittedevent";

  public static final String DEFAULT_EXIT_LABEL = "Exit";

  public static final String DEFAULT_ABOUT_LABEL = "About";

  public static final String DEFAULT_PREFERENCES_LABEL = "Preferences";

  public static final String MAC_OSX = "mac";

  public static final String IS_AN_EQUO_MODEL_ELEMENT = "isAnEquoModelElement";

  public static final String ITEM_SHORTCUT = "itemshortcut";

  public static final String ITEM_RUNNABLE = "itemrunnable";

  public static final String ITEM_ACTION = "itemaction";

  public static final String DEV_APP_URL = "dev.app.url";

}
