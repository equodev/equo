/****************************************************************************
**
** Copyright (C) 2021 Equo
**
** This file is part of the Equo SDK.
**
** Commercial License Usage
** Licensees holding valid commercial Equo licenses may use this file in
** accordance with the commercial license agreement provided with the
** Software or, alternatively, in accordance with the terms contained in
** a written agreement between you and Equo. For licensing terms
** and conditions see https://www.equo.dev/terms.
**
** GNU General Public License Usage
** Alternatively, this file may be used under the terms of the GNU
** General Public License version 3 as published by the Free Software
** Foundation. Please review the following
** information to ensure the GNU General Public License requirements will
** be met: https://www.gnu.org/licenses/gpl-3.0.html.
**
****************************************************************************/

package com.equo.application.model;

import java.util.List;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MBindingContext;
import org.eclipse.e4.ui.model.application.commands.MCommandParameter;
import org.eclipse.e4.ui.model.application.commands.MCommandsFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.equo.application.api.IEquoApplication;
import com.equo.application.handlers.ParameterizedCommandRunnable;
import com.equo.application.impl.HandlerBuilder;
import com.equo.application.util.IConstants;
import com.equo.comm.api.IEquoEventHandler;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * Builder needed to create an Equo app.
 */
@Component(service = EquoApplicationBuilder.class)
public class EquoApplicationBuilder {
  private static EquoApplicationBuilder currentBuilder;
  private static final String ECLIPSE_RCP_APP_ID = "org.eclipse.ui.ide.workbench";

  @Reference(cardinality = ReferenceCardinality.MANDATORY, policy = ReferencePolicy.STATIC)
  private ViewBuilder viewBuilder;

  private MApplication mApplication;
  private MTrimmedWindow mWindow;

  private EquoApplicationModel equoApplicationModel;
  private String applicationName;
  private IEquoEventHandler equoEventHandler;
  private OptionalViewBuilder optionalViewBuilder;

  /**
   * Builds an Equo application by wrapping up an existing web app.
   * @param  url the website to be wrapped
   * @return     app builder
   */
  public OptionalViewBuilder wrap(String url) {
    optionalViewBuilder = this.getViewBuilder().withSingleView(url);
    return optionalViewBuilder;
  }

  /**
   * Builds an Equo App with the UI starting point in the HTML file given by
   * parameter.
   * @param  baseHtmlFile path to HTML file, relative to 'resources' folder
   * @return              app builder
   */
  public OptionalViewBuilder withUI(String baseHtmlFile) {
    optionalViewBuilder = this.getViewBuilder().withBaseHtml(baseHtmlFile);
    return optionalViewBuilder;
  }

  /**
   * Configures the Equo application builder. This method is intended to be called
   * by the Equo SDK, it should not be called by clients/users applications.
   * @return app builder
   */
  OptionalViewBuilder configure(EquoApplicationModel equoApplicationModel,
      IEquoApplication equoApp) {
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
      return this.viewBuilder.configureViewPart(this, equoApp, appId);
    }
    return null;
  }

  private void configureEquoApp(String appId) {
    createDefaultBindingsAndCommandsIfNeeded();
    configureJavascriptApis();
  }

  private void configureJavascriptApis() {
    equoEventHandler.on("_setMenu", payload -> {

      CustomDeserializer deserializer = new CustomDeserializer();
      deserializer.registerMenuType(EquoMenuItem.CLASSNAME, EquoMenuItem.class);
      deserializer.registerMenuType(EquoMenuItemSeparator.CLASSNAME, EquoMenuItemSeparator.class);

      Gson gson = new GsonBuilder().registerTypeAdapter(Menu.class, deserializer).create();
      gson.fromJson(payload, Menu.class).setApplicationMenu();
    });

    equoEventHandler.on("_getMenu", JsonObject.class, payload -> {
      equoEventHandler.send("_doGetMenu", Menu.getActiveMenu().serialize());
    });

    equoEventHandler.on("_addShortcut", JsonObject.class, payload -> {
      final String shortcut = payload.get("shortcut").getAsString();
      final String event = payload.get("event").getAsString();
      new GlobalShortcutBuilder(this, this.viewBuilder.getPart().getElementId(), null, event)
          .addGlobalShortcut(shortcut);
    });

    equoEventHandler.on("_removeShortcut", JsonObject.class, payload -> {
      final String shortcut = payload.get("shortcut").getAsString();
      new GlobalShortcutBuilder(this, this.viewBuilder.getPart().getElementId(), null, null)
          .removeShortcut(shortcut);
    });
  }

  private void addAppLevelCommands(MApplication mApplication) {
    createCommTriggeredCommand(mApplication, IConstants.EQUO_COMM_OPEN_BROWSER,
        IConstants.OPEN_BROWSER_COMMAND_CONTRIBUTION_URI);

    equoEventHandler.on("openBrowser", new ParameterizedCommandRunnable(
        IConstants.EQUO_COMM_OPEN_BROWSER, getmApplication().getContext()));

    // if (action.equals(EXECUTE_ACTION_ID)) {
    // TODO call user application code with the class passed as param

    createOpenBrowserAsWindowCommand(mApplication, IConstants.EQUO_OPEN_BROWSER_AS_WINDOW,
        IConstants.OPEN_BROWSER_AS_WINDOW_COMMAND_CONTRIBUTION_URI);
    createOpenBrowserAsSidePartCommand(mApplication, IConstants.EQUO_OPEN_BROWSER_AS_SIDE_PART,
        IConstants.OPEN_BROWSER_AS_SIDE_PART_COMMAND_CONTRIBUTION_URI);
    createUpdateBrowserCommand(mApplication, IConstants.EQUO_COMM_UPDATE_BROWSER,
        IConstants.UPDATE_BROWSER_CONTRIBUTION_URI);

    equoEventHandler.on("updateBrowser", new ParameterizedCommandRunnable(
        IConstants.EQUO_COMM_UPDATE_BROWSER, getmApplication().getContext()));
  }

  private void createUpdateBrowserCommand(MApplication mApplication, String commandId,
      String commandContributionUri) {
    HandlerBuilder handlerBuilder =
        new HandlerBuilder(mApplication, commandId, commandContributionUri) {
          @Override
          protected Runnable getRunnable() {
            return null;
          }

          @Override
          protected List<MCommandParameter> createCommandParameters() {
            MCommandParameter partNameCommandParameter =
                createCommandParameter(IConstants.EQUO_BROWSER_PART_NAME, "Part Name", true);
            return Lists.newArrayList(partNameCommandParameter);
          }

        };
    handlerBuilder.createCommandAndHandler(commandId);
  }

  private void createOpenBrowserAsSidePartCommand(MApplication mApplication, String commandId,
      String commandContributionUri) {
    HandlerBuilder handlerBuilder =
        new HandlerBuilder(mApplication, commandId, commandContributionUri) {
          @Override
          protected Runnable getRunnable() {
            return null;
          }

          @Override
          protected List<MCommandParameter> createCommandParameters() {
            MCommandParameter partNameCommandParameter =
                createCommandParameter(IConstants.EQUO_BROWSER_PART_NAME, "Part Name", true);
            MCommandParameter partPositionCommandParameter = createCommandParameter(
                IConstants.EQUO_BROWSER_PART_POSITION, "Part Position", true);
            return Lists.newArrayList(partNameCommandParameter, partPositionCommandParameter);
          }

        };
    handlerBuilder.createCommandAndHandler(commandId);
  }

  private void createOpenBrowserAsWindowCommand(MApplication mApplication, String commandId,
      String commandContributionUri) {
    HandlerBuilder handlerBuilder =
        new HandlerBuilder(mApplication, commandId, commandContributionUri) {
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

  private void createCommTriggeredCommand(MApplication mApplication, String commandId,
      String commandContributionUri) {
    HandlerBuilder handlerBuilder =
        new HandlerBuilder(mApplication, commandId, commandContributionUri) {
          @Override
          protected Runnable getRunnable() {
            return null;
          }
        };
    handlerBuilder.createCommandAndHandler(commandId);
  }

  private void createDefaultBindingsAndCommandsIfNeeded() {
    if (!getmApplication().getRootContext().isEmpty()) {
      return;
    }

    MBindingContext windowAndDialogBindingContext =
        MCommandsFactory.INSTANCE.createBindingContext();
    windowAndDialogBindingContext.setElementId(IConstants.DIALOGS_AND_WINDOWS_BINDING_CONTEXT);
    windowAndDialogBindingContext.setName("Dialogs and Windows Binding Context");
    windowAndDialogBindingContext.setDescription("Either a window or a dialog is open");

    MBindingContext windowBindingContext = MCommandsFactory.INSTANCE.createBindingContext();
    windowBindingContext.setElementId(IConstants.WINDOWS_BINDING_CONTEXT);
    windowBindingContext.setName("Windows Binding Context");
    windowBindingContext.setDescription("A window is open");

    MBindingContext dialogBindingContext = MCommandsFactory.INSTANCE.createBindingContext();
    dialogBindingContext.setElementId(IConstants.DIALOGS_BINDING_CONTEXT);
    dialogBindingContext.setName("Dialogs Binding Context");
    dialogBindingContext.setDescription("A dialog is open");

    windowAndDialogBindingContext.getChildren().add(windowBindingContext);
    windowAndDialogBindingContext.getChildren().add(dialogBindingContext);

    // keep this order, the order in which they are added is important.
    getmApplication().getRootContext().add(windowAndDialogBindingContext);
    getmApplication().getBindingContexts().add(windowAndDialogBindingContext);

    addAppLevelCommands(getmApplication());
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
